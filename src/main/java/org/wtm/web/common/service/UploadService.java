package org.wtm.web.common.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    String uploadFile(MultipartFile file, String uploadDirType);
}
