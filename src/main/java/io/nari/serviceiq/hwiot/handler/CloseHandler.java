package io.nari.serviceiq.hwiot.handler;

import com.huawei.it.eip.ump.client.consumer.Consumer;
import com.huawei.it.eip.ump.common.exception.UmpException;
import io.nari.serviceiq.extsdk.ExtensionWebSocketClient;
import io.nari.serviceiq.extsdk.Handler;
import io.nari.serviceiq.hwiot.HWIOTConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class CloseHandler extends Handler<ExtensionWebSocketClient> {

    static final Logger LOG = LoggerFactory.getLogger(PublishHandler.class);

    private HWIOTConnector connector;

    public CloseHandler(HWIOTConnector connector) {
        this.connector = connector;
    }

    @Override
    public void handleMessage(ExtensionWebSocketClient client) {

        LOG.info("Close handler: {}", client);
        try {
            Iterator<Consumer> it = connector.getMqsConsumers().iterator();
            while (it.hasNext()) {
                it.next().shutdown();
            }
        } catch (UmpException e) {
            LOG.error("Error in shutting down consumer.", e);
        }
        LOG.info("Stopped consumer");

    }
}
