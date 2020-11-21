package tw.momocraft.lotteryplus;

import org.bukkit.plugin.java.JavaPlugin;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;
import tw.momocraft.lotteryplus.handlers.ServerHandler;

public class LotteryPlus extends JavaPlugin {
    private static LotteryPlus instance;

    @Override
    public void onEnable() {
        instance = this;
        ConfigHandler.generateData();
        ConfigHandler.registerEvents();
        ServerHandler.sendConsoleMessage("&fhas been Enabled.");
    }

    @Override
    public void onDisable() {
        ServerHandler.sendConsoleMessage("&fhas been Disabled.");
    }

    public static LotteryPlus getInstance() {
        return instance;
    }
}