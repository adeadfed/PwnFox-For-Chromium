document.addEventListener('DOMContentLoaded', function () {  
    chrome.storage.sync.get(['proxyConfig', 'proxyEnabled'], function (data) {
        if (data.proxyConfig) {
            document.getElementById('scheme').value = data.proxyConfig.scheme;
            document.getElementById('host').value = data.proxyConfig.host;
            document.getElementById('port').value = data.proxyConfig.port;
            document.getElementById('bypass').value = data.proxyConfig.bypass.join(',');
        }
        if (data.proxyEnabled === false) {
            document.getElementById('toggleButton').textContent = 'Enable Proxy';
            document.getElementById('toggleButton').style.backgroundColor = '#4CAF50';
        } else {
            document.getElementById('toggleButton').textContent = 'Disable Proxy';
            document.getElementById('toggleButton').style.backgroundColor = '#f44336';
        }
    });

    document.getElementById('proxyForm').addEventListener('submit', function (e) {
        e.preventDefault();

        const scheme = document.getElementById('scheme').value;
        const host = document.getElementById('host').value;
        const port = document.getElementById('port').value;
        const bypass = document.getElementById('bypass').value.split(',');

        const proxyConfig = {
            scheme: scheme,
            host: host,
            port: port,
            bypass: bypass
        };
        chrome.storage.sync.set({ proxyConfig: proxyConfig }, function () {
            alert('Proxy settings saved!');
        });
    });

    document.getElementById('toggleButton').addEventListener('click', function () {
        chrome.storage.sync.get('proxyEnabled', function (data) {
            const isEnabled = data.proxyEnabled !== false; 
            const newState = !isEnabled;
            chrome.storage.sync.set({ proxyEnabled: newState }, function () {
                if (newState) {
                    document.getElementById('toggleButton').textContent = 'Disable Proxy';
                    document.getElementById('toggleButton').style.backgroundColor = '#f44336';
                } else {
                    document.getElementById('toggleButton').textContent = 'Enable Proxy';
                    document.getElementById('toggleButton').style.backgroundColor = '#4CAF50';
                }
                applyProxySettings();
            });
        });
    });

    function applyProxySettings() {
        chrome.runtime.sendMessage({ action: 'applyProxySettings' });
    }
});
