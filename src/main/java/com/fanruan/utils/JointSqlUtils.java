package com.fanruan.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 拼接sql……
 * Created by Qloop on 2017/1/17.
 */
public class JointSqlUtils {

    /**
     * 插入单行值
     * @param tableName 表名
     * @param colDetail 列-值
     */
    public static String insert(String tableName, Map<String, String> colDetail) {
        //INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,....)
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO `" + tableName + "` (");
        Set<String> colNameSet = colDetail.keySet();
        System.out.println("set size: " + colNameSet.size());
        for (String colName : colNameSet) {
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
        return sqlBuilder.toString();
    }

    public static String insertMultiLines(String tableName, Map<String,List<String>> colDetail,int rowCount){
        //INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,...),(值12，值22,...),(...)
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
//        System.out.println("sql is: " + sqlBuilder.substring(0, sqlBuilder.length() - 2));
        return sqlBuilder.substring(0, sqlBuilder.length() - 2);
    }

    /**
     * 新建数据表
     * @param tableName 表名
     * @param colNameSet 包含列名
     */
    public static String create(String tableName, Set<String> colNameSet) {
        StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE `" + tableName + "` (  `id` INT(11) NOT NULL AUTO_INCREMENT, ");
        for (String colName : colNameSet) {
            sqlBuilder.append("`").append(colName).append("` VARCHAR(255) DEFAULT NULL, ");
        }
        sqlBuilder.append("PRIMARY KEY  (`id`)) ENGINE=INNODB DEFAULT CHARSET=utf8;");
        return sqlBuilder.toString();
    }
}
