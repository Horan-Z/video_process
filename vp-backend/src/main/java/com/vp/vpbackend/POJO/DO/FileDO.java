package com.vp.vpbackend.POJO.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("file")
@Data
public class FileDO {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String belongTo;

    private Short fileType;

    private String fileName;

    private String fileUuid;

    private String fileExtension;

    private String filePath;

    private Long fileSizeBytes;

    private String videoCodec;

    private String videoFormat;

    private Long videoBitrate;

    private Long videoDurationMillis;

    private LocalDateTime createTime;

    private LocalDateTime lastUsedTime;
}
