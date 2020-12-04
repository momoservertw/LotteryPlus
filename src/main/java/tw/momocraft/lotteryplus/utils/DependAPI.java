package tw.momocraft.lotteryplus.utils;

import org.bukkit.Bukkit;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;
import tw.momocraft.lotteryplus.handlers.ServerHandler;

public class DependAPI {
    private VaultAPI vaultApi;
    private boolean Vault = false;
    private boolean PlaceHolderAPI = false;

    public DependAPI() {
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.Vault")) {
            this.setVaultStatus(Bukkit.getServer().getPluginManager().getPlugin("Vault") != null);
            if (Vault) {
                setVaultApi();
            }
        }
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.PlaceHolderAPI")) {
            this.setPlaceHolderStatus(Bukkit.getServer().getPluginManager().getPlugin("PlaceHolderAPI") != null);
        }
        sendUtilityDepends();
    }

    private void sendUtilityDepends() {
        ServerHandler.sendConsoleMessage("&fHooked [ &e"
                + (VaultEnabled() ? "Vault, " : "")
                + (PlaceHolderAPIEnabled() ? "PlaceHolderAPI, " : "")
                + "&f]");

        /*
        if (ResidenceEnabled()) {
            if (ConfigHandler.getConfigPath().isSpawnResFlag()) {
                FlagPermissions.addFlag("spawnbypass");
            }
        }
         */
    }

    public boolean VaultEnabled() {
        return this.Vault;
    }

    public boolean PlaceHolderAPIEnabled() {
        return this.PlaceHolderAPI;
    }


    public void setVaultStatus(boolean bool) {
        this.Vault = bool;
    }

    public void setPlaceHolderStatus(boolean bool) {
        this.PlaceHolderAPI = bool;
    }

    public VaultAPI getVaultApi() {
        return this.vaultApi;
    }

    private void setVaultApi() {
        vaultApi = new VaultAPI();
    }
}
