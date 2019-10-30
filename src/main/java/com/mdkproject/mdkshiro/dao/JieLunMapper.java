package com.mdkproject.mdkshiro.dao;

import com.mdkproject.mdkshiro.entity.JieLun;
import java.util.List;
import java.util.Map;

public interface JieLunMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JieLun record);

    JieLun selectByPrimaryKey(Integer id);

    List<JieLun> selectAll();

    int updateByPrimaryKey(JieLun record);

    /**
     * 获取当前医院体检项目数据（统计）
     * */
    int getDataByHospitalNumCount(String hospitalNum);

    /**
     * 获取当前医院体检项目数据
     * */
    List<JieLun> getDataByHospitalNum(Map<String, Object> data);
}