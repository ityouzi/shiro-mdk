package com.mdkproject.mdkshiro.modules.service;


import com.mdkproject.mdkshiro.entity.TiJianInfo;

import java.util.List;

public interface CountDataService {

    /**
     * 区域+医院+时间
     * */
    List<TiJianInfo> selectByAreaNumAndHospitalNum(String hospitalNum, String dates, int page, int limit);

    /**
     * 区域+医院+时间(统计)
     * */
    int selectHospitalNumCount(String hospitalNum, String dates);

}
