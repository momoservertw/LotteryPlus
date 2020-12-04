package tw.momocraft.lotteryplus.listeners;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import javafx.util.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;
import tw.momocraft.lotteryplus.handlers.ServerHandler;
import tw.momocraft.lotteryplus.utils.Lottery;
import tw.momocraft.lotteryplus.utils.LotteryMap;

import java.lang.reflect.Field;
import java.util.Collection;
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
        Pair<String, List<LotteryMap>> lotteryBlockProp = ConfigHandler.getConfigPath().getLotteryBlockProp().get(getSkullValue(headItemStack));
        if (lotteryBlockProp != null) {
            Player player = e.getPlayer();
            e.getItems().clear();
            Lottery.startLottery(player, null, lotteryBlockProp.getKey());
            ServerHandler.sendFeatureMessage("Lottery", player.getName(), "execute", "return", "Lucky Block",
                    new Throwable().getStackTrace()[0]);
        }
    }

    public static String getSkullValue(ItemStack itemStack) {
        SkullMeta headMeta = (SkullMeta) itemStack.getItemMeta();
        String url = null;
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            GameProfile profile = (GameProfile) profileField.get(headMeta);
            Collection<Property> properties = profile.getProperties().get("textures");
            for (Property property : properties) {
                url = property.getValue();
            }
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        return url;
    }
}
