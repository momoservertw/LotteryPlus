package tw.momocraft.lotteryplus.handlers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tw.momocraft.lotteryplus.Commands;
import tw.momocraft.lotteryplus.LotteryPlus;
import tw.momocraft.lotteryplus.utils.*;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConfigHandler {

    private static YamlConfiguration configYAML;
    private static DependAPI depends;
    private static ConfigPath configPath;
    private static UpdateHandler updater;
    private static Logger logger;
    private static Zip ziper;

    public static void generateData(boolean reload) {
        genConfigFile("config.yml");
        setDepends(new DependAPI());
        sendUtilityDepends();
        setConfigPath(new ConfigPath());
        if (!reload) {
            setUpdater(new UpdateHandler());
        }
        setLogger(new Logger());
        setZip(new Zip());
    }

    public static void registerEvents() {
        LotteryPlus.getInstance().getCommand("LotteryPlus").setExecutor(new Commands());
        LotteryPlus.getInstance().getCommand("LotteryPlus").setTabCompleter(new TabComplete());

    }

    private static void sendUtilityDepends() {
        ServerHandler.sendConsoleMessage("&fHooked [ &e"
                + (getDepends().getVault().vaultEnabled() ? "Vault, " : "")
                + (getDepends().PlaceHolderAPIEnabled() ? "PlaceHolderAPI, " : "")
                + "&f]");
    }


    public static FileConfiguration getConfig(String fileName) {
        File filePath = LotteryPlus.getInstance().getDataFolder();
        File file;
        switch (fileName) {
            case "config.yml":
                if (configYAML == null) {
                    getConfigData(filePath, fileName);
                }
                break;
            default:
                break;
        }
        file = new File(filePath, fileName);
        return getPath(fileName, file, false);
    }

    private static FileConfiguration getConfigData(File filePath, String fileName) {
        File file = new File(filePath, fileName);
        if (!(file).exists()) {
            try {
                LotteryPlus.getInstance().saveResource(fileName, false);
            } catch (Exception e) {
                ServerHandler.sendErrorMessage("&cCannot save " + fileName + " to disk!");
                return null;
            }
        }
        return getPath(fileName, file, true);
    }

    private static YamlConfiguration getPath(String fileName, File file, boolean saveData) {
        switch (fileName) {
            case "config.yml":
                if (saveData) {
                    configYAML = YamlConfiguration.loadConfiguration(file);
                }
                return configYAML;
        }
        return null;
    }

    private static void genConfigFile(String fileName) {
        String[] fileNameSlit = fileName.split("\\.(?=[^\\.]+$)");
        int configVersion = 0;
        File filePath = LotteryPlus.getInstance().getDataFolder();
        switch (fileName) {
            case "config.yml":
                configVersion = 2;
                break;
        }
        getConfigData(filePath, fileName);
        File File = new File(filePath, fileName);
        if (File.exists() && getConfig(fileName).getInt("Config-Version") != configVersion) {
            if (LotteryPlus.getInstance().getResource(fileName) != null) {
                LocalDateTime currentDate = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
                String currentTime = currentDate.format(formatter);
                String newGen = fileNameSlit[0] + " " + currentTime + "." + fileNameSlit[0];
                File newFile = new File(filePath, newGen);
                if (!newFile.exists()) {
                    File.renameTo(newFile);
                    File configFile = new File(filePath, fileName);
                    configFile.delete();
                    getConfigData(filePath, fileName);
                    ServerHandler.sendConsoleMessage("&4The file \"" + fileName + "\" is out of date, generating a new one!");
                }
            }
        }
        getConfig(fileName).options().copyDefaults(false);
    }

    public static DependAPI getDepends() {
        return depends;
    }

    private static void setDepends(DependAPI depend) {
        depends = depend;
    }


    private static void setConfigPath(ConfigPath configPath) {
        ConfigHandler.configPath = configPath;
    }

    public static ConfigPath getConfigPath() {
        return configPath;
    }

    public static boolean isDebugging() {
        return ConfigHandler.getConfig("config.yml").getBoolean("Debugging");
    }

    public static UpdateHandler getUpdater() {
        return updater;
    }

    private static void setUpdater(UpdateHandler update) {
        updater = update;
    }

    private static void setLogger(Logger log) {
        logger = log;
    }

    public static Logger getLogger() {
        return logger;
    }

    private static void setZip(Zip zip) {
        ziper = zip;
    }

    public static Zip getZip() {
        return ziper;
    }
}