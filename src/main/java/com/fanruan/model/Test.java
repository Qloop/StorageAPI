package com.fanruan.model;


import javax.persistence.*;

/**
 * Created by Qloop on 2017/1/11.
 */
@Entity
@Table(name = "t_test")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String testCol1;
    private String testCol2;
    private String testCol3;

    public Test() {
    }

    public Test(String testCol1, String testCol2, String testCol3) {
        this.testCol1 = testCol1;
        this.testCol2 = testCol2;
        this.testCol3 = testCol3;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTestCol1(String testCol1) {
        this.testCol1 = testCol1;
    }

    public void setTestCol2(String testCol2) {
        this.testCol2 = testCol2;
    }

    public void setTestCol3(String testCol3) {
        this.testCol3 = testCol3;
    }

    public long getId() {
        return id;
    }

    public String getTestCol1() {
        return testCol1;
    }

    public String getTestCol2() {
        return testCol2;
    }

    public String getTestCol3() {
        return testCol3;
    }
}
