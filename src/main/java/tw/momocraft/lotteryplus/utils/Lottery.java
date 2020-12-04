package tw.momocraft.lotteryplus.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;
import tw.momocraft.lotteryplus.handlers.PermissionsHandler;
import tw.momocraft.lotteryplus.handlers.ServerHandler;

import java.util.*;

public class Lottery {

    public static void startLottery(CommandSender sender, Player player, String group) {
        Player target;
        if (player != null) {
            target = player;
        } else {
            target = (Player) sender;
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
                    if (PermissionsHandler.hasPermission(target, "lotteryplus.lottery.*")
                            || PermissionsHandler.hasPermission(target, "lotteryplus.lottery." + permGroup)) {
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
                    command = Utils.getRandomString(key);

                    String playerName = target.getName();
                    CustomCommands.executeMultipleCmds(target, command, true);
                    if (ConfigHandler.getConfigPath().isLotteryLog()) {
                        ConfigHandler.getLogger().addLotteryLog(playerName + " - " + command, true);
                    }
                    ServerHandler.sendFeatureMessage("Lottery", playerName, "execute", "return", group + " " + command,
                            new Throwable().getStackTrace()[0]);
                    return;
                }
                randTotalChance -= chance;
            }
        } else {
            Language.sendLangMessage("Message.LotteryPlus.lotteryNotFound", sender);
        }
    }
}
