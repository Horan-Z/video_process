package com.vp.vpbackend.service;

import cn.dev33.satoken.stp.StpUtil;
import com.github.kokorin.jaffree.ffmpeg.*;
import com.github.kokorin.jaffree.ffprobe.FFprobe;
import com.github.kokorin.jaffree.ffprobe.FFprobeResult;
import com.github.kokorin.jaffree.ffprobe.Stream;
import com.vp.vpbackend.POJO.DO.FileDO;
import com.vp.vpbackend.POJO.DTO.UploadDTO;
import com.vp.vpbackend.POJO.Result;
import com.vp.vpbackend.common.AliyunSTS;
import com.vp.vpbackend.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class OssService {

    private final FileMapper fileMapper;

    public Result getSts() {
        if(StpUtil.isLogin()) {
            return new Result(200, "OK", AliyunSTS.getStsCredentials());
        }
        return new Result(400, "Fail", null);
    }

    public Result registerUpload(UploadDTO uploadDTO) {
        FileDO fileDo = new FileDO();
        final String basePath = "/tmp/ossfs";

        fileDo.setFileName(uploadDTO.getFileName());
        fileDo.setFileUuid(uploadDTO.getFileUuid());
        fileDo.setFileExtension(uploadDTO.getFileExtension());
        fileDo.setFilePath(basePath + uploadDTO.getFilePath());
        fileDo.setBelongTo(StpUtil.getLoginIdAsString());

        final AtomicLong durationMillis = new AtomicLong();
        FFmpeg.atPath()
                .addInput(
                        UrlInput.fromUrl(basePath + uploadDTO.getFilePath() + uploadDTO.getFileUuid() + '.' + uploadDTO.getFileExtension())
                )
                .addOutput(new NullOutput())
                .setProgressListener(progress -> durationMillis.set(progress.getTimeMillis()))
                .execute();

        // 获取视频时长(ms)
        fileDo.setVideoDurationMillis(durationMillis.get());

        FFprobeResult result = FFprobe.atPath()
                .setShowFormat(true)
                .setShowStreams(true)
                .setInput(basePath + uploadDTO.getFilePath() + uploadDTO.getFileUuid() + '.' + uploadDTO.getFileExtension())
                .execute();
        for (Stream stream : result.getStreams()) {
            if("video".equalsIgnoreCase(stream.getCodecType().toString())) {

                // 获取第一个视频流的编码格式
                fileDo.setVideoCodec(stream.getCodecName());

                break;
            }
        }
        fileDo.setVideoBitrate(result.getFormat().getBitRate());
        fileDo.setVideoFormat(result.getFormat().getFormatLongName());
        fileDo.setFileSizeBytes(result.getFormat().getSize());

        fileDo.setCreateTime(LocalDateTime.now());
        fileDo.setLastUsedTime(LocalDateTime.now());

        fileMapper.insert(fileDo);
        return new Result(200, "OK", fileDo);
    }
}
