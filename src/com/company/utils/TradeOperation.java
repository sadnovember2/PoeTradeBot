package com.company.utils;
import com.company.tables.Currency;
import com.company.tables.TradeOffer;

import java.awt.*;

public class TradeOperation {
    TradeOffer td;
    String pathLog;
    Robot robot;


    public TradeOperation(TradeOffer td, String pathLog) {
        this.pathLog = pathLog;
        this.td = td;
    }

    public boolean checkOnGoodie() {
        Currency currency;
        for (int i = 0; i < td.currencyTable.currencies.size(); i++) {
            currency = td.currencyTable.currencies.get(i);
            if (td.offerSubject.toLowerCase().contains(currency.nameWhatToSell.toLowerCase())) {              //check what to sell in table
                if (td.offerCurrencyName.toLowerCase().contains(currency.nameForWhatSell.toLowerCase())) {    //check what for sale in table
                    if (td.countItems <= currency.countAtm) {
                        if (td.countItems % currency.sellCount == 0) {
                            if (td.offerPrice % currency.sellCountFor == 0) {
                                if(td.countItems / currency.sellCount == td.offerPrice / currency.sellCountFor){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

}

