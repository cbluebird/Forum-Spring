package org.jh.forum.oss.VO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BatchUploadVO {
    private List<String> fileUrls = new ArrayList<>();
}
