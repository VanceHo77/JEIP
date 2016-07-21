/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utility;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * 寄發email
 *
 * @author Vance
 */
public class MailUtility {

    //smtp的host名稱
    String host = "";
    //寄件人信箱
    String sender = "";
    //寄件人信箱密碼
    String password = "";
    private static Properties props = new Properties();

    /**
     * 建構子(使用參數)，空白預設使用HINET
     *
     * @param mode 目前有 GMAIL,DSIC
     */
    public MailUtility(String mode) {
        JavaMailSenderImpl senderIMP = new JavaMailSenderImpl();
        switch (mode) {
            case "GMAIL":
                this.host = "smtp.gmail.com";
                this.sender = "gn770612@gmail.com";
                this.password = "m951a753i";
                break;
            case "DSIC":
                this.host = "mail.dsic.com.tw";
                this.sender = "vance@mail.dsic.com.tw";
                this.password = "gn951753";
                break;
            default:
                this.host = "msa.hinet.net";
                this.sender = "jac.k9030@msa.hinet.net";
                this.password = "jp j04RMP";
                break;
        }
        setProperties(senderIMP);
    }

    /**
     * 建構子(自訂寄件人資訊)
     *
     * @param host
     * @param username
     * @param password
     * @param port
     */
    public MailUtility(String host, String username, String password) {
        JavaMailSenderImpl senderIMP = new JavaMailSenderImpl();
        this.host = host;
        this.sender = username;
        this.password = password;
        //設定寄信server資訊
        setProperties(senderIMP);
    }

    private void setProperties(JavaMailSenderImpl senderIMP) {
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        senderIMP.setJavaMailProperties(props);
    }

    /**
     * 寄送email，addressee=收件人信箱，subject=郵件標題，mailbody=郵件內容
     *
     * @param addressee
     * @param subject
     * @param mailbody
     * @return
     * @throws javax.mail.MessagingException
     */
    public String sendMail(String addressee, String subject, String mailbody) throws MessagingException {
        String rtnMsg;
        Transport transport = null;
        try {
            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(true);

            MimeMessage message = new MimeMessage(mailSession);
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(mailbody, "text/html; charset=UTF-8");

            //產生整封 email 的主體
            Multipart emailBody = new MimeMultipart();

            emailBody.addBodyPart(textPart);
            //寄送日期
            message.setSentDate(new Date());
            //設定主旨
            message.setSubject(subject, "UTF-8");
            //設定文字內容
            message.setContent(emailBody);
            //設定寄信人email
            message.setFrom(new InternetAddress(sender));
            //設定收件人
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addressee));

            transport = mailSession.getTransport("smtp");

            transport.connect(this.host, this.sender, this.password);

            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));

            rtnMsg = "寄出成功";

        } catch (Exception e) {
            throw e;
        } finally {
            transport.close();
        }

        return rtnMsg;
    }
}
