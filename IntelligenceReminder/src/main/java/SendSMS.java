import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public class SendSMS {
    public static void SMS(String U_TEL, String SMS_CODE, String SMS_PARAM) {
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-shanghai",
                "",
                "");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", U_TEL);
        request.putQueryParameter("SignName", "上海铁新地理信息有限公司");
        request.putQueryParameter("TemplateCode", SMS_CODE);  //SMS_CODE此处为短信模板号
        request.putQueryParameter("TemplateParam", SMS_PARAM); //SMS_PARAM此处为短信模板所需的短信信息
        // request.putQueryParameter("TemplateParam", "{\"u_name\":\"1\",\"u_id\":\"2\",\"xm_name\":\"3\",\"lc_name\":\"4\",\"bz_name\":\"5\",\"e_day\":\"6\",\"ed_day\":\"7\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
