package com.mdkproject.mdkshiro.dao;

import com.mdkproject.mdkshiro.entity.SickNessHistory;
import java.util.List;

public interface SickNessHistoryMapper {
    int deleteByPrimaryKey(Integer sicknessId);

    int insert(SickNessHistory record);

    SickNessHistory selectByPrimaryKey(Integer sicknessId);

    List<SickNessHistory> selectAll();

    int updateByPrimaryKey(SickNessHistory record);

    /**
     * 身份证号查询
     * */
    SickNessHistory findByIdCardNum(String personIdcard);
}