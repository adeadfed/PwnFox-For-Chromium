package com.adeadfed;

import com.adeadfed.preferences.PwnFoxForChromiumPreferences;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;


public class PwnFoxForChromium implements BurpExtension {
    public MontoyaApi montoyaApi;
    public PwnFoxForChromiumPreferences pwnChromiumPreferences;


    @Override
    public void initialize(MontoyaApi api) {
        this.montoyaApi = api;
        montoyaApi.extension().setName("PwnFox For Chromium");

        try {
            this.pwnChromiumPreferences = new PwnFoxForChromiumPreferences(
                montoyaApi.persistence().preferences()
            );
        } catch (Exception e) {
            montoyaApi.logging().logToOutput("PwnFox For Chromium - Error loading preferences");
            montoyaApi.logging().logToError(e);
        }
        montoyaApi.logging().logToOutput("PwnFox For Chromium - Preferences loaded...");

        montoyaApi.userInterface().registerSuiteTab(
            "PwnFox For Chromium",
            new PwnFoxForChromiumUI(this).getUI()
        );
        montoyaApi.logging().logToOutput("PwnFox For Chromium - Suite Tab loaded...");
        
        montoyaApi.proxy().registerRequestHandler(
            new PwnFoxForChromiumRequestHandler()
        );
        montoyaApi.logging().logToOutput("PwnFox For Chromium - Request Interceptor loaded...");
    }
}