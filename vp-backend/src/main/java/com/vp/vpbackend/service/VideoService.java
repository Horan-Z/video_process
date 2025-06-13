package com.vp.vpbackend.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vp.vpbackend.POJO.DO.FileDO;
import com.vp.vpbackend.POJO.DTO.GetListDTO;
import com.vp.vpbackend.POJO.Result;
import com.vp.vpbackend.POJO.VO.FileVO;
import com.vp.vpbackend.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final FileMapper fileMapper;

    public Result getList(GetListDTO getListDTO) {
        LambdaQueryWrapper<FileDO> lqw = new LambdaQueryWrapper<>();
        switch (getListDTO.getSortType()) {
            case NAME_ASC -> lqw.orderByAsc(FileDO::getFileName);
            case NAME_DESC -> lqw.orderByDesc(FileDO::getFileName);
            case CREATED_TIME_ASC -> lqw.orderByAsc(FileDO::getCreateTime);
            case CREATED_TIME_DESC -> lqw.orderByDesc(FileDO::getCreateTime);
        }

        Page<FileDO> page = new Page<>(getListDTO.getPageNum(), getListDTO.getPageSize());

        fileMapper.selectPage(page, lqw);

        return new Result(200, "success", new FileVO(fileMapper.selectCount(new LambdaQueryWrapper<>()), page));
    }

    public Result delItem(Integer id) {
        // 查询文件信息并验证用户权限
        LambdaQueryWrapper<FileDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FileDO::getId, id)
                .eq(FileDO::getBelongTo, StpUtil.getLoginIdAsString());

        FileDO file = fileMapper.selectOne(queryWrapper);

        if (file == null) {
            return new Result(400, "文件不存在或无权删除", null);
        } else {
            fileMapper.deleteById(id);
            // TODO OSS文件删除
            return new Result(200, "success", null);
        }
    }
}
