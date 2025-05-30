package com.vp.vpbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vp.vpbackend.POJO.DO.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
}
