package tw.momocraft.lotteryplus.utils;

import java.util.List;
import java.util.Map;

public class LotteryMap {

    private List<String> commands;
    private Map<String, Double> chanceMap;

    public List<String> getCommands() {
        return commands;
    }

    public Map<String, Double> getChanceMap() {
        return chanceMap;
    }

    public void setChanceMap(Map<String, Double> chanceMap) {
        this.chanceMap = chanceMap;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
}
