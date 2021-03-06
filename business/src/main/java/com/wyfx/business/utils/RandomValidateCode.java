package com.wyfx.business.utils;

import com.wyfx.business.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;


/**
 * 生成验证码的工具类
 *
 * @author Tao
 */
@Slf4j
public class RandomValidateCode extends BaseController {

    private final Random random = new Random();
    private final String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";//随机生成字符串的取值范围

    private final int width = 80;     //图片宽度
    private final int height = 26;    //图片高度
    private final int StringNum = 4;  //验证码图片中随机产生字符的数量
    private final int lineSize = 40;  //干扰线数量

    public static boolean verify(String code, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String sessionCode = (String) session.getAttribute("sessionCode");
        log.info("验证码,请求：{},真实：{}", code, sessionCode);
        return StringUtils.equalsIgnoreCase(code, sessionCode);
    }

    public static boolean verify(String code, Subject subject) {
        Session session = subject.getSession();
        String sessionCode = (String) session.getAttribute("sessionCode");
        return StringUtils.equalsIgnoreCase(code, sessionCode);
    }

    /**
     * 获取随机字符,并返回字符的String格式
     *
     * @param index (指定位置)
     * @return
     */
    public String getRandomChar(int index) {
        //获取指定位置index的字符，并转换成字符串表示形式
        return String.valueOf(randString.charAt(index));
    }

    /**
     * 获得字体
     *
     * @return
     */
    private Font getFont() {
        return new Font("Fixedsys", Font.CENTER_BASELINE, 18);  //名称、样式、磅值
    }

    /**
     * 获得颜色
     *
     * @param
     * @param
     * @return
     */
    private Color getRandColor(int frontColor, int backColor) {
        if (frontColor > 255)
            frontColor = 255;
        if (backColor > 255)
            backColor = 255;

        int red = frontColor + random.nextInt(backColor - frontColor - 16);
        int green = frontColor + random.nextInt(backColor - frontColor - 14);
        int blue = frontColor + random.nextInt(backColor - frontColor - 18);
        return new Color(red, green, blue);
    }

    /**
     * 绘制字符串,返回绘制的字符串
     *
     * @param g
     * @param randomString
     * @param i
     * @return
     */
    private String drawString(Graphics g, String randomString, int i) {
        g.setFont(getFont());   //设置字体
        g.setColor(new Color(random.nextInt(101), random.nextInt(111), random.nextInt(121)));//设置颜色
        String randChar = String.valueOf(getRandomChar(random.nextInt(randString.length())));
        randomString += randChar;   //组装
        g.translate(random.nextInt(3), random.nextInt(3));
        g.drawString(randChar, 13 * i, 16);
        return randomString;
    }

    /**
     * 绘制干扰线
     *
     * @param g
     */
    private void drawLine(Graphics g) {
        //起点(x,y)  偏移量x1、y1
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        g.drawLine(x, y, x + xl, y + yl);
    }

    /**
     * 生成随机图片
     *
     * @param request
     * @param response
     */
    public void getRandomCode(HttpServletRequest request, HttpServletResponse response) {

        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();// 获得BufferedImage对象的Graphics对象
        g.fillRect(0, 0, width, height);//填充矩形
        g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));//设置字体
        g.setColor(getRandColor(110, 133));//设置颜色
        //绘制干扰线
        for (int i = 0; i <= lineSize; i++) {
            drawLine(g);
        }
        //绘制字符
        String randomString = "";
        for (int i = 1; i <= StringNum; i++) {
            randomString = drawString(g, randomString, i);
        }

        //将生成的验证码放入session
        HttpSession session = request.getSession();
        /*Session shiroSession= getSubject().getSession();
        if(shiroSession!=null){
            shiroSession.setAttribute("sessionCode",randomString);
        }*/
        session.setAttribute("sessionCode", randomString);
        System.out.println("生成的验证码为：" + randomString);

        g.dispose();//释放绘图资源
        try {
            ByteArrayOutputStream tmp = new ByteArrayOutputStream();
            ImageIO.write(image, "png", tmp);//将会值得图片输出到流
            tmp.close();
            Integer contentLength = tmp.size();//内容长度
            response.setHeader("content-length", contentLength + "");
            response.getOutputStream().write(tmp.toByteArray());//通过response输出图片
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}