package com.company.tables;

public class TradeOffer {
    public String offer;                       //String with offer
    public String offerDealer;                 //Who wants to buy
    public String offerSubject;                //What`s want to buy
    public int countItems;                     //How much
    public int offerPrice;                     //How much can pay
    public String offerCurrencyName;           //Currency for pay
    public double priceForOne;                 //price
    public CurrencyTable currencyTable;

    public TradeOffer(String offer, CurrencyTable ct){
        this.currencyTable = ct;
        this.offer = offer;
        findOfferDealer(offer);                                                         //parsing all fields in offer string
        findOfferSubject(offer);
    }

    private void findOfferSubject(String offer) {
        int start = offer.indexOf("to buy your ") + "to buy your ".length();
        int end = offer.indexOf(" ", start);
        this.countItems = Integer.valueOf(offer.substring(start, end));                                 //how much want to buy

        start = end;
        end = offer.indexOf("for my ");
        this.offerSubject = offer.substring(start, end);                                                //what currency want to buy

        start = offer.indexOf("for my ") + "for my ".length();
        end = offer.indexOf(" ", start);
        offerPrice = Integer.valueOf(offer.substring(start, end));                                      //how much can pay

        start = end;
        end = offer.indexOf(" in ");
        offerCurrencyName = offer.substring(start+1, end);                                                //currency Name for pay

        priceForOne = offerPrice * 1.0 / countItems;                                                      //calc price for 1 item
    }

    private void findOfferDealer(String offer) {                                                        //finding nickname with guild and without
        int startIndexNickName;
        int endIndexNickName;
        if (offer.contains("<") && offer.contains(">")) {
            startIndexNickName = offer.indexOf(">") + 2;
        } else {
            startIndexNickName = offer.indexOf("@From") + 6;
        }
        endIndexNickName = offer.indexOf(":", offer.indexOf("@From"));
        offerDealer = offer.substring(startIndexNickName, endIndexNickName);
    }

    public void print(){
        System.out.println(new StringBuilder()                                          //sout in cmd
                .append("--------------------\n")
                .append("\tTRADE OFFER\n")
                .append(offerDealer).append(" want to buy\n")
                .append(countItems).append(" ").append(offerSubject).append("for\n")
                .append(offerPrice).append(" ").append(offerCurrencyName)
                .append("\nOffered price: ").append(priceForOne)
                .append("\n--------------------")
        );
    }

    public boolean isCurrencyEnough(){
        for (int i = 0; i < currencyTable.currencies.size(); i++){
            if (offerSubject.toLowerCase().contains(currencyTable.currencies.get(i).nameWhatToSell)){
                if (countItems <= currencyTable.currencies.get(i).countAtm){
                    return true;
                }
            }
        }
        return false;
    }
}
