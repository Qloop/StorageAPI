package com.fanruan.service;

import com.fanruan.conf.AppConfig;
import com.fanruan.utils.JsonParserUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
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

    @Autowired
    private SessionFactory sessionFactory;

    public void singleFeatureHandler(String jsonStr) {
        List<String> dataSummary = JsonParserUtils.parserWithKey(jsonStr, keyArray);
        String dataTableName = dataSummary.get(0);
        System.out.println("dataTableName is: " + dataTableName);
        System.out.println("json is: " + dataSummary.get(1));
        @SuppressWarnings("unchecked")
        Map<String, String> colDetail = (Map<String, String>) JsonParserUtils.parserWithoutKey(dataSummary.get(1), false);

//        dataBaseHandler(dataTableName, colDetail);
    }

    public void multipleFeatureHandler(String jsonStr) {
        List<String> dataSummary = JsonParserUtils.parserWithKey(jsonStr, mulKeyArray);
        String dataTableName = dataSummary.get(0);
        System.out.println("dataTableName is: " + dataTableName);
        System.out.println("json is: " + dataSummary.get(1));
        int rowCount = dataSummary.get(1).length() - dataSummary.get(1).replace("}", "").length();
        @SuppressWarnings("unchecked")
        Map<String, List<String>> colDetail = (Map<String, List<String>>) JsonParserUtils.parserWithoutKey(dataSummary.get(1), true);
        dataBaseMultiHandler(dataTableName, colDetail,rowCount);
    }

    private void dataBaseHandler(String tableName, Map<String, String> colDetail) {
//        Session currentSession = sessionFactory.getCurrentSession();
        Session session = sessionFactory.openSession();
        session.doWork(connection -> {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getTables(null, null, tableName, new String[]{"TABLE"});
            if (rs.next()) {
                System.out.println(tableName + ": 表存在");
//                INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,....)
                StringBuilder sqlBuilder = new StringBuilder("INSERT INTO `" + tableName + "` (");
                Set<String> colNameSet = colDetail.keySet();
                System.out.println("set size: " + colNameSet.size());
                for (String colName : colNameSet) {
                    System.out.println("colName is: " + colName);
                    System.out.println("value is: " + colDetail.get(colName));
                    sqlBuilder.append(colName).append(",");
                }
                sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
                sqlBuilder.append(") VALUES (");
                for (String colName : colNameSet) {
                    System.out.println("colName is: " + colName);
                    System.out.println("value is: " + colDetail.get(colName));
                    sqlBuilder.append("\"").append(colDetail.get(colName)).append("\"").append(",");
                }
                sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
                sqlBuilder.append(")");
                System.out.println("sql is: " + sqlBuilder.toString());
                connection.prepareStatement(sqlBuilder.toString()).execute();
            } else {
                StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE `" + tableName + "` (  `id` INT(11) NOT NULL AUTO_INCREMENT, ");
                Set<String> colNameSet = colDetail.keySet();
                System.out.println("set size: " + colNameSet.size());
                for (String colName : colNameSet) {
                    System.out.println("colName is: " + colName);
                    System.out.println("value is: " + colDetail.get(colName));
                    sqlBuilder.append("`").append(colName).append("` VARCHAR(255) DEFAULT NULL, ");
                }
                sqlBuilder.append("PRIMARY KEY  (`id`)) ENGINE=INNODB DEFAULT CHARSET=utf8;");
//                String sql = "CREATE TABLE `" + tableName + "` (  `id` INT(11) NOT NULL AUTO_INCREMENT,  `appId` BIGINT(20) DEFAULT NULL,  `userAttr` VARCHAR(255) DEFAULT NULL,  `userCA` VARCHAR(255) DEFAULT NULL,  `userIP` VARCHAR(255) DEFAULT NULL,  `userMAC` VARCHAR(255) DEFAULT NULL,  `visitDate` VARCHAR(255) DEFAULT NULL,  `visitSource` VARCHAR(255) DEFAULT NULL,  PRIMARY KEY  (`id`)) ENGINE=INNODB DEFAULT CHARSET=utf8;";
                connection.prepareStatement(sqlBuilder.toString()).execute();
                dataBaseHandler(tableName, colDetail);//创建完新的表后插入值
            }

        });

    }

    private void dataBaseMultiHandler(String tableName, Map<String, List<String>> colDetail, int rowCount) {
//        Session currentSession = sessionFactory.getCurrentSession();
        Session session = sessionFactory.openSession();
        session.doWork(connection -> {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getTables(null, null, tableName, new String[]{"TABLE"});
            if (rs.next()) {
                System.out.println(tableName + ": 表存在");
//                INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,...),(值12，值22,...),(...)
                StringBuilder sqlBuilder = new StringBuilder("INSERT INTO `" + tableName + "` (");
                Set<String> colNameSet = colDetail.keySet();
                System.out.println("set size: " + colNameSet.size());
                for (String colName : colNameSet) {
                    System.out.println("colName is: " + colName);
                    System.out.println("value is: " + colDetail.get(colName));
                    sqlBuilder.append(colName).append(",");
                }
                sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
                sqlBuilder.append(") VALUES (");
                int currentRow = 0;
                while (currentRow != rowCount) {
                    System.out.println("row: " + rowCount);
                    for (String colName : colNameSet) {
                        System.out.println("colName is: " + colName);
                        System.out.println("value is: " + colDetail.get(colName));
                        List<String> colValue = colDetail.get(colName);
                        sqlBuilder.append("\"").append(colDetail.get(colName).get(currentRow)).append("\"").append(",");
                    }
                    sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
                    sqlBuilder.append("),(");
                    currentRow++;
                }
                System.out.println("sql is: " + sqlBuilder.substring(0, sqlBuilder.length() - 2));
                connection.prepareStatement(sqlBuilder.substring(0, sqlBuilder.length() - 2)).execute();
            } else {
                StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE `" + tableName + "` (  `id` INT(11) NOT NULL AUTO_INCREMENT, ");
                Set<String> colNameSet = colDetail.keySet();
                System.out.println("set size: " + colNameSet.size());
                for (String colName : colNameSet) {
                    System.out.println("colName is: " + colName);
                    System.out.println("value is: " + colDetail.get(colName));
                    sqlBuilder.append("`").append(colName).append("` VARCHAR(255) DEFAULT NULL, ");
                }
                sqlBuilder.append("PRIMARY KEY  (`id`)) ENGINE=INNODB DEFAULT CHARSET=utf8;");
                connection.prepareStatement(sqlBuilder.toString()).execute();
                dataBaseMultiHandler(tableName, colDetail, rowCount);//创建完新的表后插入值
            }

        });

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
