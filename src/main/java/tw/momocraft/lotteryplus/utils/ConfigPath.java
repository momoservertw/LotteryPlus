package tw.momocraft.lotteryplus.utils;

import org.bukkit.configuration.ConfigurationSection;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.lotteryplus.LotteryPlus;
import tw.momocraft.lotteryplus.handlers.ConfigHandler;

import java.util.*;

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
    private final Map<String, String> lotteryBlockProp = new HashMap<>();

    //  ============================================== //
    //         Setup all configuration                 //
    //  ============================================== //
    private void setUp() {
        setupMsg();
        setupLottery();

        sendSetupMsg();
    }

    private void sendSetupMsg() {
        List<String> list = new ArrayList<>(LotteryPlus.getInstance().getDescription().getDepend());
        list.addAll(LotteryPlus.getInstance().getDescription().getSoftDepend());
        CorePlusAPI.getMsg().sendHookMsg(ConfigHandler.getPluginPrefix(), "plugins", list);

        /*
        list = Arrays.asList((
                "spawnbypass" + ","
                        + "spawnerbypass" + ","
                        + "damagebypass"
        ).split(","));
        CorePlusAPI.getMsg().sendHookMsg(ConfigHandler.getPluginPrefix(), "Residence flags", list);
    */
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
        ConfigurationSection priorityConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Lottery.Settings.Features.Priority");
        Map<String, Integer> lotteryPriority = new LinkedHashMap<>();
        try {
            for (String group : priorityConfig.getKeys(false))
                lotteryPriority.put(group,
                        ConfigHandler.getConfig("config.yml").getInt("Lottery.Settings.Features.Priority." + group));
        } catch (Exception ex) {
            CorePlusAPI.getMsg().sendErrorMsg(ConfigHandler.getPluginName(),
                    "There is an error while setting the config: Lottery.Settings.Priority");
        }
        lotteryPriority = CorePlusAPI.getUtils().sortByValue(lotteryPriority);
        ConfigurationSection lotteryConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Lottery.Groups");
        if (lotteryConfig == null)
            return;
        ConfigurationSection groupConfig;
        LotteryMap lotteryMap;
        List<LotteryMap> lotteryMapList;
        ConfigurationSection chanceConfig;
        Map<String, Double> chanceMap;
        String lotteryBlockSkull;
        // Reward Groups
        for (String groupName : lotteryConfig.getKeys(false)) {
            groupConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Lottery.Groups." + groupName);
            if (groupConfig == null)
                continue;
            if (!ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Groups." + groupName + ".Enable", true))
                continue;
            lotteryMapList = new ArrayList<>();
            // Reward Groups Priority
            for (String rarityGroup : groupConfig.getKeys(false)) {
                lotteryMap = new LotteryMap();
                if (rarityGroup.equals("Enable") || rarityGroup.equals("Settings"))
                    continue;
                lotteryMap.setCommands(ConfigHandler.getConfig("config.yml").getStringList("Lottery.Groups." + groupName + ".Groups." + rarityGroup + ".Commands"));
                // Chance
                chanceMap = new LinkedHashMap<>();
                chanceConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Lottery.Groups." + groupName + ".Groups." + rarityGroup + ".Chance");
                if (chanceConfig != null) {
                    for (String group : lotteryPriority.keySet()) {
                        chanceMap.put(group.toLowerCase(),
                                ConfigHandler.getConfig("config.yml").getDouble("Lottery.Groups." + groupName + ".Groups." + rarityGroup + ".Chance." + group));
                    }
                } else {
                    chanceMap.put("default", ConfigHandler.getConfig("config.yml").getDouble("Lottery.Groups." + groupName + ".Groups." + rarityGroup + ".Chance"));
                }
                lotteryMap.setChanceMap(chanceMap);
                lotteryMapList.add(lotteryMap);
            }
            lotteryProp.put(groupName, lotteryMapList);
            lotteryBlockSkull = ConfigHandler.getConfig("config.yml").getString("Lottery.Groups." + groupName + ".Groups.Settings.Lucky-Block.Skull-Textures");
            if (lotteryBlockSkull != null)
                lotteryBlockProp.put(lotteryBlockSkull, groupName);
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

    public Map<String, String> getLotteryBlockProp() {
        return lotteryBlockProp;
    }
}
