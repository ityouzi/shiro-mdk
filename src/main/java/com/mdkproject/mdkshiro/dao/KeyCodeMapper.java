package com.mdkproject.mdkshiro.dao;

import com.mdkproject.mdkshiro.entity.KeyCode;
import java.util.List;

public interface KeyCodeMapper {
    int deleteByPrimaryKey(Integer keyId);

    int insert(KeyCode record);

    KeyCode selectByPrimaryKey(Integer keyId);

    List<KeyCode> selectAll(Integer page1, Integer limit1);

    int updateByPrimaryKey(KeyCode record);

    /**
     * key
     * */
    KeyCode selectBykeyCode(String key);

    /**
     * 激活码列表统计
     * */
    int keyListCount();
}