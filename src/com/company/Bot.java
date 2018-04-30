package com.company;

import com.company.tables.CurrencyTable;
import com.company.tables.TradeOffer;
import com.company.utils.TradeOperation;

public class Bot extends Thread {
    private String threadName = "botThread";
    boolean isRunning = true;
    String pathLog = "E:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\logs\\Client.txt";
    String pathCurrencyLots = "E:\\Edu\\poetrad3bot\\TradeLots.xls";

    Bot(String pathLog, String pathCurrencyLots) {
        this.pathLog = pathLog;
        this.pathCurrencyLots = pathCurrencyLots;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()){
            CurrencyTable ct = new CurrencyTable(pathCurrencyLots);     //init our lots from xls table
            ct.print();

            JavaRobot javaRobot = new JavaRobot();
            javaRobot.openStash();
            javaRobot.updateCurrencyTable(ct);
            ct.print();

            LogListener logListener = new LogListener(pathLog);

            while (isRunning) {
                logListener.listen();
                if (logListener.checkOnAFK()) {
                    javaRobot = new JavaRobot();
                    javaRobot.antiAFK();
                    System.out.println("ANTI AFK");
                }
                if (logListener.checkOnTradeOffer()) {
                    System.out.println("This is currency tradeoffer");
                    TradeOffer tradeOffer = new TradeOffer(logListener.lastString, ct);
                    tradeOffer.print();
                    TradeOperation tradeOperation = new TradeOperation(tradeOffer, pathLog);
                    if (tradeOperation.checkOnGoodie()) {
                        System.out.println("Good Offer");
                        javaRobot = new JavaRobot(tradeOffer, pathLog);
                        javaRobot.openStash();
                        if (tradeOffer.isCurrencyEnough()) {
                            System.out.println("Enough currency for trade");
                            javaRobot.inviteInParty();
                            if (javaRobot.isJoined()) {
                                javaRobot.takeCurrencyFromStash();
                                javaRobot.inviteToTrade();
                                try {
                                    Thread.sleep(15000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                javaRobot.trade();
                                try {
                                    Thread.sleep(30000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                javaRobot.kickFromParty();
                                javaRobot.openStash();
                                javaRobot.clearPlayerStash();
                            }
                            javaRobot.updateCurrencyTable(ct);
                        } else {
                            System.out.println("Not Enough currency for trade\n--------------------");
                        }
                    } else {
                        System.out.println("Bad Offer");
                    }
                }
            }
        }
    }

    public void killListener() {
        isRunning = false;
    }

}
