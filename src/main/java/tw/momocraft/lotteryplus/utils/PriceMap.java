package tw.momocraft.lotteryplus.utils;

public class PriceMap {

    private String priceType;
    private double priceAmount;

    public double getPriceAmount() {
        return priceAmount;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceAmount(double priceAmount) {
        this.priceAmount = priceAmount;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }
}
