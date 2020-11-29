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
            double totalChance = 0;
            Map<List<String>, Double> rewardMap = new HashMap<>();
            for (LotteryMap lotteryMap : lotteryMaps) {
                Map<String, Double> chanceMap = lotteryMap.getChanceMap();
                // Checking player reward chance for this chance-group.
                List<Integer> permsList = new ArrayList<>();
                for (String key : chanceMap.keySet()) {
                    if (PermissionsHandler.hasPermission(target, "lotteryplus.reward.*")
                            || PermissionsHandler.hasPermission(target, "lotteryplus.reward." + key)) {
                        permsList.add(Integer.parseInt(key));
                    }
                }
                // Set this chance-group's chance.
                String chanceGroup;
                if (!permsList.isEmpty()) {
                    // Get the highest group.
                    chanceGroup = Collections.max(permsList).toString();
                } else {
                    chanceGroup = "0";
                }
                rewardMap.put(lotteryMap.getList(), chanceMap.get(chanceGroup));
                totalChance += chanceMap.get(chanceGroup);
            }
            double randTotalChance = Math.random() * totalChance;
            double value;
            String command;
            for (List<String> key : rewardMap.keySet()) {
                value = rewardMap.get(key);
                // Compare the group chance with the randomly total chance.
                if (randTotalChance <= value) {
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
                randTotalChance -= value;
            }
        } else {
            Language.sendLangMessage("Message.LotteryPlus.lotteryNotFound", sender);
        }
    }
}
