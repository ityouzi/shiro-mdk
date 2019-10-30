package com.mdkproject.mdkshiro.controller;


import com.mdkproject.mdkshiro.response.CommonReturnType;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: Liberal-World
 * @Date: 2019-04-08 09:14
 * @Description:短信http接口的java代码调用示例,基于Apache HttpClient 4.3
 * @Version 1.0
 */
@Controller
@RequestMapping("/sms")
@CrossOrigin(origins = "*",allowCredentials = "true")
public class JavaSmsApi extends BaseController{


    private static final Logger logger = LoggerFactory.getLogger(JavaSmsApi.class);


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

    @Autowired
    private StringRedisTemplate redisTemplate;




    /**
     * @auther: lizhen
     * @date: 2019-03-28 13:54
     * 功能描述: 发送短信，默认post请求
     */

    @RequestMapping( value = "/sendMsg",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public CommonReturnType sendMsg(@RequestParam(value = "telphone")String telphone) throws Exception {
        logger.info("短信请求来了:"+telphone);

        //redis中的key是否存在,如果存在就不从心获取，不存在就重新获取
        if (!redisTemplate.hasKey(telphone)){
            //修改为您的apikey.apikey可在官网（http://www.yunpian.com)登录后获取
            String apikey = "7ad4203389369086f3d7aad7cd85b60a";
            //修改为您要发送的手机号
            String mobile = telphone;

            //查账户信息调用示例
//        System.out.println(JavaSmsApi.getUserInfo(apikey));

            //使用智能匹配模板接口发短信(推荐)
            //设置您要发送的内容(内容必须和某个模板匹配。以下例子匹配的是系统提供的1号模板)
            //需要按照一定的规则生成OTP验证码
            int num = (int) (( Math.random()*9+1)*1000);
            String code = String.valueOf(num);
            String text = "【玛迪卡云】欢迎使用《从业人员健康体检管理平台》，验证码"+code+"，请在20分钟之内按页面提示提交验证码。";


            //将OTP验证码同对应的手机号关联,使用httpsession的方式绑定手机号
            String s = JavaSmsApi.sendSms(apikey, text, mobile);

            JSONObject json = JSONObject.fromObject(s);
            //0代表发送成功，其他code代表出错，详细见"返回值说明"页面
            String startcode = json.getString("code");
            if (startcode != null && startcode.equals("0")){
                //请求成功，短信已发送
                //将手机号和生成的随机数存入redis 并设置redis的过期时间20分钟
                logger.info("短信发送成功："+startcode);
                redisTemplate.opsForValue().set(mobile,code,20l, TimeUnit.MINUTES);
                return CommonReturnType.createCommonReturnType("验证码已发送，请查收！","200");
            }else if (startcode != null && startcode.equals("22")){
                logger.info("短信发送次数过多");
                return CommonReturnType.createCommonReturnType("短信发送次数过多，一小时3次","400");
            }else {
                logger.info("发送短信异常，状态码："+startcode);
                return CommonReturnType.createCommonReturnType("验证码获取失败","100");
            }
        }else{
            //获取redis缓存中的手机号key和验证码value
            String code = redisTemplate.opsForValue().get(telphone);
            return CommonReturnType.createCommonReturnType("验证码有效期20分钟不要再次发送","300");
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
