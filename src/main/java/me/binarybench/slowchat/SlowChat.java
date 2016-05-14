package me.binarybench.slowchat;

import me.binarybench.slowchat.commands.MuteCommand;
import me.binarybench.slowchat.commands.SlowCommand;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Bench on 1/27/2016.
 */
public class SlowChat extends JavaPlugin implements Listener {

    public final String bypassPerm = "chatslow.bypass";
    public volatile long chatSlow = 0;


    public String muteMessage;
    public String muteStartBroadcast;
    public String muteStopBroadcast;

    public String slowMessage;
    public String slowStartBroadcast;
    public String slowStopBroadcast;


    public synchronized String setChatSlow(long input) {

        if (input < 0) // Trying to mute
        {
            if (this.chatSlow < 0) // Already muted
            {
                return "Chat is already muted";
            }
            else // Muting chat!
            {
                this.chatSlow = -1;
                safeBroadcast(getMuteStartBroadcast());
            }
        }
        else if (input > 0) //Trying to slow!
        {
            // (we don't care if it's already slowed)

            // Slowing Chat!
            this.chatSlow = input;
            safeBroadcast(getSlowStartBroadcast().replace("<time>", format(input)));
        }
        else // Trying to set to normal!
        {
            if (this.chatSlow > 0) // Was slowed
            {
                this.chatSlow = 0;
                safeBroadcast(getSlowStopBroadcast().replace("<time>", format(input)));
            }
            else if (chatSlow < 0) // Was muted
            {
                this.chatSlow = 0;
                safeBroadcast(getMuteStopBroadcast());
            }
            else // Already normal
            {
                return "Chat Slow/Mute is already disabled!";
            }
        }
        return null;
    }

    public synchronized long getChatSlow()
    {
        return this.chatSlow;
    }

    public ConcurrentHashMap<UUID, Long> chatTimes = new ConcurrentHashMap<UUID, Long>();

    @Override
    public void onEnable()
    {
        getLogger().info("---------------------------------");
        getLogger().info("Created by: BinaryBench");
        getLogger().info("Follow me on twitter @BinaryBench");
        getLogger().info("---------------------------------");

        getServer().getPluginManager().registerEvents(this, this);

        getConfig().options().copyDefaults(true);
        saveConfig();

        this.muteMessage = loadMessage("mute-message");
        this.muteStartBroadcast = loadMessage("mute-start-broadcast");
        this.muteStopBroadcast = loadMessage("mute-stop-broadcast");

        this.slowMessage = loadMessage("slow-message");
        this.slowStartBroadcast = loadMessage("slow-start-broadcast");
        this.slowStopBroadcast = loadMessage("slow-stop-broadcast");

        this.getCommand("mutechat").setExecutor(new MuteCommand(this));
        this.getCommand("slowchat").setExecutor(new SlowCommand(this));
    }
    private String loadMessage(String path)
    {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(path));
    }

    @Override
    public void onDisable()
    {

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event)
    {
        long chatSlow = getChatSlow();

        Player p = event.getPlayer();

        if (p.hasPermission(this.bypassPerm))
            return;

        if (chatSlow > 0) //greater then zero -> slowed
        {
            UUID u = p.getUniqueId();


            long lastChat = this.chatTimes.getOrDefault(u, 0L);  //Uses java 1.8

            long currentTime = System.currentTimeMillis();

            long timeTill = lastChat + chatSlow - currentTime;

            if (timeTill > 0)
            {
                event.setCancelled(true);
                p.sendMessage(getSlowMessage().replace("<time>", format(timeTill + 999)));
            }
            else
            {
                this.chatTimes.put(u, currentTime);
            }
        }
        else if (chatSlow < 0) //less then zero -> muted
        {
            event.setCancelled(true);
            p.sendMessage(getMuteMessage());
        }
        //equals zero -> normal
    }
    
    public void safeBroadcast(String message)
    {
        if (message.isEmpty())
            return;
        getServer().broadcastMessage(message);
    }
    
    public static String format(long time)
    {
        return DurationFormatUtils.formatDurationWords(time, true, true);
    }


    public synchronized String getMuteMessage()
    {
        return muteMessage;
    }

    public synchronized String getMuteStartBroadcast() {
        return muteStartBroadcast;
    }

    public synchronized String getMuteStopBroadcast() {
        return muteStopBroadcast;
    }

    public synchronized String getSlowMessage()
    {
        return slowMessage;
    }

    public synchronized String getSlowStartBroadcast() {
        return slowStartBroadcast;
    }

    public synchronized String getSlowStopBroadcast() {
        return slowStopBroadcast;
    }
}
