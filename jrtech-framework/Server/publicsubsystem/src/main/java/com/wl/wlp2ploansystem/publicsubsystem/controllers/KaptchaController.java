package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.security.AuthoritiesConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@RequestMapping(value = "/kaptcha")
@Controller
public class KaptchaController {

	@Autowired  
	private DefaultKaptcha defaultKaptcha; 
	@GetMapping(value = "/get")
    @Log(disabled = true)
    public void getKaptchaImage(HttpServletRequest request,  
            HttpServletResponse response) throws Exception {  
		
		 byte[] captchaChallengeAsJpeg = null;    
         ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();    
        
         //生产验证码字符串并保存到session中  
         String createText = defaultKaptcha.createText();  
         request.getSession().setAttribute(AuthoritiesConstants.KAPTCHA_KEY, createText);
         //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中  
         BufferedImage challenge = defaultKaptcha.createImage(createText);  
         ImageIO.write(challenge, "jpg", jpegOutputStream);   
         
         //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组  
         captchaChallengeAsJpeg = jpegOutputStream.toByteArray();    
         response.setHeader("Cache-Control", "no-store");    
         response.setHeader("Pragma", "no-cache");    
         response.setDateHeader("Expires", 0);    
         response.setContentType("image/jpeg");    
         ServletOutputStream responseOutputStream =    
        		 response.getOutputStream();    
         responseOutputStream.write(captchaChallengeAsJpeg);    
         responseOutputStream.flush();    
         responseOutputStream.close();    
    }  
}
