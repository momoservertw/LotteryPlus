package tw.momocraft.lotteryplus.utils;

import org.bukkit.configuration.ConfigurationSection;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigPath {
    public ConfigPath() {
        setUp();
    }

    //  ============================================== //
    //         General Settings                        //
    //  ============================================== //
    private Map<String, String> customCmdProp;
    private boolean logDefaultNew;
    private boolean logDefaultZip;
    private boolean logCustomNew;
    private boolean logCustomZip;
    private String logCustomPath;
    private String logCustomName;

    //  ============================================== //
    //         Lottery Settings                        //
    //  ============================================== //
    private boolean lottery;
    private boolean lotteryLog;
    private boolean lotteryLogNew;
    private boolean lotteryLogZip;
    private Map<String, Map<List<String>, Double>> lotteryProp;


    //  ============================================== //
    //         Setup all configuration.                //
    //  ============================================== //
    private void setUp() {
        setupGeneral();
        setupLottery();
    }

    private void setupGeneral() {
        logDefaultZip = ConfigHandler.getConfig("config.yml").getBoolean("General.Custom-Commands.Settings.Log.Default.To-Zip");
        logDefaultNew = ConfigHandler.getConfig("config.yml").getBoolean("General.Custom-Commands.Settings.Log.Default.New-File");
        logCustomNew = ConfigHandler.getConfig("config.yml").getBoolean("General.Custom-Commands.Settings.Log.Custom.New-File");
        logCustomZip = ConfigHandler.getConfig("config.yml").getBoolean("General.Custom-Commands.Settings.Log.Custom.To-Zip");
        logCustomPath = ConfigHandler.getConfig("config.yml").getString("General.Custom-Commands.Settings.Log.Custom.Path");
        logCustomName = ConfigHandler.getConfig("config.yml").getString("General.Custom-Commands.Settings.Log.Custom.Name");
        ConfigurationSection cmdConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("General.Custom-Commands.Groups");
        if (cmdConfig != null) {
            customCmdProp = new HashMap<>();
            for (String group : cmdConfig.getKeys(false)) {
                customCmdProp.put(group, ConfigHandler.getConfig("config.yml").getString("General.Custom-Commands.Groups." + group));
            }
        }
    }

    private void setupLottery() {
        lottery = ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Enable");
        lotteryLog = ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Settings.Log.Enable");
        lotteryLogNew = ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Settings.Log.New-File");
        lotteryLogZip = ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Settings.Log.To-Zip");
        ConfigurationSection lotteryConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Lottery.Groups");
        if (lotteryConfig != null) {
            lotteryProp = new HashMap<>();
            ConfigurationSection groupConfig;
            Map<List<String>, Double> groupMap;
            String groupEnable;
            for (String group : lotteryConfig.getKeys(false)) {
                if (group.equals("Enable")) {
                    continue;
                }
                groupEnable = ConfigHandler.getConfig("config.yml").getString("Lottery.Groups." + group + ".Enable");
                if (groupEnable == null || groupEnable.equals("true")) {
                    groupConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Lottery.Groups." + group);
                    if (groupConfig != null) {
                        groupMap = new HashMap<>();
                        for (String key : groupConfig.getKeys(false)) {
                            if (key.equals("Enable")) {
                                continue;
                            }
                            groupEnable = ConfigHandler.getConfig("config.yml").getString("Lottery.Groups." + group + "." + key + ".Enable");
                            if (groupEnable == null || groupEnable.equals("true")) {
                                groupMap.put(ConfigHandler.getConfig("config.yml").getStringList("Lottery.Groups." + group + "." + key + ".Commands"),
                                        ConfigHandler.getConfig("config.yml").getDouble("Lottery.Groups." + group + "." + key + ".Chance"));
                            }
                        }
                        lotteryProp.put(group, groupMap);
                    }
                }
            }
        }
    }

    //  ============================================== //
    //         General Settings                        //
    //  ============================================== //
    public Map<String, String> getCustomCmdProp() {
        return customCmdProp;
    }

    public boolean isLogDefaultNew() {
        return logDefaultNew;
    }

    public boolean isLogDefaultZip() {
        return logDefaultZip;
    }

    public boolean isLogCustomNew() {
        return logCustomNew;
    }

    public boolean isLogCustomZip() {
        return logCustomZip;
    }

    public String getLogCustomName() {
        return logCustomName;
    }

    public String getLogCustomPath() {
        return logCustomPath;
    }

    //  ============================================== //
    //         Lottery Settings                        //
    //  ============================================== //
    public boolean isLottery() {
        return lottery;
    }

    public boolean isLotteryLog() {
        return lotteryLog;
    }

    public boolean isLotteryLogNew() {
        return lotteryLogNew;
    }

    public boolean isLotteryLogZip() {
        return lotteryLogZip;
    }

    public Map<String, Map<List<String>, Double>> getLotteryProp() {
        return lotteryProp;
    }
}
