package com.fanruan.service;

import com.fanruan.conf.AppConfig;
import com.fanruan.utils.JsonParserUtils;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Qloop on 2017/1/11.
 */
@Service
public class FeatureService {

    private String[] keyArray = new String[]{"location", "value"};
    private String[] mulKeyArray = new String[]{"location", "values"};


    public void singleFeatureHandler(String jsonStr) {
        List<String> dataSummary = JsonParserUtils.parserWithKey(jsonStr, keyArray);
        String dataTableName = dataSummary.get(0);
        System.out.println("dataTableName is: " + dataTableName);
        System.out.println("json is: " + dataSummary.get(1));

        Map<String, String> colDetail = JsonParserUtils.parserWithoutKey(dataSummary.get(1), false);
        Set<String> colNameSet = colDetail.keySet();
        System.out.println("set size: " + colNameSet.size());
        for (String colName : colNameSet) {
            System.out.println("colName is: " + colName);
            System.out.println("value is: " + colDetail.get(colName));
        }
    }

    public void multipleFeatureHandler(String jsonStr) {
        List<String> dataSummary = JsonParserUtils.parserWithKey(jsonStr, mulKeyArray);
        String dataTableName = dataSummary.get(0);
        System.out.println("dataTableName is: " + dataTableName);
        System.out.println("json is: " + dataSummary.get(1));
        Map<String, String> colDetail = JsonParserUtils.parserWithoutKey(dataSummary.get(1), true);
        Set<String> colNameSet = colDetail.keySet();
        System.out.println("set size: " + colNameSet.size());
        for (String colName : colNameSet) {
            System.out.println("colName is: " + colName);
            System.out.println("value is: " + colDetail.get(colName));
        }
    }


    private void dataBaseHandle(String tableName){
        Connection conn = null;
    }

    /**
     * 单个文件上传保存
     */
    public String save(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String saveFileName = AppConfig.FILE_SAVE_PATH + file.getOriginalFilename();
                byte[] bytes = file.getBytes();
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                        new FileOutputStream(new File(saveFileName)));
                bufferedOutputStream.write(bytes);
                bufferedOutputStream.close();
                return AppConfig.FILE_SAVE_SUCCESS;
            } catch (IOException e) {
                e.printStackTrace();
                return AppConfig.FILE_SAVE_FAILED;
            }
        } else {
            return AppConfig.FILE_EMPTY;
        }
    }

    /**
     * 多文件上传保存(目前的逻辑是文件list中一个保存出错就终止保存,返回出错提示)
     */
    public String saveAll(List<MultipartFile> fileList) {
        for (MultipartFile file : fileList) {
            if (AppConfig.FILE_SAVE_FAILED.equals(save(file))) {
                return AppConfig.FILE_SAVE_FAILED;
            }
        }
        return AppConfig.FILE_SAVE_SUCCESS;
    }
}
