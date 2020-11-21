package tw.momocraft.lotteryplus.utils;

import org.bukkit.command.CommandSender;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;
import tw.momocraft.lotteryplus.handlers.ServerHandler;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Lottery {

    public static void startLottery(CommandSender sender, String group) {
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
                    command = key.get(new Random().nextInt(key.size()));
                    CustomCommands.executeMultipleCmds(sender, command);
                    ServerHandler.sendFeatureMessage("Lottery", group, "execute", "continue", command,
                            new Throwable().getStackTrace()[0]);
                    return;
                }
                randTotalChance -= value;
            }
        } else {
            ServerHandler.sendConsoleMessage("Can not find this type: " + group);
        }
    }
}
