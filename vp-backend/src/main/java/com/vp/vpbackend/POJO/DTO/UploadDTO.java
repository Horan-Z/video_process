package com.vp.vpbackend.POJO.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadDTO {
    private String fileName;
    private String fileUuid;
    private String fileExtension;
    private String filePath;
}
