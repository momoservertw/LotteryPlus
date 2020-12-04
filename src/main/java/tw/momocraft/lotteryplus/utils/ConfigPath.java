package tw.momocraft.lotteryplus.utils;

import javafx.util.Pair;
import org.bukkit.configuration.ConfigurationSection;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;
import tw.momocraft.lotteryplus.handlers.ServerHandler;

import java.util.ArrayList;
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
    private boolean lotteryBlock;
    private boolean lotteryLog;
    private boolean lotteryLogNew;
    private boolean lotteryLogZip;
    private final Map<String, List<LotteryMap>> lotteryProp = new HashMap<>();
    private final Map<String, Pair<String, List<LotteryMap>>> lotteryBlockProp = new HashMap<>();

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
        lotteryBlock = ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Settings.Features.Lucky-Block");
        lotteryLog = ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Settings.Log.Enable");
        lotteryLogNew = ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Settings.Log.New-File");
        lotteryLogZip = ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Settings.Log.To-Zip");
        ConfigurationSection lotteryConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Lottery.Groups");
        if (lotteryConfig != null) {
            ConfigurationSection groupConfig;
            String groupEnable;
            LotteryMap lotteryMap;
            List<LotteryMap> lotteryMapList;
            ConfigurationSection chanceConfig;
            Map<String, Double> chanceMap;
            String lotteryBlockSkull;
            // Reward Groups
            for (String group : lotteryConfig.getKeys(false)) {
                if (group.equals("Enable")) {
                    continue;
                }
                groupConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Lottery.Groups." + group);
                if (groupConfig != null) {
                    groupEnable = ConfigHandler.getConfig("config.yml").getString("Lottery.Groups." + group + ".Enable");
                    if (groupEnable == null || groupEnable.equals("true")) {
                        lotteryMapList = new ArrayList<>();
                        // Reward Groups Priority
                        for (String rarityGroup : groupConfig.getKeys(false)) {
                            if (rarityGroup.equals("Enable") || rarityGroup.equals("Settings")) {
                                continue;
                            }
                            groupEnable = ConfigHandler.getConfig("config.yml").getString("Lottery.Groups." + group + "." + rarityGroup + ".Enable");
                            if (groupEnable == null || groupEnable.equals("true")) {
                                lotteryMap = new LotteryMap();
                                lotteryMap.setList(ConfigHandler.getConfig("config.yml").getStringList("Lottery.Groups." + group + "." + rarityGroup + ".Commands"));
                                chanceMap = new HashMap<>();
                                chanceConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Lottery.Groups." + group + "." + rarityGroup + ".Chance");
                                if (chanceConfig != null) {
                                    for (String chanceGroup : chanceConfig.getKeys(false)) {
                                        chanceMap.put(chanceGroup, ConfigHandler.getConfig("config.yml").getDouble("Lottery.Groups." + group + "." + rarityGroup + ".Chance." + chanceGroup));
                                    }
                                } else {
                                    chanceMap.put("0", ConfigHandler.getConfig("config.yml").getDouble("Lottery.Groups." + group + "." + rarityGroup + ".Chance"));
                                }
                                lotteryMap.setChanceMap(chanceMap);
                                lotteryMapList.add(lotteryMap);
                            }
                        }
                        lotteryProp.put(group, lotteryMapList);
                        lotteryBlockSkull = ConfigHandler.getConfig("config.yml").getString("Lottery.Groups." + group + ".Settings.Lucky-Block.Skull-Textures");
                        if (lotteryBlockSkull != null) {
                            lotteryBlockProp.put(lotteryBlockSkull, new Pair<>(group, lotteryMapList));
                        }
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

    public boolean isLotteryBlock() {
        return lotteryBlock;
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

    public Map<String, List<LotteryMap>> getLotteryProp() {
        return lotteryProp;
    }

    public Map<String, Pair<String, List<LotteryMap>>> getLotteryBlockProp() {
        return lotteryBlockProp;
    }
}
