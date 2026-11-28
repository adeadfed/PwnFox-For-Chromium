package com.adeadfed;


import burp.api.montoya.core.HighlightColor;
import burp.api.montoya.proxy.websocket.ProxyWebSocketCreationHandler;
import burp.api.montoya.proxy.websocket.ProxyWebSocketCreation;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.proxy.websocket.ProxyWebSocket;


public class PwnFoxForChromiumWebSocketCreationHandler implements ProxyWebSocketCreationHandler {
    private final String PWNFOX_HEADER = "X-Pwnfox-Color";
    
    @Override
    public void handleWebSocketCreation(ProxyWebSocketCreation webSocketCreation) {
        HttpRequest upgradeRequest = webSocketCreation.upgradeRequest();
        ProxyWebSocket proxyWebSocket = webSocketCreation.proxyWebSocket();

        if (upgradeRequest.hasHeader(PWNFOX_HEADER)) {
            HighlightColor tabColor = HighlightColor.highlightColor(
                upgradeRequest.headerValue(PWNFOX_HEADER)
            );
            proxyWebSocket.registerProxyMessageHandler(
                new PwnFoxForChromiumWebSocketMessageHandler(tabColor)
            );
        }
    }
}