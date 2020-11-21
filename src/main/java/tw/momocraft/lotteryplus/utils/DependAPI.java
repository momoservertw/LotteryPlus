package tw.momocraft.lotteryplus.utils;

import org.bukkit.Bukkit;

public class DependAPI {
    private boolean PlaceHolderAPI = false;
    private VaultAPI vault;

    public DependAPI() {
        this.setPlaceHolderStatus(Bukkit.getServer().getPluginManager().getPlugin("PlaceHolderAPI") != null);
        this.setVault();
    }

    public boolean PlaceHolderAPIEnabled() {
        return this.PlaceHolderAPI;
    }

    public void setPlaceHolderStatus(boolean bool) {
        this.PlaceHolderAPI = bool;
    }

    public VaultAPI getVault() {
        return this.vault;
    }

    private void setVault() {
        this.vault = new VaultAPI();
    }
}
