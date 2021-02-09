package tw.momocraft.lotteryplus.utils;

import javafx.util.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tw.momocraft.coreplus.CorePlus;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.coreplus.utils.economy.PriceMap;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;

import java.util.*;

public class Lottery {

    public static void startLottery(CommandSender sender, Player target, String group) {
        Player player;
        if (target != null) {
            player = target;
        } else {
            player = (Player) sender;
        }
        // Get the group's property.
        List<LotteryMap> lotteryMaps = ConfigHandler.getConfigPath().getLotteryProp().get(group);
        if (lotteryMaps != null) {
            Map<List<String>, Double> rewardMap = new HashMap<>();
            Map<String, Double> chanceMap;
            List<Integer> permsList;
            String chanceGroup;
            for (LotteryMap lotteryMap : lotteryMaps) {
                chanceMap = lotteryMap.getChanceMap();
                // Checking player reward chance for this chance-group.
                permsList = new ArrayList<>();
                for (String permGroup : chanceMap.keySet()) {
                    if (CorePlusAPI.getPlayerManager().hasPerm(ConfigHandler.getPluginName(), player, "lotteryplus.lottery." + permGroup)) {
                        permsList.add(Integer.parseInt(permGroup));
                    }
                }
                // Set this chance-group's chance.
                if (!permsList.isEmpty()) {
                    // Get the highest group.
                    chanceGroup = Collections.max(permsList).toString();
                } else {
                    chanceGroup = "0";
                }
                rewardMap.put(lotteryMap.getList(), chanceMap.get(chanceGroup));
            }
            // Get to total chance.
            double totalChance = 0;
            for (Double chance : rewardMap.values()) {
                totalChance += chance;
            }
            double randTotalChance = Math.random() * totalChance;
            double chance;
            String command;
            for (List<String> key : rewardMap.keySet()) {
                chance = rewardMap.get(key);
                // Compare the group chance with the randomly total chance.
                if (randTotalChance <= chance) {
                    // Random execute a reward command from that group.
                    command = CorePlusAPI.getUtilsManager().getRandomString(key);

                    String playerName = player.getName();
                    CorePlusAPI.getCommandManager().executeCmd(ConfigHandler.getPrefix(), player, command, true);
                    if (ConfigHandler.getConfigPath().isLotteryLog()) {
                        CorePlusAPI.getCommandManager().dispatchLogCustomCmd(ConfigHandler.getPrefix(), "LotteryPlus, playerName  - " + command);
                    }
                    CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPlugin(), "Lottery", playerName, "execute", "return", group + " " + command,
                            new Throwable().getStackTrace()[0]);
                    return;
                }
                randTotalChance -= chance;
            }
        } else {
            String[] langHolder = CorePlusAPI.getLangManager().newString();
            langHolder[5] = group;
            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "groupNotFound", sender, langHolder);
        }
    }
}
