package com.lantern.util;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;

/**
 * Created by seventh on 17-5-28.
 */
public class VerifyUtil {
    private static final String ACCESSID = "LTAIDlb7dtOGlTzi";
    private static final String ACCESSKEY = "0JWDTa8dJ5acGDBWqaEf0M8z1mLXUz";
    private static final String ACCOUNTENDPOINT = "http://1541493352773559.mns.cn-hangzhou.aliyuncs.com";
    private static final String TOPIC = "sms.topic-cn-hangzhou";
    private static final String SIGNNAME = "Lantern小灯笼";
    private static final String TEMPLATECODE = "SMS_69185476";

    public static String sendVerify(String username) {
        CloudAccount account = new CloudAccount(ACCESSID, ACCESSKEY,ACCOUNTENDPOINT);
        MNSClient client = account.getMNSClient();
        CloudTopic topic = client.getTopicRef(TOPIC);
        /**
         * Step 2. 设置SMS消息体（必须）
         *
         * 注：目前暂时不支持消息内容为空，需要指定消息内容，不为空即可。
         */
        RawTopicMessage msg = new RawTopicMessage();
        msg.setMessageBody("sms-message");
        /**
         * Step 3. 生成SMS消息属性
         */
        MessageAttributes messageAttributes = new MessageAttributes();
        BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
        // 3.1 设置发送短信的签名（SMSSignName）
        batchSmsAttributes.setFreeSignName(SIGNNAME);
        // 3.2 设置发送短信使用的模板（SMSTemplateCode）
        batchSmsAttributes.setTemplateCode(TEMPLATECODE);
        // 3.3 设置发送短信所使用的模板中参数对应的值（在短信模板中定义的，没有可以不用设置）
        BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
        String code = String.valueOf((int)((Math.random()*9+1)*100000));
        smsReceiverParams.setParam("code", code);
        // 3.4 增加接收短信的号码
        batchSmsAttributes.addSmsReceiver(username, smsReceiverParams);
        messageAttributes.setBatchSmsAttributes(batchSmsAttributes);
        try {
            /**
             * Step 4. 发布SMS消息
             */
            TopicMessage ret = topic.publishMessage(msg, messageAttributes);
            System.out.println("MessageId: " + ret.getMessageId());
            System.out.println("MessageMD5: " + ret.getMessageBodyMD5());
            client.close();
            return code;
        } catch (ServiceException se) {
            System.out.println(se.getErrorCode() + se.getRequestId());
            System.out.println(se.getMessage());
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.close();
        return null;
    }

}
