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

    //  ============================================== //
    //         Lottery Settings                        //
    //  ============================================== //
    private boolean lottery;
    private Map<String, Map<List<String>, Double>> lotteryProp;


    //  ============================================== //
    //         Setup all configuration.                //
    //  ============================================== //
    private void setUp() {
        setupGeneral();
        setupLottery();
    }

    private void setupGeneral() {
        ConfigurationSection cmdConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("General.Custom-Commands");
        if (cmdConfig != null) {
            customCmdProp = new HashMap<>();
            for (String group : cmdConfig.getKeys(false)) {
                customCmdProp.put(group, ConfigHandler.getConfig("config.yml").getString("General.Custom-Commands." + group));
            }
        }
    }

    private void setupLottery() {
        lottery = ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Enable");
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

    public Map<String, String> getCustomCmdProp() {
        return customCmdProp;
    }

    public boolean isLottery() {
        return lottery;
    }

    public Map<String, Map<List<String>, Double>> getLotteryProp() {
        return lotteryProp;
    }
}
