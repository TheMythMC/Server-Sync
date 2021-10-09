package dev.themyth.server_sync.commands;

public enum Keys {
    WEBHOOK_URL("webhook_url"),
    CHANNEL_ID("channel_id"),
    MEMBERS_SEND_MESSAGES("members_send_messages"),
    TOKEN("token");

    public String id;
    Keys(String id) {
        this.id = id;
    }
}
