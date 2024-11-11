package org.wtm.web.auth.security.exception;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.MediaType;

public class AccessTokenException extends RuntimeException {

  TOKEN_ERROR token_error;

  public enum TOKEN_ERROR {

    UNACCEPTED(401, "Token is null or too short"),
    BAD_TYPE(401, "Token type is Bearer"),
    MALFORMED(403, "Malformed Token"),
    BAD_SIGNATURE(403, "Bad Signature Token"),
    EXPIRED_TOKEN(403, "Expired Token");

    @Getter
    private final int status;
    @Getter
    private final String msg;

    TOKEN_ERROR(int status, String msg) {
      this.status = status;
      this.msg = msg;
    }
  }

  public AccessTokenException(TOKEN_ERROR error){
    super(error.name());
    this.token_error = error;
  }

  public void sendResponseError(HttpServletResponse response){

    response.setStatus(token_error.getStatus());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    Gson gson = new Gson();

    String responseStr = gson.toJson(Map.of("msg", token_error.getMsg(), "time", new Date()));

    try {
      response.getWriter().write(responseStr);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
