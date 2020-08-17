package com.dreamlabs.mail;

import java.util.Properties;

import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailTest {
	@Test
	public void send() {
		String host = "smtp.163.com";
		Integer port = 465;
		String userName = "dreamlabs@163.com";
		String password = "*******";
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(host);// 链接服务器
		javaMailSender.setPort(port);
		javaMailSender.setUsername(userName);// 账号
		javaMailSender.setPassword(password);// 密码
		javaMailSender.setDefaultEncoding("UTF-8");

		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth", "true");// 开启认证
		properties.setProperty("mail.smtp.starttls.enable", "true");// 开启SSL
		properties.setProperty("mail.debug", "true");// 启用调试
		properties.setProperty("mail.smtp.timeout", "30000");// 设置链接超时
		properties.setProperty("mail.smtp.port", Integer.toString(port));// 设置端口
		properties.setProperty("mail.smtp.socketFactory.port", Integer.toString(port));// 设置ssl端口
		properties.setProperty("mail.smtp.socketFactory.fallback", "false");
		properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		javaMailSender.setJavaMailProperties(properties);

		log.info("===================开始发送异常提醒邮件================");
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(userName);
		mailMessage.setSubject("====后台管理项目异常====");
		mailMessage.setText("后台管理项目异常");
		mailMessage.setTo("46886799@163.com");
		// 发送邮件
		javaMailSender.send(mailMessage);
		log.info("==================结束发送异常提醒邮件================");

	}

}
