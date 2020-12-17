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
        switch (args.length) {
            case 0:
                if (CorePlusAPI.getPermManager().hasPermission(sender, "lotteryplus.use")) {
                    CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "");
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgTitle(), sender);
                    CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "&f " + LotteryPlus.getInstance().getDescription().getName()
                            + " &ev" + LotteryPlus.getInstance().getDescription().getVersion() + "  &8by Momocraft");
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgHelp(), sender);
                    CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "");
                } else {
                    CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                }
                return true;
            case 1:
                if (args[0].equalsIgnoreCase("help")) {
                    if (CorePlusAPI.getPermManager().hasPermission(sender, "lotteryplus.use")) {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgTitle(), sender);
                        CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "&f " + LotteryPlus.getInstance().getDescription().getName()
                                + " &ev" + LotteryPlus.getInstance().getDescription().getVersion() + "  &8by Momocraft");
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgHelp(), sender);
                        if (CorePlusAPI.getPermManager().hasPermission(sender, "lotteryplus.command.reload")) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgReload(), sender);
                        }
                        if (CorePlusAPI.getPermManager().hasPermission(sender, "lotteryplus.command.version")) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgVersion(), sender);
                        }
                        if (CorePlusAPI.getPermManager().hasPermission(sender, "lotteryplus.command.lottery")) {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), ConfigHandler.getConfigPath().getMsgLottery(), sender);
                        }
                        CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "");
                    } else {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (CorePlusAPI.getPermManager().hasPermission(sender, "lotteryplus.command.reload")) {
                        ConfigHandler.generateData(true);
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.configReload", sender);
                    } else {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("version")) {
                    if (CorePlusAPI.getPermManager().hasPermission(sender, "lotteryplus.command.version")) {
                        CorePlusAPI.getLangManager().sendMsg(ConfigHandler.getPrefix(), sender, "&f " + LotteryPlus.getInstance().getDescription().getName()
                                + " &ev" + LotteryPlus.getInstance().getDescription().getVersion() + "  &8by Momocraft");
                        CorePlusAPI.getUpdateManager().check(ConfigHandler.getPrefix(), sender, LotteryPlus.getInstance().getName(), LotteryPlus.getInstance().getDescription().getVersion());
                    } else {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                    }
                    return true;
                }
            case 2:
                // lotteryplus lottery GROUP
                if (args[0].equalsIgnoreCase("lottery")) {
                    if (CorePlusAPI.getPermManager().hasPermission(sender, "lotteryplus.command.lottery")) {
                        if (ConfigHandler.getConfigPath().isLottery()) {
                            Lottery.startLottery(sender, null, args[1]);
                        } else {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.featureDisabled", sender);
                        }
                    } else {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                    }
                    return true;
                }
            case 3:
                // lotteryplus lottery GROUP <PLAYER>
                if (args[0].equalsIgnoreCase("lottery")) {
                    if (CorePlusAPI.getPermManager().hasPermission(sender, "lotteryplus.command.lottery.other")) {
                        if (ConfigHandler.getConfigPath().isLottery()) {
                            Player player = CorePlusAPI.getPlayerManager().getPlayerString(args[2]);
                            if (player == null) {
                                String[] placeHolders = CorePlusAPI.getLangManager().newString();
                                placeHolders[2] = args[2]; // %targetplayer%
                                CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.targetNotFound", sender, placeHolders);
                                return true;
                            }
                            Lottery.startLottery(sender, player, args[1]);
                        } else {
                            CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.featureDisabled", sender);
                        }
                    } else {
                        CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.noPermission", sender);
                    }
                    return true;
                }
            default:
                CorePlusAPI.getLangManager().sendLangMsg(ConfigHandler.getPrefix(), "Message.unknownCommand", sender);
                return true;
        }
    }
}