package com.example.captcha.service;

import com.example.captcha.exception.CustomException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class ImageServlet {

    private static final int width = 110;
    private static final int height = 40;
    private static final int amount = 120;
    private static final int fontSize = 30;
    private static final int ovalWidth = 2;
    private static final int ovalHeight = 1;

    public void getImage(HttpServletResponse response) {
        response.setContentType("image/jpeg");
        //设置浏览器不要缓存此图片
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");

        //创建内存图像并获得其图形上下文
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();

        getGraphics(graphics);

        //将图像输出到客户端
        ServletOutputStream out;
        try {
            out = response.getOutputStream();
            ImageIO.write(image, "jpg", out);
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new CustomException(e.getMessage());
        }
    }

    //获取图形验证码
    public void getGraphics(Graphics graphics) {

        setImgBackground(graphics);

        setPoint(graphics);

        setImgCode(graphics);
    }

    //设置图片背景
    public void setImgBackground(Graphics graphics) {
        graphics.setColor(new Color(245, 245, 220));
        graphics.fillRect(0, 0, width, height);
    }

    //随机产生amount个干扰点
    public Graphics setPoint(Graphics graphics) {
        for (int i = 0; i < amount; i++) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);
            int red = (int) (Math.random() * 255);
            int green = (int) (Math.random() * 255);
            int blue = (int) (Math.random() * 255);
            graphics.setColor(new Color(red, green, blue));
            graphics.drawOval(x, y, ovalWidth, ovalHeight);
        }
        return graphics;
    }

    //设置验证码样式
    public Graphics setImgCode(Graphics graphics) {
        //设置验证码字体属性
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("宋体", Font.ITALIC | Font.BOLD, fontSize));

        char[] rands = getCode();

        //在不同的高度上输出验证码的不同字符
        graphics.drawString("" + rands[0], 12, 24);
        graphics.drawString("" + rands[1], 33, 30);
        graphics.drawString("" + rands[2], 52, 38);
        graphics.drawString("" + rands[3], 73, 26);
        graphics.dispose();
        return graphics;
    }

    //产生4位随机验证码
    public char[] getCode() {
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] rands = new char[4];
        for (int i = 0; i < 4; i++) {
            int rand = (int) (Math.random() * 62);
            rands[i] = chars.charAt(rand);
        }
        return rands;
    }

}
