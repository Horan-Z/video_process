package com.vp.vpbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.vp.vpbackend.POJO.DTO.GetListDTO;
import com.vp.vpbackend.POJO.Result;
import com.vp.vpbackend.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
@SaCheckLogin
public class VideoController {

    private final VideoService videoService;

    @GetMapping("/list")
    public Result getList(@ModelAttribute GetListDTO getListDTO) {
        return videoService.getList(getListDTO);
    }

    @DeleteMapping("/{id}")
    public Result delItem(@PathVariable Integer id) {
        return videoService.delItem(id);
    }
}
