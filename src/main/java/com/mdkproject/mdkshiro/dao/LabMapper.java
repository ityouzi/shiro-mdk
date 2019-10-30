package com.mdkproject.mdkshiro.dao;

import com.mdkproject.mdkshiro.entity.Lab;
import java.util.List;

public interface LabMapper {
    int deleteByPrimaryKey(Integer labId);

    int insert(Lab record);

    Lab selectByPrimaryKey(Integer labId);

    List<Lab> selectAll();

    int updateByPrimaryKey(Lab record);

    /**
     * 身份证号查询
     * */
    Lab findByIdCardNum(String personIdcard);
}