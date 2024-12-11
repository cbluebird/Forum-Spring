package org.jh.forum.oss.controller;

import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.oss.VO.BatchUploadVO;
import org.jh.forum.oss.VO.UploadVO;
import org.jh.forum.oss.service.IOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/oss")
public class OssController {
    @Autowired
    private IOssService ossService;

    @PostMapping("/upload")
    public UploadVO upload(@RequestParam(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BizException(ErrorCode.COMMON_PARAM_ERROR, "文件不能为空");
        }
        String fileUrl = ossService.upload(file);
        UploadVO uploadVO = new UploadVO();
        uploadVO.setFileUrl(fileUrl);
        return uploadVO;
    }

    @PostMapping("/upload/batch")
    public BatchUploadVO upload(@RequestParam("files") MultipartFile[] files) {
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new BizException(ErrorCode.COMMON_PARAM_ERROR, "文件不能为空");
            }
        }
        BatchUploadVO batchUploadVO = new BatchUploadVO();
        for (MultipartFile file : files) {
            String fileUrl = ossService.upload(file);
            batchUploadVO.getFileUrls().add(fileUrl);
        }
        return batchUploadVO;
    }
}
