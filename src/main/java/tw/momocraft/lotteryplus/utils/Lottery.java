package tw.momocraft.lotteryplus.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lottery {

    public static void startLottery(CommandSender sender, Player target, String group) {
        Player player;
        if (target != null)
            player = target;
        else
            player = (Player) sender;
        List<LotteryMap> lotteryMaps = ConfigHandler.getConfigPath().getLotteryProp().get(group);
        if (lotteryMaps == null) {
            String[] langHolder = CorePlusAPI.getMsg().newString();
            langHolder[5] = group;
            CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(),
                    "groupNotFound", sender, langHolder);
            return;
        }
        Map<LotteryMap, Double> rewardMap = new HashMap<>();
        Map<String, Double> chanceMap;
        String highestChanceGroup = null;
        for (LotteryMap lotteryMap : lotteryMaps) {
            chanceMap = lotteryMap.getChanceMap();
            for (String chanceGroup : chanceMap.keySet()) {
                if (CorePlusAPI.getPlayer().hasPerm(player, "lotteryplus.lottery.chancegroup.*") ||
                        CorePlusAPI.getPlayer().hasPerm(player, "lotteryplus.lottery.chancegroup." + chanceGroup)) {
                    if (chanceMap.get(chanceGroup) == null)
                        continue;
                    highestChanceGroup = chanceGroup;
                    break;
                }
            }
            if (highestChanceGroup == null) {
                highestChanceGroup = "default";
                if (chanceMap.get(highestChanceGroup) == null) {
                    CorePlusAPI.getMsg().sendErrorMsg(ConfigHandler.getPluginName(),
                            "Can not find the default reward group for \"" + group + "\"");
                    return;
                }
            }
            rewardMap.put(lotteryMap, chanceMap.get(highestChanceGroup));
        }
        // Getting total chance.
        double totalChance = 0;
        for (double chance : rewardMap.values())
            totalChance += chance;
        double randomChance = Math.random() * totalChance;
        double chance;
        for (LotteryMap lotteryMap : rewardMap.keySet()) {
            chance = rewardMap.get(lotteryMap);
            // Compare the group chance with the randomly total chance.
            if (randomChance <= chance) {
                // Getting the random string of command list.
                String playerName = player.getName();
                String command = CorePlusAPI.getUtils().getRandomString(lotteryMap.getCommands());
                command = CorePlusAPI.getMsg().transHolder(ConfigHandler.getPluginName(), player, command);
                CorePlusAPI.getCmd().sendCmd(ConfigHandler.getPluginName(), player, command);
                if (ConfigHandler.getConfigPath().isLotteryLog())
                    CorePlusAPI.getFile().getLog().add(ConfigHandler.getPluginName(), "lotteryplus", playerName + " - " + command);
                CorePlusAPI.getMsg().sendDetailMsg(ConfigHandler.isDebug(), ConfigHandler.getPluginName(),
                        "Lottery", playerName, "final", "succeed", group + ": " + command,
                        new Throwable().getStackTrace()[0]);
                return;
            }
            randomChance -= chance;
        }
    }
}
