package com.adeadfed;


import burp.api.montoya.core.HighlightColor;
import burp.api.montoya.proxy.http.InterceptedRequest;
import burp.api.montoya.proxy.http.ProxyRequestHandler;
import burp.api.montoya.proxy.http.ProxyRequestReceivedAction;
import burp.api.montoya.proxy.http.ProxyRequestToBeSentAction;

public class PwnFoxForChromiumRequestHandler implements ProxyRequestHandler {
    private final String PWNFOX_HEADER = "X-Pwnfox-Color";
    
    @Override
    public ProxyRequestReceivedAction handleRequestReceived(InterceptedRequest interceptedRequest) {
        if (interceptedRequest.hasHeader(PWNFOX_HEADER)) {
            String tabColor = interceptedRequest.headerValue(PWNFOX_HEADER);
            interceptedRequest.annotations().setHighlightColor(
                    HighlightColor.highlightColor(tabColor)
            );
        }
        return null;
    }

    @Override
    public ProxyRequestToBeSentAction handleRequestToBeSent(InterceptedRequest interceptedRequest) {
        return null;
    }
}
