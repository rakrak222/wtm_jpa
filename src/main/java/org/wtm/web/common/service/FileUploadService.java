package org.wtm.web.common.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.common.configuration.FileUploadProperties;

@Service
@RequiredArgsConstructor
public class FileUploadService implements UploadService{

    private final FileUploadProperties fileUploadProperties;

    public String uploadFile(MultipartFile file, String uploadDir) {
        if (file == null || file.isEmpty()) {
            // 파일이 없거나 비어 있으면 null 반환
            return null;
        }

        try {
            // 현재 날짜로 폴더 경로 생성(yyyy/MM/dd)
            String dateFolder = new SimpleDateFormat("yyyy" + File.separator + "MM" + File.separator + "dd").format(new Date());
            String directoryPath = fileUploadProperties.getBaseUploadDir() + File.separator + uploadDir + File.separator + dateFolder;
            // 날짜별 디렉토리 생성
            synchronized (this) {
                File directory = new File(directoryPath);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
            }

            // 현재 날짜와 UUID를 조합한 파일 이름 생성
            String uuid = UUID.randomUUID().toString();
            String originalFileExtension = getFileExtension(file.getOriginalFilename());
            String newFileName = uuid + originalFileExtension;

            // 파일 경로 설정
            String filePath = directoryPath + File.separator + newFileName;

            // 파일 저장
            File dest = new File(filePath);
            file.transferTo(dest);

            // 브라우저 접근 가능한 URL 추가
            return "/uploads/" + uploadDir + File.separator + dateFolder + File.separator+ newFileName;

        } catch (IOException e) {
            // 파일 업로드에 실패 시 작업 반환
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files, String uploadDirType) {
        List<String> filePaths = new ArrayList<>();

        // 날짜별 폴더 생성 한번만 수행
        String dateFolder = new SimpleDateFormat("yyyy" + File.separator + "MM" + File.separator + "dd").format(new Date());
        String directoryPath = fileUploadProperties.getBaseUploadDir() + File.separator + uploadDirType + File.separator + dateFolder;

        synchronized (this) {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }

        for (MultipartFile file : files) {
              if (file == null || file.isEmpty()) {
                  filePaths.add(null);
              }else {
                  try {
                      String uuid = UUID.randomUUID().toString();
                      String originalFileExtension = getFileExtension(file.getOriginalFilename());
                      String newFileName = uuid + originalFileExtension;

                      String filePath = directoryPath + File.separator + newFileName;

                      File dest = new File(filePath);
                      file.transferTo(dest);

                      // 브라우저 접근 가능한 URL 추가
                      filePaths.add("/uploads/" + uploadDirType + File.separator + dateFolder + File.separator + newFileName);
                  } catch (IOException e) {
                      throw new RuntimeException("Failed to upload file", e);
                  }
              }
        }
        return filePaths;
    }

    // 파일 확장자 추출 메서드
    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }
}

