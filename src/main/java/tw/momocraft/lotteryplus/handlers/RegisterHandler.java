package tw.momocraft.lotteryplus.handlers;

import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.lotteryplus.LotteryPlus;
import tw.momocraft.lotteryplus.Commands;
import tw.momocraft.lotteryplus.listeners.BlockDropItem;
import tw.momocraft.lotteryplus.utils.TabComplete;


public class RegisterHandler {

    public static void registerEvents() {
        LotteryPlus.getInstance().getCommand("LotteryPlus").setExecutor(new Commands());
        LotteryPlus.getInstance().getCommand("LotteryPlus").setTabCompleter(new TabComplete());

        LotteryPlus.getInstance().getServer().getPluginManager().registerEvents(new BlockDropItem(), LotteryPlus.getInstance());
        CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.getPrefix(), "Register-Event", "Lottery", "BlockBreak", "continue",
                new Throwable().getStackTrace()[0]);
    }
}
