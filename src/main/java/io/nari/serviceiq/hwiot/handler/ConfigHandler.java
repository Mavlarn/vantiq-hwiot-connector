package io.nari.serviceiq.hwiot.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

//import com.huawei.it.eip.ump.client.consumer.ConsumeStatus;
//import com.huawei.it.eip.ump.client.consumer.Consumer;
//import com.huawei.it.eip.ump.common.exception.UmpException;

import com.huawei.it.eip.ump.client.consumer.ConsumeStatus;
import com.huawei.it.eip.ump.client.consumer.Consumer;
import com.huawei.it.eip.ump.client.listener.MessageListener;
import com.huawei.it.eip.ump.common.exception.UmpException;
import com.huawei.it.eip.ump.common.message.Message;

import io.nari.serviceiq.extsdk.ExtensionServiceMessage;
import io.nari.serviceiq.extsdk.Handler;
import io.nari.serviceiq.hwiot.HWIOTConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class ConfigHandler extends Handler<ExtensionServiceMessage> {

    static final Logger LOG = LoggerFactory.getLogger(ConfigHandler.class);

    private static final String CONFIG = "config";
//    consumer.setUmpNamesrvUrls("MQS Name Server IP:9776"); // 设置统一消息平台的服务器地址
//    consumer.setAppId("XXXXID");        // 设置客户端账号
//    consumer.setAppSecret("*****");     // 设置客户端密钥
//    consumer.setTopic("Topic Name");    // 设置Topic Name
    private static final String MQS_SERVER_URL = "mqs_server_url";
    private static final String MQS_APP_ID = "mqs_user";
    private static final String MQS_APP_SEC = "mqs_password";
    private static final String TOPICS = "topics";


    private HWIOTConnector connector;
    private ObjectMapper om = new ObjectMapper();

    public ConfigHandler(HWIOTConnector connector) {
        this.connector = connector;
    }

    /**
     * topics: [
     *  {
     *      queue: "topic1", protobuf_name: "face"
     *  }
     * ]
     * @param message   A message to be handled
     */
    @Override
    public void handleMessage(ExtensionServiceMessage message) {
        LOG.warn("No configuration need for source:{}", message.getSourceName());
        Map<String, Object> configObject = (Map) message.getObject();
        Map<String, String> topicConfig;

        // Obtain entire config from the message object
        if ( !(configObject.get(CONFIG) instanceof Map)) {
            LOG.error("Configuration failed. No configuration suitable for AMQP Connector.");
            return;
        }
        topicConfig = (Map) configObject.get(CONFIG);

        String mqsServer = topicConfig.get(MQS_SERVER_URL);
        String mqsAppId = topicConfig.get(MQS_APP_ID);
        String mqsAppSec = topicConfig.get(MQS_APP_SEC);

        String topicStr = topicConfig.get(TOPICS);
        String[] topics = topicStr.split(",");


        try {
            for (String topic: topics) {

                Consumer consumer = new Consumer();
                consumer.setUmpNamesrvUrls(mqsServer); // 设置统一消息平台的服务器地址
                consumer.setAppId(mqsAppId);        // 设置客户端账号
                consumer.setAppSecret(mqsAppSec);     // 设置客户端密钥
                consumer.setTopic(topic);// 设置Topic Name
                consumer.setTags("*");                  // 设置订阅消息的标签，可以指定消费某一类型的消息，默认*表示消费所有类型的消息
                consumer.setEncryptTransport(false);// 设置是否需要加密传输
                /*
                consumer.subscribe(message1 -> {
                    try {
                        String msg = new String(message1.getBody(), "UTF-8");
                        System.out.println("Receive: " + msg);
                        Map data = om.readValue(msg, Map.class);
                        LOG.debug("result json string: {}", data);
                        connector.getVantiqClient().sendNotification(data);
                    } catch (Exception e) {
                        LOG.error(e.getMessage(), e);
                    }
                    // 正常接收到消息后，请务必返回CONSUME_SUCCESS，只有在业务处理失败才返回RECONSUME_LATER
                    return ConsumeStatus.CONSUME_SUCCESS;
                });
                */
                consumer.subscribe(new MessageListener() {
                    public ConsumeStatus consume(Message message) {
                        try {
                            String msg = new String(message.getBody(), "UTF-8");
                            System.out.println("Receive: " + msg);
                            Map data = om.readValue(msg, Map.class);
                            LOG.debug("result json string: {}", data);
                            connector.getVantiqClient().sendNotification(data);
                        } catch (Exception e) {
                            LOG.error(e.getMessage(), e);
                        }
                        // 正常接收到消息后，请务必返回CONSUME_SUCCESS，只有在业务处理失败才返回RECONSUME_LATER
                        return ConsumeStatus.CONSUME_SUCCESS;
                    }
                });
                consumer.start();
                System.out.println("########after consumer.start");
                this.connector.addMqsConsumer(consumer);
            }

        } catch (UmpException e) {
            LOG.error(e.getMessage(), e);
        }

    }

}
