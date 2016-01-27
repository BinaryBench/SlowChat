package me.binarybench.slowchat.commands;

import me.binarybench.slowchat.SlowChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Bench on 1/27/2016.
 */
public class MuteCommand implements CommandExecutor {

    private SlowChat plugin;

    public MuteCommand(SlowChat plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        long chatSlow = plugin.getChatSlow();
        String returnMessage;

        if (chatSlow < 0) // if it's already muted
        {
            returnMessage = plugin.setChatSlow(0);
        }
        else
        {
            returnMessage = plugin.setChatSlow(-1);
        }
        if (returnMessage != null)
            commandSender.sendMessage(ChatColor.RED + returnMessage);
        return true;
    }
}
