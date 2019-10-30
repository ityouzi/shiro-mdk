package com.mdkproject.mdkshiro.entity;

import lombok.Data;

/**
 * @Auther: Liberal-World
 * @Date: 2019-06-17 11:57
 * @Description:
 * @Version 1.0
 */
@Data
public class SysPermissionVo {

    private Integer id;//主键.
    private String text;//名称.
    private String parent;//资源路径.
    private String state;//状态
}
