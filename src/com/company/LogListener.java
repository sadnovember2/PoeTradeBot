package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LogListener {
    List<String> StringsInLog = null;
    String path;
    String lastString = "1";
    File log;
    int timeIndex = 500;

    //Constructor
    LogListener(String path) {
        this.path = path;
        log = new File(path);
        try {
            StringsInLog = Files.readAllLines(Paths.get(path));                     //get all filelog
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (StringsInLog.size() > 0)
            lastString = StringsInLog.get(StringsInLog.size() - 1);             //get last String in filelog
        else
            lastString = "";
    }


    void listen() {
        while (StringsInLog.size() > 0) {
            try {
                StringsInLog = Files.readAllLines(Paths.get(path));                     //reinit linked file
            } catch (IOException e) {
                e.printStackTrace();
            }
            String buff = StringsInLog.get(StringsInLog.size() - 1);                //get last String in ACTUAL filelog
            if (!lastString.equals(buff)) {                                         //is lastString changed?
                lastString = buff;
                //System.out.println("New String in log:\n\t" + lastString);        //show new string
                break;
            }
            try {
                Thread.sleep(timeIndex);
            } catch (InterruptedException e) {
            }
        }
    }

    void listenOntime(int time){
        while (time > 0) {
            try {
                StringsInLog = Files.readAllLines(Paths.get(path));                     //reinit linked file
            } catch (IOException e) {
                e.printStackTrace();
            }
            String buff = StringsInLog.get(StringsInLog.size() - 1);                //get last String in ACTUAL filelog
            if (!lastString.equals(buff)) {                                         //is lastString changed?
                lastString = buff;
                //System.out.println("New String in log:\n\t" + lastString);        //show new string
                break;
            }
            time -= timeIndex;
            try {
                Thread.sleep(timeIndex);
            } catch (InterruptedException e) {
            }
        }
    }

    boolean checkOnTradeOffer() {                           //phrases from poe.trade and pathofexile.com/trade
        if ((lastString.toLowerCase().contains("Hi, I'd like to buy your".toLowerCase())
                || lastString.toLowerCase().contains("Hi, I'd like to buy your".toLowerCase())
                || lastString.toLowerCase().contains("Hi, I would like to buy your".toLowerCase()))
                && lastString.toLowerCase().contains("@From".toLowerCase())
                ) {
            if (lastString.toLowerCase().contains("stash tab"))                     //check on currency or item offer
                return false;
            return true;
        } else {
            return false;
        }
    }

    public boolean checkOnAFK() {
        if (lastString.toLowerCase().contains("AFK mode is now ON".toLowerCase())){
            return true;
        }
        else
            return false;
    }
}