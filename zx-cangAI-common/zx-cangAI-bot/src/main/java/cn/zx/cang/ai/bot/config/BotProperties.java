package cn.zx.cang.ai.bot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = BotProperties.PREFIX)
public class BotProperties {

    public static final String PREFIX = "coze";
    private boolean enabled;
    private String clientId;
    private String publicKey;

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = Boolean.parseBoolean(String.valueOf(enabled));
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
