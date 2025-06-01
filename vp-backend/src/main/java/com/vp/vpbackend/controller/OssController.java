package com.vp.vpbackend.controller;

import com.vp.vpbackend.POJO.DTO.UploadDTO;
import com.vp.vpbackend.POJO.Result;
import com.vp.vpbackend.service.OssService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/oss")
public class OssController {

    private final OssService ossService;

    @GetMapping("/sts")
    public Result getSts() {
        return ossService.getSts();
    }

    @PostMapping("/register-upload")
    public Result registerUpload(@RequestBody UploadDTO uploadDTO) {
        return ossService.registerUpload(uploadDTO);
    }
}
