package io.nari.serviceiq.hwiot;

import io.nari.serviceiq.extsdk.ExtensionWebSocketClient;

import java.util.Map;

public class HWIOTClient {

    ExtensionWebSocketClient vantiqClient = null;
    Map configurationDoc = null;

    public void connect(ExtensionWebSocketClient client, Map config)
    {
        this.vantiqClient = client;
        this.configurationDoc = config;
    }

    public void performQuery(Map parameters)
    {

    }

}
