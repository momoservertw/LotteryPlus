package tw.momocraft.lotteryplus.utils;

import javafx.util.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
        Pair<PriceMap, List<LotteryMap>> lotteryMaps = ConfigHandler.getConfigPath().getLotteryProp().get(group);
        if (lotteryMaps != null) {
            if (!CorePlusAPI.getPlayerManager().hasPerm(ConfigHandler.getPluginName(), sender, "lotteryplus.bypass.lottery")) {
                UUID uuid = player.getUniqueId();
                String priceType = lotteryMaps.getKey().getPriceType();
                if (priceType == null) {
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", player);
                    CorePlusAPI.getLangManager().sendErrorMsg(ConfigHandler.getPluginName(), "If player doesn't have bypass permission, you will need to add \"Price Type\" for this lottery group.");
                    CorePlusAPI.getLangManager().sendErrorMsg(ConfigHandler.getPluginName(), "Or using command \"/ltp lottery <group> <player>\" from command.");
                    return;
                }
                double priceAmount = lotteryMaps.getKey().getPriceAmount();
                if (CorePlusAPI.getPlayerManager().getTypeBalance(uuid, priceType) < priceAmount) {
                    String[] placeHolders = CorePlusAPI.getLangManager().newString();
                    placeHolders[9] = priceType; // %pricetype%
                    placeHolders[10] = String.valueOf(priceAmount); // %price%
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.notEnoughMoney", player);
                    if (target != null) {
                        placeHolders[1] = player.getName(); // %targetplayer%
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.notEnoughMoneyTarget", sender, placeHolders);
                    }
                    return;
                }
                String[] placeHolders = CorePlusAPI.getLangManager().newString();
                placeHolders[5] = group; // %group%
                placeHolders[9] = priceType; // %pricetype%
                placeHolders[10] = String.valueOf(CorePlusAPI.getPlayerManager().takeTypeMoney(uuid, priceType, priceAmount)); // %price%
                CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgLotterySucceed(), player, placeHolders);
                if (target != null) {
                    placeHolders[1] = player.getName(); // %targetplayer%
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgLotterySucceedTarget(), player, placeHolders);
                }
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
            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgLotteryNotFound(), sender);
        }
    }
}
