package tw.momocraft.lotteryplus.utils;

import java.util.List;
import java.util.Map;

public class LotteryMap {

    private List<String> list;
    private Map<String, Double> groupMap;

    public List<String> getList() {
        return list;
    }

    public Map<String, Double> getChanceMap() {
        return groupMap;
    }

    public void setGroupMap(Map<String, Double> groupMap) {
        this.groupMap = groupMap;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
