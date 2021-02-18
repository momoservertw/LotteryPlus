package tw.momocraft.lotteryplus.utils;

import javafx.util.Pair;
import org.bukkit.configuration.ConfigurationSection;
import tw.momocraft.coreplus.utils.economy.PriceMap;
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
    //         Message Variables                       //
    //  ============================================== //
    private String msgTitle;
    private String msgHelp;
    private String msgReload;
    private String msgVersion;
    private String msgCmdLottery;

    //  ============================================== //
    //         Lottery Variables                       //
    //  ============================================== //
    private boolean lottery;
    private boolean lotteryBlock;
    private boolean lotteryLog;
    private final Map<String, List<LotteryMap>> lotteryProp = new HashMap<>();
    private final Map<String, Pair<String, List<LotteryMap>>> lotteryBlockProp = new HashMap<>();

    //  ============================================== //
    //         Setup all configuration                 //
    //  ============================================== //
    private void setUp() {
        setupMsg();
        setupLottery();
    }

    //  ============================================== //
    //         Message Setter                          //
    //  ============================================== //
    private void setupMsg() {
        msgTitle = ConfigHandler.getConfig("config.yml").getString("Message.Commands.title");
        msgHelp = ConfigHandler.getConfig("config.yml").getString("Message.Commands.help");
        msgReload = ConfigHandler.getConfig("config.yml").getString("Message.Commands.reload");
        msgVersion = ConfigHandler.getConfig("config.yml").getString("Message.Commands.version");
        msgCmdLottery = ConfigHandler.getConfig("config.yml").getString("Message.Commands.lottery");
    }

    //  ============================================== //
    //         Lottery Setter                          //
    //  ============================================== //
    private void setupLottery() {
        lottery = ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Enable");
        lotteryBlock = ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Settings.Features.Lucky-Block");
        lotteryLog = ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Settings.Log", true);
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
                groupConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Lottery.Groups." + group);
                if (groupConfig == null) {
                    continue;
                }
                if (!ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Groups." + group + ".Enable", true)) {
                    continue;
                }
                lotteryMapList = new ArrayList<>();
                // Reward Groups Priority
                for (String rarityGroup : groupConfig.getKeys(false)) {
                    lotteryMap = new LotteryMap();
                    if (rarityGroup.equals("Enable") || rarityGroup.equals("Settings")) {
                        continue;
                    }
                    if (!ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Groups." + group + ".Enable", true)) {
                        continue;
                    }
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
                lotteryProp.put(group, lotteryMapList);
                lotteryBlockSkull = ConfigHandler.getConfig("config.yml").getString("Lottery.Groups." + group + ".Settings.Lucky-Block.Skull-Textures");
                if (lotteryBlockSkull != null) {
                    lotteryBlockProp.put(lotteryBlockSkull, new Pair<>(group, lotteryMapList));
                }
            }
        }
    }

    //  ============================================== //
    //         Message Getter                          //
    //  ============================================== //
    public String getMsgTitle() {
        return msgTitle;
    }

    public String getMsgHelp() {
        return msgHelp;
    }

    public String getMsgReload() {
        return msgReload;
    }

    public String getMsgVersion() {
        return msgVersion;
    }

    public String getMsgCmdLottery() {
        return msgCmdLottery;
    }

    //  ============================================== //
    //         Lottery Getter                          //
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

    public Map<String, List<LotteryMap>> getLotteryProp() {
        return lotteryProp;
    }

    public Map<String, Pair<String, List<LotteryMap>>> getLotteryBlockProp() {
        return lotteryBlockProp;
    }
}
