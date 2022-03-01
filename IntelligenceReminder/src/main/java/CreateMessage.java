import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

public class CreateMessage {
    static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail, String U_NAME, String U_ID, String XM_NAME, String LC_NAME, String BZ_NAME, String E_DAY, long ED_TIME) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, "铁新地理", "UTF-8"));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "usersName", "UTF-8"));

        // 4. Subject: 邮件主题
        message.setSubject("流程进度提醒", "UTF-8");

        // 5. Content: 邮件正文（可以使用html标签）
        String emailText = "【铁新地理】" + U_NAME + "(工号:" + U_ID + ")，您好。您所参与的项目：“" + XM_NAME + "”，已进行到“" + LC_NAME + "”流程的“"+BZ_NAME+"”步骤，距离结束日期" + E_DAY + "还有" + ED_TIME + "天,请按时完成工程项目。";
        message.setContent(emailText, "text/html;charset=UTF-8");

        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
        message.saveChanges();

        return message;
    }
}

