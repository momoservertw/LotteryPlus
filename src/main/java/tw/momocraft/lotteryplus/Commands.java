package tw.momocraft.lotteryplus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;
import tw.momocraft.lotteryplus.handlers.PermissionsHandler;
import tw.momocraft.lotteryplus.handlers.PlayerHandler;
import tw.momocraft.lotteryplus.handlers.ServerHandler;
import tw.momocraft.lotteryplus.utils.Language;
import tw.momocraft.lotteryplus.utils.Lottery;


public class Commands implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, Command c, String l, String[] args) {
        switch (args.length) {
            case 0:
                if (PermissionsHandler.hasPermission(sender, "lotteryplus.use")) {
                    Language.dispatchMessage(sender, "");
                    Language.sendLangMessage("Message.LotteryPlus.Commands.title", sender, false);
                    if (PermissionsHandler.hasPermission(sender, "LotteryPlus.command.version")) {
                        Language.dispatchMessage(sender, "&d&lLotteryPlus &e&lv" + LotteryPlus.getInstance().getDescription().getVersion() + "&8 - &fby Momocraft");
                    }
                    Language.sendLangMessage("Message.LotteryPlus.Commands.help", sender, false);
                    Language.dispatchMessage(sender, "");
                } else {
                    Language.sendLangMessage("Message.noPermission", sender);
                }
                return true;
            case 1:
                if (args[0].equalsIgnoreCase("help")) {
                    if (PermissionsHandler.hasPermission(sender, "lotteryplus.use")) {
                        Language.dispatchMessage(sender, "");
                        Language.sendLangMessage("Message.LotteryPlus.Commands.title", sender, false);
                        if (PermissionsHandler.hasPermission(sender, "LotteryPlus.command.version")) {
                            Language.dispatchMessage(sender, "&d&lLotteryPlus &e&lv" + LotteryPlus.getInstance().getDescription().getVersion() + "&8 - &fby Momocraft");
                        }
                        Language.sendLangMessage("Message.LotteryPlus.Commands.help", sender, false);
                        if (PermissionsHandler.hasPermission(sender, "LotteryPlus.command.reload")) {
                            Language.sendLangMessage("Message.LotteryPlus.Commands.reload", sender, false);
                        }
                        if (PermissionsHandler.hasPermission(sender, "LotteryPlus.command.lottery")) {
                            Language.sendLangMessage("Message.LotteryPlus.Commands.lottery", sender, false);
                        }
                        Language.dispatchMessage(sender, "");
                    } else {
                        Language.sendLangMessage("Message.noPermission", sender);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (PermissionsHandler.hasPermission(sender, "lotteryplus.command.reload")) {
                        // working: close purge.Auto-Clean schedule
                        ConfigHandler.generateData(true);
                        Language.sendLangMessage("Message.configReload", sender);
                    } else {
                        Language.sendLangMessage("Message.noPermission", sender);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("version")) {
                    if (PermissionsHandler.hasPermission(sender, "lotteryplus.command.version")) {
                        Language.dispatchMessage(sender, "&d&lLotteryPlus &e&lv" + LotteryPlus.getInstance().getDescription().getVersion() + "&8 - &fby Momocraft");
                        ConfigHandler.getUpdater().checkUpdates(sender);
                    } else {
                        Language.sendLangMessage("Message.noPermission", sender);
                    }
                    return true;
                }
            case 2:
                if (args[0].equalsIgnoreCase("lottery")) {
                    if (PermissionsHandler.hasPermission(sender, "lotteryplus.command.lottery")) {
                        if (ConfigHandler.getConfigPath().isLottery()) {
                            Lottery.startLottery(sender, null, args[1]);
                        } else {
                            ServerHandler.sendConsoleMessage("Lottery is Disabled.");
                        }
                    } else {
                        Language.sendLangMessage("Message.noPermission", sender);
                    }
                    return true;
                }
            case 3:
                if (args[0].equalsIgnoreCase("lottery")) {
                    if (PermissionsHandler.hasPermission(sender, "lotteryplus.command.lottery")) {
                        if (ConfigHandler.getConfigPath().isLottery()) {
                            Player player = PlayerHandler.getPlayerString(args[2]);
                            if (player == null) {
                                String[] placeHolders = Language.newString();
                                placeHolders[2] = args[2];
                                Language.sendLangMessage("Message.targetNotFound", sender, placeHolders);
                                return true;
                            }
                            Lottery.startLottery(sender, player, args[1]);
                        } else {
                            ServerHandler.sendConsoleMessage("Lottery is Disabled.");
                        }
                    } else {
                        Language.sendLangMessage("Message.noPermission", sender);
                    }
                    return true;
                }
            default:
                Language.sendLangMessage("Message.unknownCommand", sender);
                return true;
        }
    }
}