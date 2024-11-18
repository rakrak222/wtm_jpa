package org.wtm.web.common.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Optional;

@Service
public class GeocodingService {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    private static final String GEOCODING_URL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";

    @Cacheable(value = "geocodingCache", key = "#address")
    public Optional<Coordinates> getCoordinates(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            String urlStr = GEOCODING_URL + "?query=" + encodedAddress;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

            int responseCode = conn.getResponseCode();
            if(responseCode != 200){
                // 에러 처리
                return Optional.empty();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            in.close();

            // JSON 파싱 (Jackson 사용)
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.toString());
            JsonNode addresses = root.path("addresses");
            if (addresses.isArray() && addresses.size() > 0) {
                JsonNode first = addresses.get(0);
                double lat = first.path("y").asDouble();
                double lng = first.path("x").asDouble();
                return Optional.of(new Coordinates(lat, lng));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Coordinates 클래스 정의
    public static class Coordinates {
        private double latitude;
        private double longitude;

        // Constructor
        public Coordinates(double latitude, double longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }

        // Getters
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
    }
}
