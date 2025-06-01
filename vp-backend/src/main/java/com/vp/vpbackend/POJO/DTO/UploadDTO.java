package com.vp.vpbackend.POJO.DTO;

import lombok.Data;

@Data
public class UploadDTO {
    private String fileName;
    private String fileUuid;
    private String fileExtension;
    private String filePath;
}
