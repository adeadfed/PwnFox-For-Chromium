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
                        value: 'blue'
                    }
                ]
            },
            condition: {
            },
        }
    ],
});