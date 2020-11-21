package tw.momocraft.lotteryplus.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;
import tw.momocraft.lotteryplus.handlers.PermissionsHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();
        final List<String> commands = new ArrayList<>();
        if (args.length == 2 && args[0].equalsIgnoreCase("lottery") && PermissionsHandler.hasPermission(sender, "lotteryplus.command.lottery")) {
            commands.addAll(ConfigHandler.getConfigPath().getLotteryProp().keySet());
        } else if (args.length == 1) {
            if (PermissionsHandler.hasPermission(sender, "lotteryplus.use")) {
                commands.add("help");
            }
            if (PermissionsHandler.hasPermission(sender, "lotteryplus.command.reload")) {
                commands.add("reload");
            }
            if (PermissionsHandler.hasPermission(sender, "lotteryplus.command.version")) {
                commands.add("version");
            }
            if (PermissionsHandler.hasPermission(sender, "lotteryplus.command.lottery")) {
                commands.add("lottery");
            }
        }
        StringUtil.copyPartialMatches(args[(args.length - 1)], commands, completions);
        Collections.sort(completions);
        return completions;
    }
}