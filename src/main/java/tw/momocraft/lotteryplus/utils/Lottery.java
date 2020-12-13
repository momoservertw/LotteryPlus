package tw.momocraft.lotteryplus.utils;

import javafx.util.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;
import tw.momocraft.lotteryplus.handlers.PermissionsHandler;
import tw.momocraft.lotteryplus.handlers.ServerHandler;

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
        Pair<PriceMap, List<LotteryMap>> lotteryMaps = ConfigHandler.getConfigPath().getLotteryProp().get(group);
        if (lotteryMaps != null) {
            if (!PermissionsHandler.hasPermission(player, "lotteryplus.bypass.lottery")) {
                UUID uuid = player.getUniqueId();
                String priceType = lotteryMaps.getKey().getPriceType();
                if (priceType == null) {
                    Language.sendLangMessage("Message.noPermission", sender);
                    return;
                }
                double priceAmount = lotteryMaps.getKey().getPriceAmount();
                if (PriceAPI.getTypeBalance(uuid, priceType) < priceAmount) {
                    String[] placeHolders = Language.newString();
                    placeHolders[4] = priceType;
                    placeHolders[5] = String.valueOf(priceAmount);
                    Language.sendLangMessage("Message.notEnoughMoney", sender, placeHolders);
                    return;
                }
                String[] placeHolders = Language.newString();
                placeHolders[3] = group;
                placeHolders[4] = priceType;
                placeHolders[6] = String.valueOf(PriceAPI.takeTypeMoney(uuid, priceType, priceAmount));
                Language.sendLangMessage("Message.LotteryPlus.lotterySucceed", sender, placeHolders);
            }
            Map<List<String>, Double> rewardMap = new HashMap<>();
            Map<String, Double> chanceMap;
            List<Integer> permsList;
            String chanceGroup;
            for (LotteryMap lotteryMap : lotteryMaps.getValue()) {
                chanceMap = lotteryMap.getChanceMap();
                // Checking player reward chance for this chance-group.
                permsList = new ArrayList<>();
                for (String permGroup : chanceMap.keySet()) {
                    if (PermissionsHandler.hasPermission(player, "lotteryplus.lottery.*")
                            || PermissionsHandler.hasPermission(player, "lotteryplus.lottery." + permGroup)) {
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

                    String playerName = player.getName();
                    CustomCommands.executeMultipleCmds(player, command, true);
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
