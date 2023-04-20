package com.hb.blog.controller;

import com.hb.blog.util.StringLowerCaseEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
@ControllerAdvice
public class AllControllerAdvice {
    /*@InitBinder
    public void initBinder( WebDataBinder dataBinder )
    {
        StringLowerCaseEditor lowerCaseEditor = new StringLowerCaseEditor();
        dataBinder.registerCustomEditor( String.class, lowerCaseEditor );
    }*/
}
