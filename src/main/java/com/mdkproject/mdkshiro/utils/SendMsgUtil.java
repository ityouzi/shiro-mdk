package com.mdkproject.mdkshiro.utils;

import com.mdkproject.mdkshiro.controller.JavaSmsApi;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Liberal-World
 * @Date: 2019-06-04 16:09
 * @Description:
 * @Version 1.0
 */
public class SendMsgUtil {

    private static final Logger logger = LoggerFactory.getLogger(SendMsgUtil.class);


    //查帐户信息的http地址
    private static String URI_GET_USER_INFO = "https://sms.yunpian.com/v2/user/get.json";

    //智能匹配模板发送接口的http地址
    private static String URI_SEND_SMS = "https://sms.yunpian.com/v2/sms/single_send.json";

    //模板发送接口的http地址
    private static String URI_TPL_SEND_SMS = "https://sms.yunpian.com/v2/sms/tpl_single_send.json";

    //发送语音验证码接口的http地址
    private static String URI_SEND_VOICE = "https://voice.yunpian.com/v2/voice/send.json";

    //绑定主叫、被叫关系的接口http地址
    private static String URI_SEND_BIND = "https://call.yunpian.com/v2/call/bind.json";

    //解绑主叫、被叫关系的接口http地址
    private static String URI_SEND_UNBIND = "https://call.yunpian.com/v2/call/unbind.json";

    //编码格式。发送编码格式统一用UTF-8
    private static String ENCODING = "UTF-8";


    private static String text1 = "【玛迪卡云】尊敬的#name#，您的体检不合格，请在7个工作日内重新预约体检，体检结果请咨询体检机构。";
    private static String text2 = "【玛迪卡云】尊敬的#name#，请本人在7个工作日内领取健康证明。或登陆<武汉健康证>小程序自助领取电子健康证。";
    private static String text3 = "【玛迪卡云】尊敬的#name#，体检登记已成功。您可登陆www.jkzcloud.com 平台或<武汉健康证>小程序自助查询结果。";
    private static String text4 = "【玛迪卡云】您的体检预约不成功，请贵单位在3个工作日内查看原因，并重新上传预约资料。";
    private static String text5 = "【玛迪卡云】您已成功预约体检，请贵单位体检人于#time#当日携带身份证原件登记，预约当日有效。";
    private static String text6 = "【玛迪卡云】尊敬的#name#，您已成功办理健康证，自助查询请登陆www.jkzcloud.com 平台或<武汉健康证>小程序查看。";




    /**
     * @auther: lizhen
     * @date: 2019-03-28 13:54
     * 功能描述: 发送短信，默认post请求
     */


    public static int sendMsg(String telphone,String content,String type) throws Exception {
        logger.info("短信请求来了");
        //修改为您的apikey.apikey可在官网（http://www.yunpian.com)登录后获取
        String apikey = "7ad4203389369086f3d7aad7cd85b60a";
        //修改为您要发送的手机号
        String mobile = telphone;

        //查账户信息调用示例
//        System.out.println(JavaSmsApi.getUserInfo(apikey));

        //使用智能匹配模板接口发短信(推荐)
        //设置您要发送的内容(内容必须和某个模板匹配。以下例子匹配的是系统提供的1号模板)

        //模板选择
        String text = null;
        if (type.equals("text1")) {
            text = "【玛迪卡云】尊敬的"+content+"，您的体检不合格，请在7个工作日内重新预约体检，体检结果请咨询体检机构。";
        }
        if (type.equals("text2")){
            text = "【玛迪卡云】尊敬的"+content+"，您的体检合格，请本人在7个工作日内领取健康证明。或登陆<武汉健康证>小程序自助领取电子健康证。";
        }
        if (type.equals("text3")){
            text = "【玛迪卡云】尊敬的"+content+"，体检登记已成功。您可登陆www.jkzcloud.com 平台或<武汉健康证>小程序自助查询结果。";
        }
        if (type.equals("text4")){
            text = "【玛迪卡云】您的体检预约不成功，请贵单位在3个工作日内查看原因，并重新上传预约资料。";
        }
        if (type.equals("text5")){
            text = "【玛迪卡云】您已成功预约体检，请贵单位体检人于"+content+"当日携带身份证原件登记，预约当日有效。";
        }
        if (type.equals("text6")){
            text = "【玛迪卡云】尊敬的"+content+"，您已成功办理健康证，自助查询请登陆www.jkzcloud.com 平台或<武汉健康证>小程序查看。";
        }



        //将OTP验证码同对应的手机号关联,使用httpsession的方式绑定手机号
        String s = JavaSmsApi.sendSms(apikey, text, mobile);

        JSONObject json = JSONObject.fromObject(s);
        //0代表发送成功，其他code代表出错，详细见"返回值说明"页面
        String startcode = json.getString("code");
        System.err.println(startcode);
        if (startcode != null && startcode.equals("0")){
            //请求成功，短信已发送
            return 0;
        }else if (startcode != null && startcode.equals("22")){
            return 22;//已达上限
        }else {
            return 1;//失败
        }
    }


    /**
     * 取账户信息
     * @return json格式字符串
     * @throws IOException
     */
    public static String getUserInfo(String apikey){
        Map<String,String> params = new HashMap<>();
        params.put("apikey",apikey);
        return post(URI_GET_USER_INFO,params);
    }


    /**
     * 智能匹配模板接口发短信
     * @param apikey apikey账户
     * @param text   　短信内容
     * @param mobile 　接受的手机号
     * @return json格式字符串
     * @throws IOException
     */
    public static String sendSms(String apikey, String text, String mobile) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("apikey",apikey);
        params.put("text",text);
        params.put("mobile",mobile);
        return post(URI_SEND_SMS,params);
    }


    /**
     * 基于HttpClient 4.3的通用POST方法
     * @param url       提交的URL查帐户信息的http地址
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */
    public static String post(String url,Map<String, String> paramsMap){
        //http请求
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            if (paramsMap != null){
                List<NameValuePair> paramList = new ArrayList<>();
                for (Map.Entry<String, String> param: paramsMap.entrySet()){
                    NameValuePair pair = new BasicNameValuePair(param.getKey(),param.getValue());
                    paramList.add(pair);
                }
                //编码格式。发送编码格式统一用UTF-8
                method.setEntity(new UrlEncodedFormEntity(paramList,ENCODING));
            }
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null){
                responseText = EntityUtils.toString(entity, ENCODING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return responseText;
    }





}
