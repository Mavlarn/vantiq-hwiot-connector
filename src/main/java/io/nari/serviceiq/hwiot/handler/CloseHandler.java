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

    static final Logger LOG = LoggerFactory.getLogger(CloseHandler.class);

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

        // reconnect
        int CONNECTOR_CONNECT_TIMEOUT = 10;
        int RECONNECT_INTERVAL = 5000;
        boolean sourcesSucceeded = false;
        while (!sourcesSucceeded) {
            client.initiateFullConnection(connector.vantiqUrl, connector.vantiqToken);
            sourcesSucceeded = connector.checkConnectionFails(client, CONNECTOR_CONNECT_TIMEOUT);
            if (!sourcesSucceeded) {
                try {
                    Thread.sleep(RECONNECT_INTERVAL);
                } catch (InterruptedException e) {
                    LOG.error("An error occurred when trying to sleep the current thread. Error Message: ", e);
                }
            }
        }

    }
}
