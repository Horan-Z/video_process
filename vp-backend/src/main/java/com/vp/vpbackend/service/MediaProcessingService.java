package com.vp.vpbackend.service;

import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.NullOutput;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffprobe.FFprobe;
import com.github.kokorin.jaffree.ffprobe.FFprobeResult;
import com.github.kokorin.jaffree.ffprobe.Stream;
import com.vp.vpbackend.POJO.DO.FileDO;
import com.vp.vpbackend.POJO.DTO.UploadDTO;
import com.vp.vpbackend.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaProcessingService {

    private final FileMapper fileMapper;

    @Async("mediaProcessingExecutor") // 指定使用的线程池
    public void processMedia(FileDO fileDo, UploadDTO uploadDTO) {
        try {
            final String basePath = "/tmp/ossfs2-bucket";

            // 执行 FFmpeg 命令获取视频时长
            final AtomicLong durationMillis = new AtomicLong();
            FFmpeg.atPath()
                    .addInput(
                            UrlInput.fromUrl(basePath + uploadDTO.getFilePath() + uploadDTO.getFileUuid() + '.' + uploadDTO.getFileExtension())
                    )
                    .addOutput(new NullOutput())
                    .setProgressListener(progress -> durationMillis.set(progress.getTimeMillis()))
                    .execute();

            // 设置视频时长
            fileDo.setVideoDurationMillis(durationMillis.get());

            // 执行 FFprobe 命令获取视频信息
            FFprobeResult result = FFprobe.atPath()
                    .setShowFormat(true)
                    .setShowStreams(true)
                    .setInput(basePath + uploadDTO.getFilePath() + uploadDTO.getFileUuid() + '.' + uploadDTO.getFileExtension())
                    .execute();

            // 解析视频流信息
            for (Stream stream : result.getStreams()) {
                if ("video".equalsIgnoreCase(stream.getCodecType().toString())) {
                    fileDo.setVideoCodec(stream.getCodecName());
                    break;
                }
            }

            // 设置其他视频信息
            fileDo.setVideoBitrate(result.getFormat().getBitRate());
            fileDo.setVideoFormat(result.getFormat().getFormatLongName());
            fileDo.setFileSizeBytes(result.getFormat().getSize());

            // 更新文件信息到数据库
            fileMapper.insert(fileDo);

        } catch (Exception e) {
            // 处理异常，可以记录日志或进行其他操作
            log.error("处理媒体文件失败: {}", e.getMessage(), e);
        }
    }
}
