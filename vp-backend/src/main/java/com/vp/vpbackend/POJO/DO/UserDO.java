package com.vp.vpbackend.POJO.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("user")
@Data
public class UserDO {
    @TableId(type = IdType.AUTO)
    private int id;

    private String uuid;

    private String username;

    private String password;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
