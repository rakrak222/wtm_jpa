package org.wtm.web.common.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadService {

    String uploadFile(MultipartFile file, String uploadDirType);

    List<String> uploadFiles(List<MultipartFile> files, String uploadDirType);
}
