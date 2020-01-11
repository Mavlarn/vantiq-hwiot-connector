package io.nari.serviceiq.hwiot.handler;

import io.nari.serviceiq.extsdk.ExtensionServiceMessage;
import io.nari.serviceiq.extsdk.Handler;
import io.nari.serviceiq.hwiot.HWIOTConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PublishHandler extends Handler<ExtensionServiceMessage> {

    static final Logger LOG = LoggerFactory.getLogger(PublishHandler.class);

    public PublishHandler(HWIOTConnector connector) {
    }

    @Override
    public void handleMessage(ExtensionServiceMessage message) {
        LOG.warn("Publish NOT support!");
    }

}
