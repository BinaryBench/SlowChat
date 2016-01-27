package me.binarybench.slowchat.commands;

import me.binarybench.slowchat.SlowChat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Bench on 1/27/2016.
 */
public class SlowCommand implements CommandExecutor {

    private SlowChat plugin;

    public SlowCommand(SlowChat plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {

        long chatSlow = plugin.getChatSlow();

        int input = 0;

        if (args.length >= 1)
        {
            try
            {
                input = (int) (Float.parseFloat(args[0]) * 1000);
            }
            catch (NumberFormatException e)
            {
                commandSender.sendMessage(ChatColor.RED + "'" + args[0] + "' is not a number!");
                return true;
            }
        }

        String returnMessage = plugin.setChatSlow(input);
        if (returnMessage != null)
            commandSender.sendMessage(ChatColor.RED + returnMessage);
        return true;
    }
}
