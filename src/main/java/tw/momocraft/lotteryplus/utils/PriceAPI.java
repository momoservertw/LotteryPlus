package tw.momocraft.lotteryplus.utils;

import org.bukkit.Bukkit;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;
import tw.momocraft.lotteryplus.handlers.ServerHandler;

import java.util.UUID;

public class PriceAPI {

    public static double getTypeBalance(UUID uuid, String priceType) {
        switch (priceType) {
            case "money":
                if (ConfigHandler.getDepends().VaultEnabled() && ConfigHandler.getDepends().getVaultApi().getEconomy() != null) {
                    return ConfigHandler.getDepends().getVaultApi().getEconomy().getBalance(Bukkit.getOfflinePlayer(uuid));
                }
                break;
            case "points":
                if (ConfigHandler.getDepends().PlayerPointsEnabled()) {
                    return ConfigHandler.getDepends().getPlayerPointsApi().getBalance(uuid);
                }
                break;
            default:
                if (ConfigHandler.getDepends().GemsEconomyEnabled()) {
                    if (ConfigHandler.getDepends().getGemsEconomyApi().getCurrency(priceType) != null) {
                        return ConfigHandler.getDepends().getGemsEconomyApi().getBalance(uuid, priceType);
                    }
                }
                break;
        }
        ServerHandler.sendErrorMessage("Can not find price type: " + priceType);
        return 0;
    }

    public static double takeTypeMoney(UUID uuid, String priceType, double amount) {
        switch (priceType) {
            case "money":
                if (ConfigHandler.getDepends().VaultEnabled() && ConfigHandler.getDepends().getVaultApi().getEconomy() != null) {
                    ConfigHandler.getDepends().getVaultApi().getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(uuid), amount);
                    return ConfigHandler.getDepends().getVaultApi().getBalance(uuid);
                }
                break;
            case "points":
                if (ConfigHandler.getDepends().PlayerPointsEnabled()) {
                    return ConfigHandler.getDepends().getPlayerPointsApi().takePoints(uuid, amount);
                }
                break;
            default:
                if (ConfigHandler.getDepends().GemsEconomyEnabled()) {
                    if (ConfigHandler.getDepends().getGemsEconomyApi().getCurrency(priceType) != null) {
                        return ConfigHandler.getDepends().getGemsEconomyApi().withdraw(uuid, amount, priceType);
                    }
                }
                break;
        }
        ServerHandler.sendErrorMessage("Can not find price type: " + priceType);
        return 0;
    }
}
