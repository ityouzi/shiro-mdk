package com.mdkproject.mdkshiro.dao;

import com.mdkproject.mdkshiro.entity.Area;
import java.util.List;
import java.util.Map;

public interface AreaMapper {
    int deleteByPrimaryKey(Integer areaId);

    int insert(Area record);

    Area selectByPrimaryKey(Integer areaId);

    /**
     * 区域列表
     * */
    List<Area> selectAll(Map<String, Object> date);

    int updateByPrimaryKey(Area record);

    /**
     * 获取区域信息
     * */
    Area selectByAreaNum(String areaNum);

}