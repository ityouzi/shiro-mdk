package com.mdkproject.mdkshiro.modules.service.impl;

import com.mdkproject.mdkshiro.dao.*;
import com.mdkproject.mdkshiro.entity.*;
import com.mdkproject.mdkshiro.modules.service.TJProjectService;
import com.mdkproject.mdkshiro.modules.service.model.TjProjectModel;
import com.mdkproject.mdkshiro.utils.MyTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: mdk-shiro->TJProjectServiceImpl
 * @description: 体检项目管理
 * @author: lizhen
 * @create: 2019-07-19 11:27
 **/
@Slf4j
@Service
public class TJProjectServiceImpl implements TJProjectService {

    @Autowired
    private JieLunMapper jieLunMapper;
    @Autowired
    private LabMapper labMapper;
    @Autowired
    private SickNessHistoryMapper sickNessHistoryMapper;
    @Autowired
    private SignMapper signMapper;
    @Autowired
    private XpictureMapper xpictureMapper;



    /**
     * 基础录入添加体检项目
     * @param tjProjectModel
     */
    @Override
    @Transactional
    public boolean addProject(TjProjectModel tjProjectModel) {
        tjProjectModel.setCreateTime(MyTools.getTime());
        try {

            SickNessHistory sickNessHistory = this.convertSicknessHistoryFromModle(tjProjectModel);
            sickNessHistoryMapper.insert(sickNessHistory);

            Sign sign = this.convertSignFromModel(tjProjectModel);
            signMapper.insert(sign);

            Xpicture xpicture = this.convertXpictureFromModel(tjProjectModel);
            xpictureMapper.insert(xpicture);

            Lab lab = this.convertLabFromModel(tjProjectModel);
            labMapper.insert(lab);

            JieLun jieLun = this.convertJieLunFromModel(tjProjectModel);
            jieLunMapper.insert(jieLun);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("添加体检项目异常"+e);
            return false;
        }
    }

    /**
     * 获取当前医院体检项目数据
     * @param hospitalNum
     * @param page
     * @param limit
     */
    @Override
    @Transactional
    public List<TjProjectModel> getDataByHospitalNum(String hospitalNum, int page, int limit) {
        List<TjProjectModel> lists = new ArrayList<>();
        Map<String,Object> data = new HashMap<>();
        data.put("hospitalNum",hospitalNum);
        data.put("page",(page-1)*limit);
        data.put("hospitalNum",limit);
        List<JieLun> list = jieLunMapper.getDataByHospitalNum(data);
        for (int i=0;i<list.size();i++){
            TjProjectModel tjProjectModels = new TjProjectModel();
            JieLun jieLun = list.get(i);
            String personIdcard = jieLun.getPersonIdcard(); //身份证号
            SickNessHistory sickNessHistory = sickNessHistoryMapper.findByIdCardNum(personIdcard);
            Sign sign = signMapper.findByIdCardNum(personIdcard);
            Xpicture xpicture = xpictureMapper.findByIdCardNum(personIdcard);
            Lab lab = labMapper.findByIdCardNum(personIdcard);
            //转换
            TjProjectModel tjProjectModel1 = this.convertModelFromEntity1(sickNessHistory);
            TjProjectModel tjProjectModel2 = this.convertModelFromEntity2(sign);
            TjProjectModel tjProjectModel3 = this.convertModelFromEntity3(xpicture);
            TjProjectModel tjProjectModel4 = this.convertModelFromEntity4(lab);
            TjProjectModel tjProjectModel5 = this.convertModelFromEntity5(jieLun);
            BeanUtils.copyProperties(tjProjectModel1,tjProjectModels);
            BeanUtils.copyProperties(tjProjectModel2,tjProjectModels);
            BeanUtils.copyProperties(tjProjectModel3,tjProjectModels);
            BeanUtils.copyProperties(tjProjectModel4,tjProjectModels);
            BeanUtils.copyProperties(tjProjectModel5,tjProjectModels);
            lists.add(tjProjectModels);
        }
        return lists;
    }

