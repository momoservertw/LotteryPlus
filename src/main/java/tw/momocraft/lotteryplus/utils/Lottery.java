package tw.momocraft.lotteryplus.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;
import tw.momocraft.lotteryplus.handlers.ServerHandler;

import java.util.List;
import java.util.Map;

public class Lottery {

    public static void startLottery(CommandSender sender, Player player, String group) {
        // Get the group's property.
        Map<List<String>, Double> lotteryProp = ConfigHandler.getConfigPath().getLotteryProp().get(group);
        if (lotteryProp != null) {
            // Get to total Chance.
            double totalChance = 0;
            for (List<String> key : lotteryProp.keySet()) {
                totalChance += lotteryProp.get(key);
            }
            double randTotalChance = Math.random() * totalChance;
            double value;
            String command;
            for (List<String> key : lotteryProp.keySet()) {
                value = lotteryProp.get(key);
                // Compare the group chance with the randomly total chance.
                if (randTotalChance <= value) {
                    // Random execute a reward command from that group.
                    command = Utils.getRandomString(key);
                    if (player != null) {
                        String playerName = player.getName();
                        CustomCommands.executeMultipleCmds(player, command, true);
                        if (ConfigHandler.getConfigPath().isLotteryLog()) {
                            ConfigHandler.getLogger().addLotteryLog(playerName + " - " + command, true);
                        }
                        ServerHandler.sendFeatureMessage("Lottery", playerName, "execute", "return", group + " " + command,
                                new Throwable().getStackTrace()[0]);
                    } else {
                        String senderName = sender.getName();
                        CustomCommands.executeMultipleCmds((Player) sender, command, true);
                        if (ConfigHandler.getConfigPath().isLotteryLog()) {
                            ConfigHandler.getLogger().addLotteryLog(senderName + " - " + command, true);
                        }
                        ServerHandler.sendFeatureMessage("Lottery", senderName, "execute", "return", group + " " + command,
                                new Throwable().getStackTrace()[0]);
                    }
                    return;
                }
                randTotalChance -= value;
            }
        } else {
            Language.sendLangMessage("Message.LotteryPlus.lotteryNotFound", sender);
        }
    }
}
