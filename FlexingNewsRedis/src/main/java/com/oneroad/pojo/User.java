package com.oneroad.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @TableName news_user
 */
@Data
@Schema(description = "用户实体类")
public class User implements Serializable {
    @TableId
    private Integer uid;

    private String username;

    private String userPwd;

    private String nickName;

    @Version
    private Integer version;

    private Integer isDeleted;

    private static final long serialVersionUID = 1L;
}