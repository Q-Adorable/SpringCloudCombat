package com.example.captcha.controller;

import com.example.captcha.service.CaptchaService;
import com.example.captcha.service.ImageServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private ImageServlet imageServlet;

    @GetMapping("/captchaImgByte")
    public String getImgByte() {
        return captchaService.getCaptchaImgByteStr();
    }

    @GetMapping("/captchaImg")
    public void getImg(HttpServletResponse response) throws IOException {
        captchaService.getCaptchaImg(response);
    }

    @GetMapping("/image")
    public void getDefaultImg(HttpServletResponse response) throws IOException {
        imageServlet.getImage(response);
    }
}
