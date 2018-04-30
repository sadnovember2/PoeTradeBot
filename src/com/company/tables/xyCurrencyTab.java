package com.company.tables;

import java.util.ArrayList;

public class xyCurrencyTab {
    public ArrayList<xyCurrency> currencyTab;

    public xyCurrencyTab() {
        currencyTab = new ArrayList<>();
        currencyTab.add(new xyCurrency("alteration",71,220, 20));
        currencyTab.add(new xyCurrency("fusing",103, 293, 20));
        currencyTab.add(new xyCurrency("alchemy", 277, 214, 10));
        currencyTab.add(new xyCurrency("gcp",310,177, 20));
        currencyTab.add(new xyCurrency("exalted",177,216, 10));
        currencyTab.add(new xyCurrency("chrome",135, 294, 20));
        currencyTab.add(new xyCurrency("jeweller's",68,293, 20));
        currencyTab.add(new xyCurrency("chance",136,219, 20));
        currencyTab.add(new xyCurrency("chisel",342,171, 20));
        currencyTab.add(new xyCurrency("scouring", 246, 295, 30));
        currencyTab.add(new xyCurrency("blessed",343,212, 20));
        currencyTab.add(new xyCurrency("regret", 280, 296, 20));
        currencyTab.add(new xyCurrency("regal",247, 214, 10));
        currencyTab.add(new xyCurrency("divine", 344,245, 10));
        currencyTab.add(new xyCurrency("vaal", 311,294,10));
        currencyTab.add(new xyCurrency("chaos",310,211,10));
    }
}
