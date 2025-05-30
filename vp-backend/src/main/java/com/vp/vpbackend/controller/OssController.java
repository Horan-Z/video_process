package com.vp.vpbackend.controller;

import com.vp.vpbackend.POJO.Result;
import com.vp.vpbackend.service.OssService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/oss")
public class OssController {

    private final OssService ossService;

    @GetMapping("/sts")
    public Result getSts() {
        return ossService.getSts();
    }
}
