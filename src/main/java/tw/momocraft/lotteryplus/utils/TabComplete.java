package tw.momocraft.lotteryplus.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.coreplus.handlers.UtilsHandler;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();
        final List<String> commands = new ArrayList<>();
        int length = args.length;
        if (length == 0) {
            if (CorePlusAPI.getPlayer().hasPerm(sender, "lotteryplus.use"))
                commands.add("help");
            if (CorePlusAPI.getPlayer().hasPerm(sender, "lotteryplus.command.reload"))
                commands.add("reload");
            if (CorePlusAPI.getPlayer().hasPerm(sender, "lotteryplus.command.version"))
                commands.add("version");
            if (CorePlusAPI.getPlayer().hasPerm(sender, "lotteryplus.command.lottery"))
                commands.add("lottery");
        }
        switch (args[0]) {
            case "lottery":
                if (UtilsHandler.getPlayer().hasPerm(sender, "lotteryplus.command.lottery")) {
                    if (length == 1) {
                        commands.addAll(ConfigHandler.getConfigPath().getLotteryProp().keySet());
                    } else if (length == 2) {
                        commands.addAll(CorePlusAPI.getPlayer().getOnlinePlayerNames());
                    }
                }
                break;
        }
        StringUtil.copyPartialMatches(args[(args.length - 1)], commands, completions);
        Collections.sort(completions);
        return completions;
    }
}