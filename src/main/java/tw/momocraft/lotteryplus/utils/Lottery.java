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
                if (CorePlusAPI.getPlayer().hasPerm(player, "lotteryplus.lottery.group." + chanceGroup)) {
                    if (chanceMap.containsKey(chanceGroup)) {
                        highestChanceGroup = chanceGroup;
                        break;
                    }
                }
            }
            if (highestChanceGroup == null)
                highestChanceGroup = "default";
            rewardMap.put(lotteryMap, chanceMap.get(highestChanceGroup));
        }
        // Getting total chance.
        double totalChance = 0;
        for (double chance : rewardMap.values())
            totalChance += chance;
        double randomChance = Math.random() * totalChance;
        String command;
        double chance;
        for (LotteryMap lotteryMap : rewardMap.keySet()) {
            chance = rewardMap.get(lotteryMap);
            // Compare the group chance with the randomly total chance.
            if (randomChance <= chance) {
                // Getting the random string of command list.
                command = CorePlusAPI.getUtils().getRandomString(lotteryMap.getCommands());
                CorePlusAPI.getCmd().sendCmd(ConfigHandler.getPlugin(), player, player, command);
                String playerName = player.getName();
                if (ConfigHandler.getConfigPath().isLotteryLog())
                    CorePlusAPI.getCmd().dispatchLogGroup(ConfigHandler.getPlugin(), "LotteryPlus, " + playerName + " - " + command);
                CorePlusAPI.getMsg().sendDetailMsg(ConfigHandler.isDebug(), ConfigHandler.getPluginPrefix(),
                        "Lottery", playerName, "Final", "succeed", group + ": " + command,
                        new Throwable().getStackTrace()[0]);
                return;
            }
            randomChance -= chance;
        }
    }
}
