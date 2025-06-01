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

//    @CrossOrigin(origins = "*")
//    @PostMapping("/upload-callback")
//    public Result uploadCallback(
//            @RequestParam("fileName") String fileName,
//            @RequestParam("fileUuid") String fileUuid,
//            @RequestParam("fileExtension") String fileExtension,
//            @RequestParam("filePath") String filePath
//    ) {
//        return ossService.registerUpload(new UploadDTO(fileName, fileUuid, fileExtension, filePath));
//    }

    @PostMapping("/upload-callback")
    public Result uploadCallback(@RequestBody UploadDTO uploadDTO) {
        return ossService.registerUpload(uploadDTO);
    }
}