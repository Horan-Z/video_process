package com.vp.vpbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
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

    @SaCheckLogin
    @GetMapping("/sts")
    public Result getSts() {
        return ossService.getSts();
    }

    @PostMapping("/upload-callback")
    public Result uploadCallback(@ModelAttribute UploadDTO uploadDTO) {
        return ossService.registerUpload(uploadDTO);
    }
}