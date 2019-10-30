package com.mdkproject.mdkshiro.modules.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.mdkproject.mdkshiro.entity.ExcelModel;
import com.mdkproject.mdkshiro.modules.service.TiJianInfoService;
import com.mdkproject.mdkshiro.response.CommonReturnType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @program: mdk-shiro->ExcelController
 * @description: Excel导入导出
 * @author: lizhen
 * @create: 2019-07-31 12:10
 **/
@RestController
@RequestMapping("export")
@Slf4j
public class ExcelController {

    @Autowired
    private TiJianInfoService tiJianInfoService;


    /**
     * @auther: lizhen
     * @date: 2019/7/31 12:29
     * 功能描述: 导入
     */
    @RequestMapping(value = "importExcel")
    public CommonReturnType importExcel(MultipartFile file){
        ImportParams importParams = new ImportParams();
        try {
            ExcelImportResult<ExcelModel> result = ExcelImportUtil.importExcelMore(file.getInputStream(), ExcelModel.class, importParams);
            List<ExcelModel> successList = result.getList();
            if (tiJianInfoService.importExcel(successList)){
                log.info("从Excel导入数据一共 {} 行", successList.size());
                return CommonReturnType.createCommonReturnType("导入成功", "200");
            }
            return CommonReturnType.createCommonReturnType("导入失败","100");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonReturnType.createCommonReturnType("导入异常","500");
        }
    }





}
