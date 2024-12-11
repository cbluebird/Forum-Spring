package org.jh.forum.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface IOssService {
    String upload(MultipartFile file);
}
