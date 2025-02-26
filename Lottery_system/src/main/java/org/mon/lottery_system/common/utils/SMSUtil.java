package org.mon.lottery_system.common.utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SMSUtil {
    private static final Logger logger = LoggerFactory.getLogger(SMSUtil.class);

    @Value(value = "${sms.sign-name}")
    private String signName;
    @Value(value = "${sms.access-key-id}")
    private String accessKeyId;
    @Value(value = "${sms.access-key-secret}")
    private String accessKeySecret;

    /**
     * 发送短信
     *
     * @param templateCode 模板号
     * @param phoneNumbers 手机号
     * @param templateParam 模板参数 {"key":"value"}
     */
    public void sendMessage(String templateCode, String phoneNumbers, String templateParam) {
        try {
            Client client = createClient();
            com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setSignName(signName)
                    .setTemplateCode(templateCode)
                    .setPhoneNumbers(phoneNumbers)
                    .setTemplateParam(templateParam);
            RuntimeOptions runtime = new RuntimeOptions();
            SendSmsResponse response = client.sendSmsWithOptions(sendSmsRequest, runtime);
            if (null != response.getBody()
                    && null != response.getBody().getMessage()
                    && "OK".equals(response.getBody().getMessage())) {
                logger.info("向{}发送信息成功，templateCode={}", phoneNumbers, templateCode);
                return;
            }
            logger.error("向{}发送信息失败，templateCode={}，失败原因：{}",
                    phoneNumbers, templateCode, response.getBody().getMessage());
        } catch (TeaException error) {
            logger.error("向{}发送信息失败，templateCode={}", phoneNumbers, templateCode, error);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            logger.error("向{}发送信息失败，templateCode={}", phoneNumbers, templateCode, error);
        }
    }

    /**
     * 使用AK&SK初始化账号Client
     * @return Client
     */
    private Client createClient() throws Exception {
        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考。
        // 建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html。
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }

}