    /**
     * 获取当前医院体检项目数据（统计）
     * @param hospitalNum
     */
    @Override
    public int getDataByHospitalNumCount(String hospitalNum) {
        return jieLunMapper.getDataByHospitalNumCount(hospitalNum);
    }


    /**
     * 模型转换model--->entity
     */
    private SickNessHistory convertSicknessHistoryFromModle(TjProjectModel tjProjectModel){
        SickNessHistory sickNessHistory = new SickNessHistory();
        BeanUtils.copyProperties(tjProjectModel,sickNessHistory);
        sickNessHistory.setPersonIdcard(tjProjectModel.getIdcardNum());
        sickNessHistory.setYuliu1(tjProjectModel.getHospitalNum());
        return sickNessHistory;
    }

    //体征
    private Sign convertSignFromModel(TjProjectModel tjProjectModel){
        Sign sign = new Sign();
        BeanUtils.copyProperties(tjProjectModel,sign);
        sign.setPersonIdcard(tjProjectModel.getIdcardNum());
        sign.setYuliu1(tjProjectModel.getHospitalNum());
        return sign;
    }
    //x线
    private Xpicture convertXpictureFromModel(TjProjectModel tjProjectModel){
        Xpicture xpicture = new Xpicture();
        BeanUtils.copyProperties(tjProjectModel,xpicture);
        xpicture.setPersonIdcard(tjProjectModel.getIdcardNum());
        xpicture.setYuliu1(tjProjectModel.getHospitalNum());
        return xpicture;
    }

    //检验
    private Lab convertLabFromModel(TjProjectModel tjProjectModel){
        Lab lab = new Lab();
        BeanUtils.copyProperties(tjProjectModel,lab);
        lab.setPersonIdcard(tjProjectModel.getIdcardNum());
        lab.setYuliu1(tjProjectModel.getHospitalNum());
        return lab;
    }
    //结论
    private JieLun convertJieLunFromModel(TjProjectModel tjProjectModel){
        JieLun jieLun = new JieLun();
        BeanUtils.copyProperties(tjProjectModel,jieLun);
        tjProjectModel.getStatus();
        jieLun.setPersonIdcard(tjProjectModel.getIdcardNum());
        jieLun.setHearthCardid(tjProjectModel.getHealthNum());
        jieLun.setAge(Integer.valueOf(tjProjectModel.getAge()));
        jieLun.setNumber(tjProjectModel.getNumber());
        jieLun.setYl1(tjProjectModel.getHospitalNum());
        jieLun.setYl2(tjProjectModel.getCreateTime().split(" ")[0]);
        return jieLun;
    }


    /**
     * 模型转换 entity--->model
     */
    private TjProjectModel convertModelFromEntity1(SickNessHistory sickNessHistory){
        TjProjectModel tjProjectModel = new TjProjectModel();
        BeanUtils.copyProperties(sickNessHistory,tjProjectModel);
        return tjProjectModel;
    }

    //体征
    private TjProjectModel convertModelFromEntity2(Sign sign){
        TjProjectModel tjProjectModel = new TjProjectModel();
        BeanUtils.copyProperties(sign,tjProjectModel);
        return tjProjectModel;
    }
    //x线
    private TjProjectModel convertModelFromEntity3(Xpicture xpicture){
        TjProjectModel tjProjectModel = new TjProjectModel();
        BeanUtils.copyProperties(xpicture,tjProjectModel);
        return tjProjectModel;
    }

    //lab
    private TjProjectModel convertModelFromEntity4(Lab lab){
        TjProjectModel tjProjectModel = new TjProjectModel();
        BeanUtils.copyProperties(lab,tjProjectModel);
        return tjProjectModel;
    }
    //结论
    private TjProjectModel convertModelFromEntity5(JieLun jieLun){
        TjProjectModel tjProjectModel = new TjProjectModel();
        BeanUtils.copyProperties(jieLun,tjProjectModel);
        tjProjectModel.setTjTime(jieLun.getYl2());      //体检日期
        tjProjectModel.setHealthNum(jieLun.getHearthCardid());//健康证编号
        return tjProjectModel;
    }

}
