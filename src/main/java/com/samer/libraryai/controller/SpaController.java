package com.samer.libraryai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class SpaController {

    @RequestMapping(value = { "/", "/{path:^(?!api|swagger-ui|v3|h2-console).*$}" })
    public String forward() {
        return "forward:/index.html";
    }
}

/*
@Controller
public class SpaController {

    @RequestMapping(value = { "/", "/{path:[^\\.]*}" })
    public String forward() {
        return "forward:/index.html";
    }
}
 */