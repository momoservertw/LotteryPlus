package tw.momocraft.lotteryplus.handlers;

import tw.momocraft.lotteryplus.LotteryPlus;
import tw.momocraft.lotteryplus.Commands;
import tw.momocraft.lotteryplus.listeners.BlockDropItem;
import tw.momocraft.lotteryplus.utils.TabComplete;


public class RegisterHandler {

    public static void registerEvents() {
        LotteryPlus.getInstance().getCommand("LotteryPlus").setExecutor(new Commands());
        LotteryPlus.getInstance().getCommand("LotteryPlus").setTabCompleter(new TabComplete());

        LotteryPlus.getInstance().getServer().getPluginManager().registerEvents(new BlockDropItem(), LotteryPlus.getInstance());
        ServerHandler.sendFeatureMessage("Register-Event", "Lottery", "BlockBreak", "continue",
                new Throwable().getStackTrace()[0]);
        /*
        if (ConfigHandler.getDepends().MyPetEnabled()) {
            LotteryPlus.getInstance().getServer().getPluginManager().registerEvents(new MyPet(), ServerPlus.getInstance());
            ServerHandler.sendFeatureMessage("Register-Event", "MyPet", "MyPet", "continue",
                    new Throwable().getStackTrace()[0]);
        }
         */
    }
}
