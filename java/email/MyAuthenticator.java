package com.whaty.core.commons.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MyAuthenticator extends Authenticator{
	String userName = null;
	String password = null;

	public MyAuthenticator() {
	}

	public MyAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}
	
	
	public static void main(String[] args){     
     //这个类主要是设置邮件     
     MailSenderInfo mailInfo = new MailSenderInfo();      
     mailInfo.setMailServerHost("smtp.whaty.com");      
     mailInfo.setMailServerPort("25");      
     mailInfo.setValidate(true);      
     mailInfo.setUserName("jipengfei@whaty.com");      
     mailInfo.setPassword("whaty@jipengfei");//您的邮箱密码      
     mailInfo.setFromAddress("jipengfei@whaty.com");      
     mailInfo.setToAddress("jipengfei@whaty.com");    
     mailInfo.setCcAddress("jipengfei@whaty.com;guoleitao@whaty.com");  
     mailInfo.setSubject("设置邮箱标题 如http://www.guihua.org 中国桂花网");      
     mailInfo.setContent("设置邮箱内容 如<div>http://www.1ting.com/rand.php</div>http://www.guihua.org 中国桂花网 是中国最大桂花网站==");      
        //这个类主要来发送邮件     
     SimpleMailSender sms = new SimpleMailSender();     
     sms.sendTextMail(mailInfo);//发送文体格式      
         //sms.sendHtmlMail(mailInfo);//发送html格式     
   }  
	
	
}
