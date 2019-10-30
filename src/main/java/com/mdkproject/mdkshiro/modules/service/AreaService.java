package com.mdkproject.mdkshiro.modules.service;

import com.mdkproject.mdkshiro.entity.Area;

import java.util.List;

/**
 * @program: mdk-shiro->AreaService
 * @description: 区域管理
 * @author: lizhen
 * @create: 2019-07-12 09:49
 **/
public interface AreaService {

    /**
     * 区域列表
     * */
    List<Area> areaList(int page, int limit);

    /**
     * 区域添加
     * */
    boolean areaAdd(Area area);

    /**
     * 区域删除
     * */
    boolean areaDelete(Integer areaId);

    /**
     * 区域修改
     * */
    boolean areaUpdate(Area area);

    /**
     * 区域详情
     * */
    Area areaGet(Integer areaId);

    /**
     * 通过医院编号获取区域简称
     * */
    Area selectByAreaNum(String areaNum);
}
