DEFAULT_PROXY_SETTINGS = {
    scheme: 'http',
    host: 'localhost',
    port: 8080,
    bypassList: []
}

chrome.runtime.onInstalled.addListener(() => {
    applyProxySettings();
});

chrome.runtime.onStartup.addListener(() => {
    applyProxySettings();
});

chrome.runtime.onMessage.addListener((message) => {
    if (message.action === 'applyProxySettings') {
        applyProxySettings();
    }
});

function applyProxySettings() {
    chrome.storage.sync.get(['proxyConfig', 'proxyEnabled'], function (data) {
        const isEnabled = data.proxyEnabled !== false; 
        const newProxyConfig = data.proxyConfig ? data.proxyConfig : DEFAULT_PROXY_SETTINGS

        if (isEnabled) {
            const proxyConfig = {
                mode: 'fixed_servers',
                rules: {
                    singleProxy: {
                        scheme: newProxyConfig.scheme,
                        host: newProxyConfig.host,
                        port: parseInt(newProxyConfig.port)
                    },
                    bypassList: newProxyConfig.bypass
                }
            };
            chrome.proxy.settings.set(
                { value: proxyConfig, scope: 'regular' },
                () => {
                    if (chrome.runtime.lastError) {
                        console.error('Error applying proxy:', chrome.runtime.lastError);
                    } else {
                        console.log('Proxy settings applied:', proxyConfig);
                    }
                }
            );
        } else {
            chrome.proxy.settings.set(
                { value: { mode: 'direct' }, scope: 'regular' },
                () => {
                    if (chrome.runtime.lastError) {
                        console.error('Error disabling proxy:', chrome.runtime.lastError);
                    } else {
                        console.log('Proxy disabled');
                    }
                }
            );
        }
    });
}

chrome.proxy.onProxyError.addListener(function (details) {
    console.error('Proxy error:', details);
});
