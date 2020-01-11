package io.nari.serviceiq.hwiot;

import java.io.IOException;
import java.util.Map;

import static io.nari.serviceiq.hwiot.ConnectorConstants.VANTIQ_SOURCE_NAME;


public class HWIOTConnectorMain extends AbstractConnectorMain {

    public static void main(String[] argv) throws IOException {
        Map<String, String> connectInfo = constructConfig();
        HWIOTConnector iotConnector = new HWIOTConnector(connectInfo.get(VANTIQ_SOURCE_NAME), connectInfo);
        iotConnector.start();
    }
}
