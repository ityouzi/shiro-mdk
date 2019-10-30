package com.mdkproject.mdkshiro.dao;

import com.mdkproject.mdkshiro.entity.Xpicture;
import java.util.List;

public interface XpictureMapper {
    int deleteByPrimaryKey(Integer xId);

    int insert(Xpicture record);

    Xpicture selectByPrimaryKey(Integer xId);

    List<Xpicture> selectAll();

    int updateByPrimaryKey(Xpicture record);

    /**
     * 身份证号查询
     * */
    Xpicture findByIdCardNum(String personIdcard);
}