package com.vp.vpbackend;

import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.NullOutput;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffprobe.FFprobe;
import com.github.kokorin.jaffree.ffprobe.FFprobeResult;
import com.github.kokorin.jaffree.ffprobe.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest
class VpBackendApplicationTests {

    @Test
    void testJaffree() {
        final AtomicLong durationMillis = new AtomicLong();
        FFmpeg.atPath()
                .addInput(
                        UrlInput.fromUrl("Family.Guy.S01E02.1999.1080p.DSNP.WEB-DL.H264.AAC-ADWeb.mkv")
                )
                .addOutput(new NullOutput())
                .setProgressListener(progress -> durationMillis.set(progress.getTimeMillis()))
                .execute();
        FFprobeResult result = FFprobe.atPath()
                .setShowFormat(true)
                .setShowStreams(true)
                .setInput("Family.Guy.S01E02.1999.1080p.DSNP.WEB-DL.H264.AAC-ADWeb.mkv")
                .execute();
        for (Stream stream : result.getStreams()) {
            System.out.println("Stream #" + stream.getIndex()
                    + " type: " + stream.getCodecType()
                    + " name: " + stream.getCodecName());
        }
        System.out.println("Format: " + result.getFormat().getSize());
        System.out.println("Exact duration: " + durationMillis.get() + " milliseconds");
    }
}
