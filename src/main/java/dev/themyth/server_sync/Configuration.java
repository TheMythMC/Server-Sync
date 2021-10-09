package dev.themyth.server_sync;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "server-sync")
public class Configuration implements ConfigData {
    @Comment(value = "Channel Webhook URL")
    public String webhookURL = "";
    @Comment(value = "Bot token")
    public String token = "";
    @Comment(value = "ID of the channel that the bridge is")
    public long channelID = 0;
    @Comment(value = "Allows members to interact with the server through the bridge channel")
    public boolean membersSendMessages = true;
}
