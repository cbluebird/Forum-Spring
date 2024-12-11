package org.jh.forum.oss.service.impl;

import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.oss.config.QiniuConfig;
import org.jh.forum.oss.service.IOssService;
import org.jh.forum.oss.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Objects;

@Service
public class OssServiceImpl implements IOssService {
    @Autowired
    private QiniuConfig qiniuConfig;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private Auth auth;

    @Override
    public String upload(MultipartFile file) {
        try {
            String upToken = auth.uploadToken(qiniuConfig.getBucketName());
            String fileName = StringUtil.getRandomFileName(Objects.requireNonNull(file.getOriginalFilename()));
            String key = qiniuConfig.getPath() + fileName;
            InputStream inputStream = file.getInputStream();
            Response response = uploadManager.put(inputStream, key, upToken, null, null);
            if (!response.isOK()) {
                throw new BizException(ErrorCode.OSS_UPLOAD_ERROR);
            }
            return qiniuConfig.getDomain() + "/" + key;
        } catch (Exception e) {
            throw new BizException(ErrorCode.OSS_UPLOAD_ERROR, e);
        }
    }
}
