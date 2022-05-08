package tw.momocraft.lotteryplus.handlers;

import org.bukkit.Bukkit;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.lotteryplus.Commands;
import tw.momocraft.lotteryplus.LotteryPlus;
import tw.momocraft.lotteryplus.listeners.BlockDropItem;
import tw.momocraft.lotteryplus.utils.TabComplete;

public class DependHandler {

    public void setup(boolean reload) {
        registerEvents();
        if (!reload)
            checkUpdate();
    }

    public void checkUpdate() {
        if (!ConfigHandler.isCheckUpdates())
            return;
        CorePlusAPI.getUpdate().check(ConfigHandler.getPluginName(),
                ConfigHandler.getPluginPrefix(), Bukkit.getConsoleSender(),
                LotteryPlus.getInstance().getDescription().getName(),
                LotteryPlus.getInstance().getDescription().getVersion(), true);
    }

    private void registerEvents() {
        LotteryPlus.getInstance().getCommand("LotteryPlus").setExecutor(new Commands());
        LotteryPlus.getInstance().getCommand("LotteryPlus").setTabCompleter(new TabComplete());

        LotteryPlus.getInstance().getServer().getPluginManager().registerEvents(new BlockDropItem(), LotteryPlus.getInstance());
    }
}
