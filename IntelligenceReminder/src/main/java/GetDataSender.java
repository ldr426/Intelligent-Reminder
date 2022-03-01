import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;//Date 表示特定的瞬间，精确到毫秒
import java.text.SimpleDateFormat;//SimpleDateFormat根据特定的日期格式在Date和字符串之间转换
import java.util.Calendar;//java.util.Calendar是抽象类，主要用来对时间分量进行计算

/**
 * Created by qcl on 2017/11/18.
 * 数据库连接
 */
public class GetDataSender {
    public static void main(String[] args) {
        Connection con;
        String driver="com.mysql.jdbc.Driver";
        String url="";
        String user="root"; //用户名
        String password="@123";

        // 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
        // PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）,
        // 对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
        String myEmailAccount = "1933722481@qq.com";
        String myEmailPassword = "rljooxqxrghkcfei"; //密码如果出错，改用SMTP独立密码（“授权码”）
        // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
        String myEmailSMTPHost = "smtp.qq.com";

        //流程提醒模板CODE
        String SMS_TXCODE = "SMS_203716170";

        long ID; //ID号
        String XM_NAME; //项目名
        String LC_NAME; //流程名
        String BZ_NAME; //步骤名
        String S_TIME; //开始时间
        long E_TIME; //项目时长（天）
        long ED_TIME; //距离结束的天数
        String U_ID; //工号
        String U_NAME; //名字
        String U_TEL; //电话
        String U_EMAIL; //邮箱
        String U_WECHAT; //微信
        String E_DAY; //截止日期
        String SMS_TXPARAM; //用于生成SMS提醒功能的JSON串
        long days; //计算项目已经进行的天数
        long hours; //计算项目已经进行的天数之后的小时
        long mins; //计算项目已经进行的天数、小时之后的分钟
        long secs; //计算项目已经进行的天数、小时、分钟之后的秒数
        long Days; //计算项目剩余的天数
        long Hours; //计算项目剩余的天数之后的小时
        long Mins; //计算项目剩余的天数、小时之后的分钟
        long Secs; //计算项目剩余的天数、小时、分钟之后的秒数

        //SMS发送JSON格式的制作字符串
        String TXs1 = "{\"u_name\":\"";
        String TXs2 = "\",\"u_id\":\"";
        String TXs3 = "\",\"xm_name\":\"";
        String TXs4 = "\",\"lc_name\":\"";
        String TXs5 = "\",\"bz_name\":\"";
        String TXs6 = "\",\"e_day\":\"";
        String TXs7 = "\",\"ed_day\":\"";
        String TXs8 = "\"}";

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        //给Calendar设置具体时间值
        //cal.set(year, month, date);这样设置时间也可以
        // 设置当前Calendar实例中的时间,如不设置则默认为系统当前时间
            String current = df.format(System.currentTimeMillis());
            //获取系统时间 && 时间格式转换
            Date date = new Date();
            //利用转换为Date格式进行时间的计算
            System.out.println("Start time:" + current);

            int res_0; //用于判断项目是否达到开始时间
            int res_1; //用于判断项目是否超过结束期限
            int rem; //用于计算余数

            try {
                Class.forName(driver);
                con = DriverManager.getConnection(url, user, password);
                if (!con.isClosed()) {
                    System.out.println("数据库连接成功");
                }
                Statement statement = con.createStatement();
                String sql = "select * from meetings.procedure_message;";//我的表格叫home
                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    ID = resultSet.getLong("ID");
                    XM_NAME = resultSet.getString("XM_NAME");
                    LC_NAME = resultSet.getString("LC_NAME");
                    BZ_NAME = resultSet.getString("BZ_NAME");
                    S_TIME = resultSet.getString("S_TIME");
                    E_TIME = resultSet.getLong("E_TIME");
                    U_ID = resultSet.getString("U_ID");
                    U_NAME = resultSet.getString("U_NAME");
                    U_TEL = resultSet.getString("U_TEL");
                    U_EMAIL = resultSet.getString("U_EMAIL");
                    U_WECHAT = resultSet.getString("U_WECHAT");

                    System.out.println(ID+" "+XM_NAME+" "+LC_NAME+" "+BZ_NAME+" "+S_TIME+" "+E_TIME+" "+U_ID+" "+U_NAME+" "+U_TEL+" "+U_EMAIL+" "+U_WECHAT);

                    current = df.format(System.currentTimeMillis());
                    res_0 = current.compareTo(S_TIME); //current与S_TIME比较，若现在时间在开始时间后，值为1

                    if (res_0 >= 0) {
                        Date d1 = df.parse(S_TIME); //导入开始时间
                        Date d2 = df.parse(current); //导入系统当前时间

                        //计算项目已经进行的时间
                        days = (d2.getTime() - d1.getTime()) / (24*60*60*1000);
                        hours = ((d2.getTime() - d1.getTime()) / (60*60*1000)) % 24;
                        mins = ((d2.getTime() - d1.getTime()) / (60*1000)) % 60;
                        secs = ((d2.getTime() - d1.getTime()) / 1000) % 60;
                        System.out.println("This project has been:" + days + " day(s) " + hours + " hour(s) " + mins + " minute(s) " + secs + " second(s) ");

                        //string转calendar，便于进行时间计算
                        cal.setTime(d1);
                        cal.add(Calendar.DATE, (int) E_TIME);
                        Date d3 = cal.getTime();
                        E_DAY = dt.format(d3);
                        System.out.println(d3);
                        System.out.println(E_DAY);

                        //计算项目在规定期限内剩余的时间
                        Days = (d3.getTime() - d2.getTime()) / (24*60*60*1000);
                        Hours = ((d3.getTime() - d2.getTime()) / (60*60*1000)) % 24;
                        Mins = ((d3.getTime() - d2.getTime()) / (60*1000)) % 60;
                        Secs = ((d3.getTime() - d2.getTime()) / 1000) % 60;
                        System.out.println("This project has :" + Days + " day(s) " + Hours + " hour(s) " + Mins + " minute(s) " + Secs + " second(s) " + " left.");

                        ED_TIME = Days;
                        SMS_TXPARAM = TXs1 + U_NAME + TXs2 + U_ID + TXs3 + XM_NAME + TXs4 + LC_NAME + TXs5 + BZ_NAME + TXs6 + E_DAY + TXs7 + ED_TIME + TXs8;

                        SendSMS.SMS(U_TEL, SMS_TXCODE, SMS_TXPARAM);
                        SendEMAIL.SendEmail(myEmailSMTPHost, myEmailAccount, myEmailPassword, U_EMAIL, U_NAME, U_ID, XM_NAME, LC_NAME, BZ_NAME, E_DAY, ED_TIME);
                        System.out.println(U_TEL + "已发送");

                        if ((Days >= 0)&&(Hours >= 0)&&(Mins >= 0)&&(Secs >= 0)){
                            //期限小于等于5天时，每1天发送一次
                            if (Days <= 5){
                                SendSMS.SMS(U_TEL, SMS_TXCODE, SMS_TXPARAM);
                                SendEMAIL.SendEmail(myEmailSMTPHost, myEmailAccount, myEmailPassword, U_EMAIL, U_NAME, U_ID, XM_NAME, LC_NAME, BZ_NAME, E_DAY, ED_TIME);
                                System.out.println(U_TEL + "已发送");
                            }
                            //期限小于等于15天，大于5天时，每2天发送一次
                            else if (Days <= 15){
                                rem = ((int)Days - 5) % 2;
                                if (rem == 0){
                                    SendSMS.SMS(U_TEL, SMS_TXCODE, SMS_TXPARAM);
                                    SendEMAIL.SendEmail(myEmailSMTPHost, myEmailAccount, myEmailPassword, U_EMAIL, U_NAME, U_ID, XM_NAME, LC_NAME, BZ_NAME, E_DAY, ED_TIME);
                                    System.out.println(U_TEL + "-已发送");
                                }
                            }
                            //期限小于等于35天，大于15天时，每3天发送一次
                            else if (Days <= 35){
                                rem = ((int)Days - 15) % 3;
                                if (rem == 0){
                                    SendSMS.SMS(U_TEL, SMS_TXCODE, SMS_TXPARAM);
                                    SendEMAIL.SendEmail(myEmailSMTPHost, myEmailAccount, myEmailPassword, U_EMAIL, U_NAME, U_ID, XM_NAME, LC_NAME, BZ_NAME, E_DAY, ED_TIME);
                                    System.out.println(U_TEL + "已发送");
                                }
                            }
                            //期限大于35天，每5天发送一次
                            else {
                                rem = ((int)Days - 35) % 5;
                                if (rem == 0){
                                    SendSMS.SMS(U_TEL, SMS_TXCODE, SMS_TXPARAM);
                                    SendEMAIL.SendEmail(myEmailSMTPHost, myEmailAccount, myEmailPassword, U_EMAIL, U_NAME, U_ID, XM_NAME, LC_NAME, BZ_NAME, E_DAY, ED_TIME);
                                    System.out.println(U_TEL + "已发送");
                                }
                            }
                        }
                    }


                }
                resultSet.close();
                con.close();
            } catch (ClassNotFoundException e) {
                System.out.println("数据库驱动没有安装");
            } catch (SQLException e) {
                System.out.println("数据库连接失败");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}