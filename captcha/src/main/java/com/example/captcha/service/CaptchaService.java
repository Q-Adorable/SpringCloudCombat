package com.example.captcha.service;

import com.example.captcha.exception.CustomException;
import com.google.code.kaptcha.Producer;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class CaptchaService {
    @Autowired
    private Producer captchaProducer;

    public String getCaptchaImgByteStr() {
        String imgStr = null;
        String code = captchaProducer.createText();
        BufferedImage bi = captchaProducer.createImage(code);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "jpg", bos);
            byte[] imageBytes = bos.toByteArray();
            imgStr = "data:image/jpeg;base64," + new String(Base64.encodeBase64(imageBytes));
            bos.flush();
        } catch (IOException e) {
            throw new CustomException(e.getMessage());
        }
        return imgStr;
    }

    public void getCaptchaImg(HttpServletResponse response) {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String capText = captchaProducer.createText();

        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out;
        try {
            out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new CustomException(e.getMessage());
        }
    }
}
