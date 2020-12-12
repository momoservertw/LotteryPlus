package tw.momocraft.lotteryplus.utils;

import org.bukkit.Bukkit;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;
import tw.momocraft.lotteryplus.handlers.ServerHandler;

public class DependAPI {
    private VaultAPI vaultApi;
    private PlayerPointsAPI playerPointsApi;
    private GEAPI gemsEconomyApi;

    private boolean Vault = false;
    private boolean PlayerPoints = false;
    private boolean GemsEconomy = false;
    private boolean PlaceHolderAPI = false;

    public DependAPI() {
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.Vault")) {
            this.setVaultStatus(Bukkit.getServer().getPluginManager().getPlugin("Vault") != null);
            if (Vault) {
                setVaultApi();
            }
        }
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.PlayerPoints")) {
            this.setPlayerPointsStatus(Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints") != null);
            if (PlayerPoints) {
                setPlayerPointsApi();
            }
        }
        if (ConfigHandler.getConfig("config.yml").getBoolean("General.Settings.Features.Hook.GemsEconomy")) {
            this.setGemsEconomyStatus(Bukkit.getServer().getPluginManager().getPlugin("GemsEconomy") != null);
            if (GemsEconomy) {
                setGemsEconomyApi();
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
                + (PlayerPointsEnabled() ? "PlayerPoints, " : "")
                + (GemsEconomyEnabled() ? "GemsEconomy, " : "")
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

    public boolean PlayerPointsEnabled() {
        return this.PlayerPoints;
    }

    public boolean GemsEconomyEnabled() {
        return this.GemsEconomy;
    }

    public boolean PlaceHolderAPIEnabled() {
        return this.PlaceHolderAPI;
    }


    public void setVaultStatus(boolean bool) {
        this.Vault = bool;
    }

    private void setPlayerPointsStatus(boolean bool) {
        this.PlayerPoints = bool;
    }

    private void setGemsEconomyStatus(boolean bool) {
        this.GemsEconomy = bool;
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

    public PlayerPointsAPI getPlayerPointsApi() {
        return this.playerPointsApi;
    }

    private void setPlayerPointsApi() {
        playerPointsApi = new PlayerPointsAPI();
    }

    public GEAPI getGemsEconomyApi() {
        return this.gemsEconomyApi;
    }

    private void setGemsEconomyApi() {
        gemsEconomyApi = new GEAPI();
    }
}
