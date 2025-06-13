package com.vp.vpbackend.POJO.VO;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vp.vpbackend.POJO.DO.FileDO;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FileVO {
    private Long count;
    private Page<FileDO> page;
}
