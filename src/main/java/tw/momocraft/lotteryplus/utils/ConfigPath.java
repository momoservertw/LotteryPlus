package tw.momocraft.lotteryplus.utils;

import javafx.util.Pair;
import org.bukkit.configuration.ConfigurationSection;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;

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
    private final Map<String, SoundMap> soundProp = new HashMap<>();
    private final Map<String, ParticleMap> particleProp = new HashMap<>();

    //  ============================================== //
    //         Lottery Settings                        //
    //  ============================================== //
    private boolean lottery;
    private boolean lotteryBlock;
    private boolean lotteryLog;
    private boolean lotteryLogNew;
    private boolean lotteryLogZip;
    private final Map<String, Pair<PriceMap, List<LotteryMap>>> lotteryProp = new HashMap<>();
    private final Map<String, Pair<String, List<LotteryMap>>> lotteryBlockProp = new HashMap<>();

    //  ============================================== //
    //         Setup all configuration.                //
    //  ============================================== //
    private void setUp() {
        setupGeneral();
        setupLottery();
    }

    private void setupGeneral() {
        ConfigurationSection cmdConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("General.Custom-Commands.Groups");
        if (cmdConfig != null) {
            customCmdProp = new HashMap<>();
            for (String group : cmdConfig.getKeys(false)) {
                customCmdProp.put(group, ConfigHandler.getConfig("config.yml").getString("General.Custom-Commands.Groups." + group));
            }
        }
        ConfigurationSection particleConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("General.Particles");
        if (particleConfig != null) {
            ParticleMap particleMap;
            for (String group : particleConfig.getKeys(false)) {
                particleMap = new ParticleMap();
                particleMap.setType(ConfigHandler.getConfig("config.yml").getString("General.Particles." + group + ".Type"));
                particleMap.setAmount(ConfigHandler.getConfig("config.yml").getInt("General.Particles." + group + ".Amount", 1));
                particleMap.setTimes(ConfigHandler.getConfig("config.yml").getInt("General.Particles." + group + ".Times", 1));
                particleMap.setInterval(ConfigHandler.getConfig("config.yml").getInt("General.Particles." + group + ".Interval", 20));
                particleProp.put(group, particleMap);
            }
        }
        ConfigurationSection soundConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("General.Sounds");
        if (soundConfig != null) {
            SoundMap soundMap;
            for (String group : soundConfig.getKeys(false)) {
                soundMap = new SoundMap();
                soundMap.setType(ConfigHandler.getConfig("config.yml").getString("General.Sounds." + group + ".Type"));
                soundMap.setVolume(ConfigHandler.getConfig("config.yml").getInt("General.Sounds." + group + ".Volume", 1));
                soundMap.setPitch(ConfigHandler.getConfig("config.yml").getInt("General.Sounds." + group + ".Pitch", 1));
                soundMap.setTimes(ConfigHandler.getConfig("config.yml").getInt("General.Sounds." + group + ".Loop.Times", 1));
                soundMap.setInterval(ConfigHandler.getConfig("config.yml").getInt("General.Sounds." + group + ".Loop.Interval", 20));
                soundProp.put(group, soundMap);
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
            LotteryMap lotteryMap;
            PriceMap priceMap;
            List<LotteryMap> lotteryMapList;
            ConfigurationSection chanceConfig;
            Map<String, Double> chanceMap;
            String lotteryBlockSkull;
            // Reward Groups
            for (String group : lotteryConfig.getKeys(false)) {
                if (group.equals("Enable")) {
                    continue;
                }
                lotteryMap = new LotteryMap();
                priceMap = new PriceMap();
                priceMap.setPriceType(ConfigHandler.getConfig("config.yml").getString("Lottery.Groups." + group + ".Settings.Price.Type"));
                priceMap.setPriceAmount(ConfigHandler.getConfig("config.yml").getDouble("Lottery.Groups." + group + ".Settings.Price.Amount"));
                groupConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Lottery.Groups." + group);
                if (groupConfig != null) {
                    if (ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Groups." + group + ".Enable", true)) {
                        lotteryMapList = new ArrayList<>();
                        // Reward Groups Priority
                        for (String rarityGroup : groupConfig.getKeys(false)) {
                            if (rarityGroup.equals("Enable") || rarityGroup.equals("Settings")) {
                                continue;
                            }
                            if (ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Groups." + group + ".Enable", true)) {
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
                        lotteryProp.put(group, new Pair<>(priceMap, lotteryMapList));
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

    public Map<String, ParticleMap> getParticleProp() {
        return particleProp;
    }

    public Map<String, SoundMap> getSoundProp() {
        return soundProp;
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

    public Map<String, Pair<PriceMap, List<LotteryMap>>> getLotteryProp() {
        return lotteryProp;
    }

    public Map<String, Pair<String, List<LotteryMap>>> getLotteryBlockProp() {
        return lotteryBlockProp;
    }
}
