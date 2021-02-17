package tw.momocraft.lotteryplus.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.coreplus.handlers.UtilsHandler;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;

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
        int length = args.length;
        if (length == 0) {
            if (CorePlusAPI.getPlayerManager().hasPerm(sender, "lotteryplus.use")) {
                commands.add("help");
            }
            if (CorePlusAPI.getPlayerManager().hasPerm(sender, "lotteryplus.command.reload")) {
                commands.add("reload");
            }
            if (CorePlusAPI.getPlayerManager().hasPerm(sender, "lotteryplus.command.version")) {
                commands.add("version");
            }
            if (CorePlusAPI.getPlayerManager().hasPerm(sender, "lotteryplus.command.lottery")) {
                commands.add("lottery");
            }
        }
        switch (args[0]) {
            case "lottery":
                if (UtilsHandler.getPlayer().hasPerm(sender, "lotteryplus.command.lottery")) {
                    if (length == 1) {
                        commands.addAll(ConfigHandler.getConfigPath().getLotteryProp().keySet());
                    } else if (length == 2) {
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
                            UtilsHandler.getLang().sendDebugTrace(tw.momocraft.coreplus.handlers.ConfigHandler.isDebugging(), ConfigHandler.getPluginPrefix(), e);
                        }
                    }
                }
                break;
        }
        StringUtil.copyPartialMatches(args[(args.length - 1)], commands, completions);
        Collections.sort(completions);
        return completions;
    }
}