package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by shenguoliang on 2019/3/14 0014.
 */
@RestController
public class MyDemoIndex {

    @RequestMapping(value = "/index")
    @ResponseBody
    public String myindex(@RequestParam Map<String,String> map, HttpServletRequest request){
        String name = map.get("name");
        return "Hellow !  " + name;
    }
}
