package com.fanruan.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * 设置支持jsonp
 * basePackages为支持jsonp的controller范围
 * Created by boris on 2017/1/16.
 */
@ControllerAdvice(basePackages = "com.fanruan.controllers")
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
    public JsonpAdvice(){
        super("callback");  //queryParamNames eg. callback,jsonp
    }
}
