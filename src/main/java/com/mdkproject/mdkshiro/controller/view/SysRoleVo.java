package com.mdkproject.mdkshiro.controller.view;

import lombok.Data;

/**
 * @Auther: Liberal-World
 * @Date: 2019-07-03 14:43
 * @Description:
 * @Version 1.0
 */
@Data
public class SysRoleVo {
    private Integer id;         //id
    private String pid;         //父级ID
    private String name;        //名称
    private String status;      //是否选择,0没有1有

}
