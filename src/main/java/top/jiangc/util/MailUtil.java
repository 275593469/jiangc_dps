package top.jiangc.util;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.internet.MimeUtility;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailUtil {
	
	private static Logger logger = LoggerFactory.getLogger(MailUtil.class);

	private static final String HOST = "smtp.163.com";
    private static final String USERNAME = "jiangc";
    private static final String PASSWOED = "123456jiangc";
    // 发件人邮箱
    private static final String FROM_EMAIL = "jiangc_java@163.com";
    
    //收件人用户名
    private String toName;
	// 收件人邮箱，多个邮箱以“;”分隔
	private String toEmails;
	// 邮件主题
	private String subject;
	// 邮件内容
	private String content;
	// 邮件中的图片，为空时无图片。map中的key为图片ID，value为图片地址
	private Map<String, String> pictures;
	// 邮件中的附件，为空时无附件。map中的key为附件ID，value为附件地址
	private Map<String, String> attachments;
    
	/**
	 * 发送简单邮件
	 * @author: jiangcheng
	 *
	 * @param subject 主题
	 * @param content 内容
	 * @param toEmails 收件人
	 * @throws EmailException
	 */
	public static void sendMail(String subject, String content, String... toEmails) throws EmailException {
		SimpleEmail email = new SimpleEmail();
        email.setHostName(HOST);// 设置smtp服务器
        email.setAuthentication(FROM_EMAIL, PASSWOED);// 设置授权信息
        email.setCharset("utf-8");
        
		email.setFrom(FROM_EMAIL, USERNAME, "utf-8");// 设置发件人信息
		email.addTo(toEmails);// 设置收件人信息
		email.setSubject(subject);// 设置主题
		email.setMsg(content);// 设置邮件内容
		email.send();// 发送邮件
	}
	
	/**
	 * 发送简单邮件
	 * @author: jiangcheng
	 *
	 * @param subject 主题
	 * @param content 内容
	 * @param toEmails 收件人
	 * @throws EmailException
	 */
	public static void sendHtmlMail(String subject, String content, String... toEmails) throws EmailException {
		HtmlEmail  email = new HtmlEmail ();
        email.setHostName(HOST);// 设置smtp服务器
        email.setAuthentication(FROM_EMAIL, PASSWOED);// 设置授权信息
        email.setCharset("utf-8");
        
		email.setFrom(FROM_EMAIL, USERNAME, "utf-8");// 设置发件人信息
		email.addTo(toEmails);// 设置收件人信息
		email.setSubject(subject);// 设置主题
		email.setHtmlMsg(content);// 设置邮件内容
		email.send();// 发送邮件
	}
	
	/**
	 * 发送邮件，并带有附件
	 * @author: jiangcheng
	 *
	 * @param subject 主题
	 * @param content 内容
	 * @param attachments 附件，key为附件名，value为附件绝对路径
	 * @param toEmails 收件人
	 * @throws EmailException
	 * @throws UnsupportedEncodingException
	 */
	public static void sendMail(String subject, String content, Map<String, Object> attachments, String... toEmails) throws EmailException, UnsupportedEncodingException {
		MultiPartEmail email = new MultiPartEmail();
		  email.setHostName(HOST);// 设置smtp服务器
	        email.setAuthentication(FROM_EMAIL, PASSWOED);// 设置授权信息
	        email.setCharset("utf-8");
	        
			email.setFrom(FROM_EMAIL, USERNAME, "utf-8");// 设置发件人信息
			email.addTo(toEmails);// 设置收件人信息
			email.setSubject(subject);// 设置主题
			email.setMsg(content);// 设置邮件内容
			EmailAttachment ment = new EmailAttachment();
			for(Entry<String, Object> entry : attachments.entrySet()){

				ment.setPath((String) entry.getValue());//附件觉得路径
				ment.setName(MimeUtility.encodeText(entry.getKey()));//附件在邮件中显示的名称，也是下载保存的名字[需要带后缀]
				// 将附件添加到邮件中
				email.attach(ment);
			}

			email.send();
	}
	
}
