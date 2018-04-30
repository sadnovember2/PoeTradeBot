package com.company.tables;

public class Currency {
    public String nameWhatToSell;
    public String nameForWhatSell;
    public int countAtm;
    public int sellCount;
    public int sellCountFor;
    float priceFor1;

    Currency(String nameWhatToSell,
             String nameForWhatSell,
             int countAtm,
             int sellCount,
             int sellCountFor) {
        this.nameWhatToSell = nameWhatToSell;
        this.nameForWhatSell = nameForWhatSell;
        this.countAtm = countAtm;
        this.sellCount = sellCount;
        this.sellCountFor = sellCountFor;
        priceFor1 = (float) ((1.0 * sellCount) / sellCountFor);
    }

    void print(){
        System.out.println(nameWhatToSell+ "(atm " + countAtm + ") -> " + nameForWhatSell + " || " + sellCount + " -> " + sellCountFor + " " + priceFor1);
    }

}
