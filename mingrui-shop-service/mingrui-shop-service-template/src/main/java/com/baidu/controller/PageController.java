package com.baidu.controller;

import com.baidu.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping(value = "item")
public class PageController {

    @Autowired
    private PageService pageService;

    @GetMapping(value = "{spuId}.html")
    public String test(@PathVariable(value = "spuId") Integer spuId, ModelMap modelMap){

        Map<String, Object> map = pageService.getGoodsInfo(spuId);
        modelMap.putAll(map);
        return "item";
    }
}
