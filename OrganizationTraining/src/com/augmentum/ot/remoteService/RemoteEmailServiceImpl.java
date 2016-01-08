package com.augmentum.ot.remoteService;

import java.util.Date;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.email.EmailConstant;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.util.BeanFactory;

@Component("remoteEmailService")
public class RemoteEmailServiceImpl implements IRemoteEmailService {
    private static Logger logger = Logger.getLogger(RemoteEmailServiceImpl.class);

    @Override
    public void sendEmail(Map<String, String> messageMap) throws ServerErrorException {
        // TODO
        if (messageMap.get(EmailConstant.EMAIL_TO) == null) {
            messageMap.put(EmailConstant.EMAIL_TO, "");
        }
        if (messageMap.get(EmailConstant.EMAIL_CC) == null) {
            messageMap.put(EmailConstant.EMAIL_CC, "");
        }
        if (messageMap.get(EmailConstant.EMAIL_BCC) == null) {
            messageMap.put(EmailConstant.EMAIL_BCC, "");
        }
        logger.info("email to: " + messageMap.get(EmailConstant.EMAIL_TO));
        logger.info("email cc: " + messageMap.get(EmailConstant.EMAIL_CC));
        logger.info("email bcc: " + messageMap.get(EmailConstant.EMAIL_BCC));

        try {
            localsendEmail(messageMap.get(EmailConstant.EMAIL_SUBJECT), messageMap.get(EmailConstant.EMAIL_TO), messageMap
                    .get(EmailConstant.EMAIL_CC), messageMap.get(EmailConstant.EMAIL_BCC), messageMap.get(EmailConstant.EMAIL_CONTENT));
            logger.info("send Email complete ");
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("Send Email"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
    }

    private static boolean localsendEmail(String subject, String to, String cc, String bcc, String content) throws MessagingException {
        String from = "OrganinzitionTrainingSystem";

        JavaMailSenderImpl mailSender = BeanFactory.getJavaMailSenderImpl();
        MimeMessage msg = mailSender.createMimeMessage();

        msg.setFrom(new InternetAddress(from));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to.replaceAll(";", ",")));
        msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc.replaceAll(";", ",")));
        msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc.replaceAll(";", ",")));

        msg.setSubject(subject);
        msg.setSentDate(new Date());

        msg.setContent(content, "text/html;charset = utf-8");
        msg.saveChanges();
        mailSender.send(msg);
        return true;
    }
}
