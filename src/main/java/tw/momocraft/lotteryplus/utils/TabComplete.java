package tw.momocraft.lotteryplus.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;
import tw.momocraft.lotteryplus.handlers.PermissionsHandler;
import tw.momocraft.lotteryplus.handlers.ServerHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();
        final List<String> commands = new ArrayList<>();
        Collection<?> playersOnlineNew;
        Player[] playersOnlineOld;
        switch (args.length) {
            case 1:
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
                break;
            case 2:
                if (args[0].equalsIgnoreCase("lottery") && PermissionsHandler.hasPermission(sender, "lotteryplus.command.lottery")) {
                    commands.addAll(ConfigHandler.getConfigPath().getLotteryProp().keySet());
                }
                break;
            case 3:
                if (args[0].equalsIgnoreCase("lottery") && PermissionsHandler.hasPermission(sender, "lotteryplus.command.lottery")) {
                    try {
                        if (Bukkit.class.getMethod("getOnlinePlayers").getReturnType() == Collection.class) {
                            if (Bukkit.class.getMethod("getOnlinePlayers").getReturnType() == Collection.class) {
                                playersOnlineNew = ((Collection<?>) Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).invoke(null, new Object[0]));
                                for (Object objPlayer : playersOnlineNew) {
                                    commands.add(((Player) objPlayer).getName());
                                }
                            }
                        } else {
                            playersOnlineOld = ((Player[]) Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).invoke(null, new Object[0]));
                            for (Player player : playersOnlineOld) {
                                commands.add(player.getName());
                            }
                        }
                    } catch (Exception e) {
                        ServerHandler.sendDebugTrace(e);
                    }
                }
                break;
        }
        StringUtil.copyPartialMatches(args[(args.length - 1)], commands, completions);
        Collections.sort(completions);
        return completions;
    }
}