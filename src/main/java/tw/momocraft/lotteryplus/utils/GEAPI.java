package tw.momocraft.lotteryplus.utils;

import me.xanium.gemseconomy.api.GemsEconomyAPI;
import me.xanium.gemseconomy.currency.Currency;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class GEAPI {

    private GemsEconomyAPI ge;

    public GEAPI() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("GemsEconomy");
        if (plugin != null) {
            ge = new GemsEconomyAPI();
        }
    }

    public double withdraw(UUID uuid, double value, String currency) {
        ge.withdraw(uuid, value, ge.getCurrency(currency));
        return getBalance(uuid, currency);
    }

    public double deposit(UUID uuid, double value, String currency) {
        ge.deposit(uuid, value, ge.getCurrency(currency));
        return getBalance(uuid, currency);
    }

    public double getBalance(UUID uuid, String currency) {
        return ge.getBalance(uuid, ge.getCurrency(currency));
    }

    public Currency getCurrency(String currency) {
        return ge.getCurrency(currency);
    }
}
