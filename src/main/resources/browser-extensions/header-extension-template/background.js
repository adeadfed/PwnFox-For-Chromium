chrome.declarativeNetRequest.updateDynamicRules({
    removeRuleIds: [1],
    addRules: [
        {
            id: 1,
            priority: 1,
            action: {
                type: 'modifyHeaders',
                requestHeaders: [
                    {
                        operation: 'set',
                        header: 'x-pwnfox-color',
                        value: '{{colorname}}'
                    }
                ]
            },
            condition: {
                urlFilter: "*",
                resourceTypes: [
                    "main_frame",
                    "sub_frame",
                    "stylesheet",
                    "script",
                    "image",
                    "font",
                    "object",
                    "xmlhttprequest",
                    "ping",
                    "csp_report",
                    "media",
                    "websocket",
                    "webtransport",
                    "webbundle",
                    "other"
                ]
            },
        }
    ],
});