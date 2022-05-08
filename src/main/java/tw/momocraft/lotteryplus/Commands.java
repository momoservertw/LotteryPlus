package tw.momocraft.lotteryplus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;
import tw.momocraft.lotteryplus.utils.Lottery;


public class Commands implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, Command c, String l, String[] args) {
        int length = args.length;
        if (length == 0) {
            if (CorePlusAPI.getPlayer().hasPerm(sender, "lotteryplus.use")) {
                CorePlusAPI.getMsg().sendMsg("", sender, "");
                CorePlusAPI.getMsg().sendLangMsg("", ConfigHandler.getConfigPath().getMsgTitle(), sender);
                CorePlusAPI.getMsg().sendMsg("", sender,
                        "&f " + LotteryPlus.getInstance().getDescription().getName()
                                + " &ev" + LotteryPlus.getInstance().getDescription().getVersion() + "  &8by Momocraft");
                CorePlusAPI.getMsg().sendLangMsg("",
                        ConfigHandler.getConfigPath().getMsgHelp(), sender);
                CorePlusAPI.getMsg().sendMsg("", sender, "");
            } else {
                CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(),
                        "Message.noPermission", sender);
            }
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "help":
                if (CorePlusAPI.getPlayer().hasPerm(sender, "lotteryplus.use")) {
                    CorePlusAPI.getMsg().sendMsg("", sender, "");
                    CorePlusAPI.getMsg().sendLangMsg("", ConfigHandler.getConfigPath().getMsgTitle(), sender);
                    CorePlusAPI.getMsg().sendMsg("", sender,
                            "&f " + LotteryPlus.getInstance().getDescription().getName()
                                    + " &ev" + LotteryPlus.getInstance().getDescription().getVersion() + "  &8by Momocraft");
                    CorePlusAPI.getMsg().sendLangMsg("", ConfigHandler.getConfigPath().getMsgHelp(), sender);
                    if (CorePlusAPI.getPlayer().hasPerm(sender, "lotteryplus.command.reload"))
                        CorePlusAPI.getMsg().sendLangMsg("", ConfigHandler.getConfigPath().getMsgReload(), sender);
                    if (CorePlusAPI.getPlayer().hasPerm(sender, "lotteryplus.command.version"))
                        CorePlusAPI.getMsg().sendLangMsg("", ConfigHandler.getConfigPath().getMsgVersion(), sender);
                    if (CorePlusAPI.getPlayer().hasPerm(sender, "lotteryplus.command.lottery"))
                        CorePlusAPI.getMsg().sendLangMsg("", ConfigHandler.getConfigPath().getMsgCmdLottery(), sender);
                    CorePlusAPI.getMsg().sendMsg("", sender, "");
                } else {
                    CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(),
                            "Message.noPermission", sender);
                }
                return true;
            case "reload":
                if (CorePlusAPI.getPlayer().hasPerm(sender, "lotteryplus.command.reload")) {
                    ConfigHandler.generateData(true);
                    CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(),
                            "Message.configReload", sender);
                } else {
                    CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(),
                            "Message.noPermission", sender);
                }
                return true;
            case "version":
                if (CorePlusAPI.getPlayer().hasPerm(sender, "lotteryplus.command.version")) {
                    CorePlusAPI.getMsg().sendMsg(ConfigHandler.getPrefix(), sender,
                            "&f " + LotteryPlus.getInstance().getDescription().getName()
                                    + " &ev" + LotteryPlus.getInstance().getDescription().getVersion() + "  &8by Momocraft");
                    CorePlusAPI.getUpdate().check(ConfigHandler.getPluginName(), ConfigHandler.getPrefix(), sender,
                            LotteryPlus.getInstance().getName(), LotteryPlus.getInstance().getDescription().getVersion(), true);
                } else {
                    CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(),
                            "Message.noPermission", sender);
                }
                return true;
            case "lottery":
                if (CorePlusAPI.getPlayer().hasPerm(sender, "lotteryplus.command.lottery")) {
                    if (!ConfigHandler.getConfigPath().isLottery()) {
                        CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(),
                                "Message.featureDisabled", sender);
                        return true;
                    }
                    // lotteryplus lottery <group>
                    if (length == 2) {
                        Lottery.startLottery(sender, null, args[1]);
                        return true;
                        // lotteryplus lottery <group> <player>
                    } else if (length == 3) {
                        Player player = CorePlusAPI.getPlayer().getPlayer(args[2]);
                        if (player == null) {
                            String[] placeHolders = CorePlusAPI.getMsg().newString();
                            placeHolders[1] = args[2]; // %targetplayer%
                            CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(),
                                    "Message.targetNotFound", sender, placeHolders);
                            return true;
                        }
                        Lottery.startLottery(sender, player, args[1]);
                    }
                } else {
                    CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(),
                            "Message.noPermission", sender);
                }
                return true;
        }
        CorePlusAPI.getMsg().sendLangMsg(ConfigHandler.getPrefix(),
                "Message.unknownCommand", sender);
        return true;
    }
}