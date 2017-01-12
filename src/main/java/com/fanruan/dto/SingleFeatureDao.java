package com.fanruan.dto;

/**
 * Created by Qloop on 2017/1/11.
 */
public class SingleFeatureDao {

    /**
     * location : 数据库表名
     * value : {"列名2":"值2","列名1":"值1"}
     */
    private String location;
    private ValueEntity value;

    public void setLocation(String location) {
        this.location = location;
    }

    public void setValue(ValueEntity value) {
        this.value = value;
    }

    public String getLocation() {
        return location;
    }

    public ValueEntity getValue() {
        return value;
    }

    public class ValueEntity {
        /**
         * 列名2 : 值2
         * 列名1 : 值1
         */
        private String 列名2;
        private String 列名1;

        public void set列名2(String 列名2) {
            this.列名2 = 列名2;
        }

        public void set列名1(String 列名1) {
            this.列名1 = 列名1;
        }

        public String get列名2() {
            return 列名2;
        }

        public String get列名1() {
            return 列名1;
        }
    }
}
