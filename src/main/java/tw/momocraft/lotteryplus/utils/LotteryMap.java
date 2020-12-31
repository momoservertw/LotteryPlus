package tw.momocraft.lotteryplus.utils;

import java.util.List;
import java.util.Map;

public class LotteryMap {

    private List<String> list;
    private Map<String, Double> chanceMap;

    public List<String> getList() {
        return list;
    }

    public Map<String, Double> getChanceMap() {
        return chanceMap;
    }

    public void setChanceMap(Map<String, Double> chanceMap) {
        this.chanceMap = chanceMap;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
