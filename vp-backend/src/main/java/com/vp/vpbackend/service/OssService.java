package com.vp.vpbackend.service;

import cn.dev33.satoken.stp.StpUtil;
import com.vp.vpbackend.POJO.DO.FileDO;
import com.vp.vpbackend.POJO.DTO.UploadDTO;
import com.vp.vpbackend.POJO.Result;
import com.vp.vpbackend.common.AliyunSTS;
import com.vp.vpbackend.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OssService {

    private final FileMapper fileMapper;

    private final MediaProcessingService mediaProcessingService;

    public Result getSts() {
        if(StpUtil.isLogin()) {
            return new Result(200, "OK", AliyunSTS.getStsCredentials());
        }
        return new Result(400, "Fail", null);
    }

    public Result registerUpload(UploadDTO uploadDTO) {
        FileDO fileDo = new FileDO();
        final String basePath = "/tmp/ossfs";

        // 设置基本文件信息
        fileDo.setFileName(uploadDTO.getFileName());
        fileDo.setFileUuid(uploadDTO.getFileUuid());
        fileDo.setFileExtension(uploadDTO.getFileExtension());
        fileDo.setFilePath(basePath + uploadDTO.getFilePath());
        fileDo.setBelongTo(StpUtil.getLoginIdAsString());
        fileDo.setCreateTime(LocalDateTime.now());
        fileDo.setLastUsedTime(LocalDateTime.now());
        fileDo.setFileType((short) 0);

        fileDo.setVideoFormat("null");
        fileDo.setVideoCodec("null");
        fileDo.setFileSizeBytes(0L);
        fileDo.setVideoDurationMillis(0L);
        fileDo.setVideoBitrate(0L);

        // 先保存基本信息到数据库，此时 fileDo 会被回填 ID
        fileMapper.insert(fileDo);

        // 异步处理媒体信息，传递 ID 而不是整个对象
        mediaProcessingService.processMedia(fileDo, uploadDTO);
        System.out.println("Async");
        // 立即返回结果，不等待媒体处理完成
        return new Result(200, "OK", fileDo);
    }
}
