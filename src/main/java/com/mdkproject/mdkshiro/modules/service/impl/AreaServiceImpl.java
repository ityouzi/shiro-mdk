package com.mdkproject.mdkshiro.modules.service.impl;

import com.mdkproject.mdkshiro.dao.AreaMapper;
import com.mdkproject.mdkshiro.entity.Area;
import com.mdkproject.mdkshiro.modules.service.AreaService;
import com.mdkproject.mdkshiro.utils.MyTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: mdk-shiro->AreaServiceImpl
 * @description: 区域管理
 * @author: lizhen
 * @create: 2019-07-12 09:54
 **/
@Slf4j
@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaMapper areaMapper;


    /**
     * 区域列表
     * */
    @Override
    public List<Area> areaList(int page, int limit) {
        Map<String,Object> date = new HashMap<>();
        date.put("page",(page-1)*limit);
        date.put("limit",limit);
        List<Area> list = areaMapper.selectAll(date);
        return list;
    }

    /**
     * 区域添加
     * */
    @Override
    public boolean areaAdd(Area area) {
        try {
            area.setCreateTime(MyTools.getTime());
            area.setUpdateTime(MyTools.getTime());
            areaMapper.insert(area);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 区域删除
     * */
    @Override
    public boolean areaDelete(Integer areaId) {
        try {
            areaMapper.deleteByPrimaryKey(areaId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 区域修改
     * */
    @Override
    public boolean areaUpdate(Area area) {
        try {
            area.setUpdateTime(MyTools.getTime());
            areaMapper.updateByPrimaryKey(area);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 区域详情
     * */
    @Override
    public Area areaGet(Integer areaId) {
        Area area = areaMapper.selectByPrimaryKey(areaId);
        return area;
    }

    /**
     * 通过医院编号获取区域简称
     * @param areaNum
     */
    @Override
    public Area selectByAreaNum(String areaNum) {
        Area area = areaMapper.selectByAreaNum(areaNum);
        if (area == null){
            return null;
        }
        return area;
    }
}
