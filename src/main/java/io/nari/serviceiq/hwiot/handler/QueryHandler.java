package io.nari.serviceiq.hwiot.handler;

import io.nari.serviceiq.extsdk.ExtensionServiceMessage;
import io.nari.serviceiq.extsdk.Handler;
import io.nari.serviceiq.hwiot.HWIOTConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryHandler extends Handler<ExtensionServiceMessage> {

    static final Logger LOG = LoggerFactory.getLogger(PublishHandler.class);

    private HWIOTConnector extension;

    public QueryHandler(HWIOTConnector extension) {
        this.extension = extension;
    }

    @Override
    public void handleMessage(ExtensionServiceMessage msg) {
        LOG.warn("Query NOT support!");
    }
}
