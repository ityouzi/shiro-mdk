package com.mdkproject.mdkshiro.dao;

import com.mdkproject.mdkshiro.entity.Sign;
import java.util.List;

public interface SignMapper {
    int deleteByPrimaryKey(Integer signId);

    int insert(Sign record);

    Sign selectByPrimaryKey(Integer signId);

    List<Sign> selectAll();

    int updateByPrimaryKey(Sign record);

    /**
     * 身份证号查询
     * */
    Sign findByIdCardNum(String personIdcard);
}