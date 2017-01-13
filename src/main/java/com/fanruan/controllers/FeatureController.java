package com.fanruan.controllers;

import com.fanruan.conf.AppConfig;
import com.fanruan.service.FeatureService;
import com.fanruan.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Qloop on 2017/1/11.
 */
@RestController
@RequestMapping("/monitor/feature")
public class FeatureController {

    @Autowired
    private FeatureService featureService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String singleMsgReceiver(@RequestParam String token, @RequestParam String feature) {
        if (DateUtils.getMD5Data().equalsIgnoreCase(token)) {
            featureService.singleFeatureHandler(feature);
            return "单行信息保存.token is: " + token + " And feature is: " + feature;
        } else {
            return AppConfig.ILLEGAL_ACCESS;
        }
    }

    @RequestMapping(value = "/multiple", method = RequestMethod.POST)
    public String multipleMsgReceiver(@RequestParam String token, @RequestParam String feature) {
        if (DateUtils.getMD5Data().equalsIgnoreCase(token)) {
            featureService.multipleFeatureHandler(feature);
            return "多行信息保存.token is: " + token + " and feature is: " + feature;
        } else {
            return AppConfig.ILLEGAL_ACCESS;
        }
    }

    @RequestMapping("/file")
    @ResponseBody
    public String fileReceiver(@RequestParam("file") MultipartFile file) {
        return featureService.save(file);
    }

    @RequestMapping("/file/batch")
    @ResponseBody
    public String fileReceiver(HttpServletRequest request) {
        List<MultipartFile> fileList = ((MultipartHttpServletRequest) request).getFiles("file");
        return featureService.saveAll(fileList);
    }
}
