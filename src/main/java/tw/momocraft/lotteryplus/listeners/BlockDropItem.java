package tw.momocraft.lotteryplus.listeners;

import javafx.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;
import tw.momocraft.lotteryplus.utils.Lottery;
import tw.momocraft.lotteryplus.utils.LotteryMap;

import java.util.List;

public class BlockDropItem implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    private void onBlockDropItemEvent(BlockDropItemEvent e) {
        if (!ConfigHandler.getConfigPath().isLottery()) {
            return;
        }
        if (!ConfigHandler.getConfigPath().isLotteryBlock()) {
            return;
        }
        ItemStack headItemStack = null;
        for (Item item : e.getItems()) {
            if (item.getItemStack().getType().equals(Material.PLAYER_HEAD)) {
                headItemStack = item.getItemStack();
            }
        }
        if (headItemStack == null) {
            return;
        }
        Pair<String, List<LotteryMap>> lotteryBlockProp = ConfigHandler.getConfigPath().getLotteryBlockProp().get(
                CorePlusAPI.getUtilsManager().getSkullValue(headItemStack));
        if (lotteryBlockProp == null) {
            return;
        }
        Player player = e.getPlayer();
        e.getItems().clear();
        Lottery.startLottery(Bukkit.getConsoleSender(), player, lotteryBlockProp.getKey());
        CorePlusAPI.getLangManager().sendFeatureMsg(ConfigHandler.isDebugging(), ConfigHandler.getPlugin(), "Lottery", player.getName(), "execute", "return", "Lucky Block",
                new Throwable().getStackTrace()[0]);
    }
}
