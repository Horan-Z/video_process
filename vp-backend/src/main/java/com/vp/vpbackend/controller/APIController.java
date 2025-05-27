package com.vp.vpbackend.controller;

import com.vp.vpbackend.POJO.Result;
import com.vp.vpbackend.service.AliService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class APIController {

    private final AliService aliService;

    @GetMapping("/sts")
    public Result getSts() {
        return aliService.getSts();
    }
}
