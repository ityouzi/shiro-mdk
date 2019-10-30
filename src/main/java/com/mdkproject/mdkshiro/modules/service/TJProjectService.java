package com.mdkproject.mdkshiro.modules.service;

import com.mdkproject.mdkshiro.modules.service.model.TjProjectModel;

import java.util.List;

public interface TJProjectService {

    /**
     * 基础录入添加体检项目
     * */
    boolean addProject(TjProjectModel tjProjectModel);

    /**
     * 获取当前医院体检项目数据
     * */
    List<TjProjectModel> getDataByHospitalNum(String hospitalNum, int page, int limit);

    /**
     * 获取当前医院体检项目数据（统计）
     * */
    int getDataByHospitalNumCount(String hospitalNum);
}
