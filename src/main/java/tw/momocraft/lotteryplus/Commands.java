package tw.momocraft.lotteryplus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;
import tw.momocraft.lotteryplus.handlers.PermissionsHandler;
import tw.momocraft.lotteryplus.handlers.ServerHandler;
import tw.momocraft.lotteryplus.utils.Language;
import tw.momocraft.lotteryplus.utils.Lottery;


public class Commands implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, Command c, String l, String[] args) {
        if (args.length == 0) {
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
        } else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
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
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (PermissionsHandler.hasPermission(sender, "lotteryplus.command.reload")) {
                // working: close purge.Auto-Clean schedule
                ConfigHandler.generateData();
                Language.sendLangMessage("Message.configReload", sender);
            } else {
                Language.sendLangMessage("Message.noPermission", sender);
            }
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
            if (PermissionsHandler.hasPermission(sender, "lotteryplus.command.version")) {
                Language.dispatchMessage(sender, "&d&lLotteryPlus &e&lv" + LotteryPlus.getInstance().getDescription().getVersion() + "&8 - &fby Momocraft");
                ConfigHandler.getUpdater().checkUpdates(sender);
            } else {
                Language.sendLangMessage("Message.noPermission", sender);
            }
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("lottery")) {
            if (PermissionsHandler.hasPermission(sender, "lotteryplus.command.lottery")) {
                if (ConfigHandler.getConfigPath().isLottery()) {
                    Lottery.startLottery(sender, args[1]);
                } else {
                    ServerHandler.sendConsoleMessage("Lottery: Disabled");
                }
            } else {
                Language.sendLangMessage("Message.noPermission", sender);
            }
            return true;
        } else {
            Language.sendLangMessage("Message.unknownCommand", sender);
            return true;
        }
    }
}