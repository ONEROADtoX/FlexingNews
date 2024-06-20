package com.oneroad.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @TableName news_type
 */
@Data
@Schema(description = "头条类型实体类")
public class Type implements Serializable {
    @TableId
    private Integer tid;

    private String tname;

    @Version
    private Integer version;

    private Integer isDeleted;

    private static final long serialVersionUID = 1L;
}