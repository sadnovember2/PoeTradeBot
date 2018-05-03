package com.company;

import com.company.tables.CurrencyTable;
import com.company.tables.TradeOffer;
import com.company.tables.xyCurrency;
import com.company.tables.xyCurrencyTab;
import com.company.utils.KeyboardKeys;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JavaRobot {
    xyCurrencyTab stashTab = new xyCurrencyTab();
    Robot robot;
    String offerDealer;
    int countItems;
    String valuteName;
    String pathLog;
    TradeOffer td;
    String currencyIncmin;
    int countIncmincurrency;


    public JavaRobot() {
    }

    public JavaRobot(TradeOffer td, String pathLog) {
        this.td = td;
        this.pathLog = pathLog;
        this.offerDealer = td.offerDealer;
        this.countItems = td.countItems;
        this.valuteName = td.offerSubject;
        this.currencyIncmin = td.offerCurrencyName;
        this.countIncmincurrency = td.offerPrice;
    }

    void inviteInParty() {
        try {
            this.robot = new Robot();
            robot.setAutoDelay(40);
            robot.setAutoWaitForIdle(true);

            robot.delay(1000);
            robot.mouseMove(2, 2);
            leftClick();
            robot.delay(500);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            type("/invite " + offerDealer);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void leftClick() {
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(50);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(50);
    }

    private void type(int i) {
        robot.delay(40);
        robot.keyPress(i);
        robot.keyRelease(i);
    }

    private void type(String s) {
        try {
            KeyboardKeys kk = new KeyboardKeys();
            /*
            byte[] bytes = s.getBytes();
            for (byte b : bytes) {
                int code = b;
                // keycode only handles [A-Z] (which is ASCII decimal [65-90])
                if (code > 96 && code < 123) code = code - 32;
                {
                    robot.delay(40);
                    robot.keyPress(code);
                    robot.keyRelease(code);
                }
                if (code == 95) {
                    robot.delay(40);
                    robot.keyPress(523);
                    robot.keyRelease(523);
                }
                */
            for (int i = 0; i < s.length(); i++) {
                kk.keyPress(s.charAt(i));
            }
        } catch (Exception e) {
            System.err.print(e);
        }
    }

    public void tryToTrade() {                      //tradeProcedure
        //Check if OfferName joined hideout
        if (isJoined()) {
            takeCurrencyFromStash();
            forcetoTrade();
            if (true) {
                kickFromParty();
            } else {
                forcetoTrade();
                kickFromParty();
            }
        } else kickFromParty();
    }

    public void kickFromParty() {
        try {
            this.robot = new Robot();
            robot.setAutoDelay(40);
            robot.setAutoWaitForIdle(true);

            robot.delay(500);
            robot.mouseMove(2, 2);
            leftClick();
            robot.delay(40);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            type("/kick " + offerDealer);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            System.out.println("..." + offerDealer + " kicked...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isSuccess() {
        return true;
    }

    private void forcetoTrade() {
    }

    public boolean isJoined() {
        try {
            int timeWaiting = 45000;                                                           //45s waiting player in hideout

            List<String> StringsInLog = Files.readAllLines(Paths.get(pathLog));           //get all filelog fields
            String lastString = StringsInLog.get(StringsInLog.size() - 1);                     //get last String in filelog
            System.out.println("...Waiting " + offerDealer + " in the Hideout...");

            while (timeWaiting > 0) {
                StringsInLog = Files.readAllLines(Paths.get(pathLog));                 //reinit linked file
                String buff = StringsInLog.get(StringsInLog.size() - 1);                //get last String in ACTUAL filelog
                if (!lastString.equals(buff) && buff.contains(offerDealer + " has joined the area.")) {                                         //is lastString changed?
                    lastString = buff;
                    System.out.println(offerDealer + " has joined");        //show new string
                    return true;
                }
                timeWaiting -= 500;
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("..." + offerDealer + " didn`t join for 45s...");
        return false;
    }

    public void takeCurrencyFromStash() {
        int countStocks = 0;
        int left = 0;
        xyCurrency temp = new xyCurrency("", 1000, 1000, 0);
        for (int i = 0; i < stashTab.currencyTab.size(); i++) {
            if (valuteName.toLowerCase().contains(stashTab.currencyTab.get(i).name.toLowerCase())) {
                temp = stashTab.currencyTab.get(i);
                countStocks = calcCountStocks(countItems, temp.stock);                                          //calc N stocks to move in stash
                left = countItems - countStocks * temp.stock;                                                     //left after moving full stocks
                System.out.println("need move :" + countStocks + " * " + temp.stock + " + left: " + left);
            }
        }
        try {
            robot.setAutoDelay(40);
            robot.setAutoWaitForIdle(true);
            if (!(countStocks == 0 && left == 1)) {

                robot.delay(100);
                robot.mouseMove(temp.x, temp.y);
                for (int i = 0; i < countStocks; i++) {                                         //moving full stocks into inventory
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    leftClick();
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                }

                if (left > 0) {
                    robot.delay(100);
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    leftClick();
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                    robot.delay(100);
                    robot.mouseMove(temp.x + 36, temp.y - 64);
                    for (int i = 0; i < left - 1; i++) {                                               //moving unstocking count
                        leftClick();
                    }

                    robot.delay(100);
                    robot.mouseMove(temp.x + 54, temp.y - 22);                                 //press OK
                    leftClick();

                    robot.delay(100);
                    int StepRight = 0;
                    int StepDown = 0;
                    if (countStocks >= 5) {
                        StepRight = countStocks / 5;
                    }
                    StepDown = countStocks - 5 * StepRight;
                    robot.mouseMove(462 + 29 * StepRight, 372 + 29 * StepDown);
                    leftClick();
                }
            } else {
                robot.delay(100);
                robot.mouseMove(temp.x, temp.y);
                robot.keyPress(KeyEvent.VK_CONTROL);
                leftClick();
                robot.keyRelease(KeyEvent.VK_CONTROL);
            }

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private int calcCountStocks(int countItems, int stock) {
        int i = 0;
        while (countItems >= stock) {
            countItems -= stock;
            i++;
        }
        return i;
    }

    public void updateCurrencyTable(CurrencyTable currencyTable) {
        try {
            this.robot = new Robot();
            robot.setAutoDelay(40);
            robot.setAutoWaitForIdle(true);
            robot.delay(1000);
            robot.mouseMove(2, 2);
            leftClick();

            int chaos = 0;
            int[] countAtm = new int[currencyTable.currencies.size()];
            for (int i = 0; i < stashTab.currencyTab.size(); i++) {
                String name = stashTab.currencyTab.get(i).name;
                for (int j = 0; j < currencyTable.currencies.size(); j++) {
                    if (name.equals(currencyTable.currencies.get(j).nameWhatToSell.toLowerCase())) {
                        robot.mouseMove(stashTab.currencyTab.get(i).x, stashTab.currencyTab.get(i).y);
                        robot.keyPress(KeyEvent.VK_CONTROL);
                        robot.delay(40);
                        robot.keyPress(KeyEvent.VK_C);
                        robot.delay(40);
                        robot.keyRelease(KeyEvent.VK_C);
                        robot.keyRelease(KeyEvent.VK_CONTROL);
                        String infoCurrency = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString();
                        infoCurrency = infoCurrency.substring(infoCurrency.indexOf("Stack Size: ") + "Stack Size: ".length(), infoCurrency.indexOf("/"));
                        countAtm[i] = Integer.parseInt(infoCurrency);
                        chaos = countAtm[i];
                    }
                }
            }
            int chaosIndex = 0;
            for (int i = 0; i < countAtm.length; i++) {
                if (chaos == countAtm[i])
                    chaosIndex = i;
            }
            for (int i = chaosIndex; i < countAtm.length; i++) {
                countAtm[i] = chaos;
            }
            for (int i = 0; i < currencyTable.currencies.size(); i++) {
                currencyTable.currencies.get(i).countAtm = countAtm[i];
            }
        } catch (AWTException | UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
    }

    public void openStash() {
        try {
            this.robot = new Robot();
            robot.setAutoDelay(40);
            robot.setAutoWaitForIdle(true);

            robot.delay(40);
            robot.mouseMove(2, 2);
            leftClick();
            robot.delay(40);
            robot.mouseMove(394, 282);
            leftClick();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void inviteToTrade() {
        try {
            this.robot = new Robot();
            robot.setAutoDelay(40);
            robot.setAutoWaitForIdle(true);

            robot.delay(1000);
            robot.mouseMove(2, 2);
            leftClick();
            robot.delay(500);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            robot.delay(500);
            type("/tradewith " + offerDealer);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void trade() {
        //MOVING FRROM STASH TO TRADE
        try {
            int countStocks = 0;
            int left = 0;
            robot.setAutoDelay(40);
            robot.setAutoWaitForIdle(true);
            xyCurrency temp = new xyCurrency("", 1000, 1000, 0);
            for (int i = 0; i < stashTab.currencyTab.size(); i++) {
                if (valuteName.toLowerCase().contains(stashTab.currencyTab.get(i).name.toLowerCase())) {
                    temp = stashTab.currencyTab.get(i);
                    countStocks = calcCountStocks(countItems, temp.stock);                                          //calc N stocks to move in stash
                    left = countItems - countStocks * temp.stock;                                                     //left after moving full stocks
                    //System.out.println("need move :" + countStocks + " * " + temp.stock + " + left: " + left);
                }
            }
            int startX = 462;
            int startY = 372;
            robot.setAutoDelay(40);
            robot.setAutoWaitForIdle(true);
            robot.mouseMove(startX, startY);
            for (int i = 0; i < countStocks / 5; i++) {
                for (int j = 0; j < 5; j++) {
                    robot.mouseMove(startX, startY);
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    leftClick();
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                    startY = startY + 29;
                }
                startX = startX + 29;
                startY = 372;

            }
            for (int i = 0; i < (countStocks - 5 * (countStocks / 5) + 1); i++) {
                robot.mouseMove(startX, startY);
                robot.keyPress(KeyEvent.VK_CONTROL);
                leftClick();
                robot.keyRelease(KeyEvent.VK_CONTROL);
                startY = startY + 29;
            }
        } catch (Exception e) {
            System.err.println(e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //CHECKING INCOMNIG TRADEITEMS

        {/*
            try {
                int countIncmin = countIncmincurrency;
                int countStocks = 0;
                int left = 0;
                robot.setAutoDelay(40);
                robot.setAutoWaitForIdle(true);
                xyCurrency temp = new xyCurrency("", 1000, 1000, 0);
                for (int i = 0; i < stashTab.currencyTab.size(); i++) {
                    if (currencyIncmin.toLowerCase().contains(stashTab.currencyTab.get(i).name.toLowerCase())) {
                        temp = stashTab.currencyTab.get(i);
                        countStocks = calcCountStocks(countIncmin, temp.stock);                                          //calc N stocks to move in stash
                        left = countIncmin - countStocks * temp.stock;                                                     //left after moving full stocks
                        System.out.println("need move :" + countStocks + " * " + temp.stock + " + left: " + left);
                    }
                }

                int received = 0;
                int startX = 62;
                int startY = 159;
                robot.setAutoDelay(40);
                robot.setAutoWaitForIdle(true);
                robot.mouseMove(startX, startY);
                for (int i = 0; i < countStocks / 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        robot.mouseMove(startX, startY);
                        robot.keyPress(KeyEvent.VK_CONTROL);
                        robot.delay(40);
                        robot.keyPress(KeyEvent.VK_C);
                        robot.delay(40);
                        robot.keyRelease(KeyEvent.VK_C);
                        robot.delay(40);
                        robot.keyRelease(KeyEvent.VK_CONTROL);
                        String infoCurrency = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString();
                        if (infoCurrency.contains(currencyIncmin)) {
                            infoCurrency = infoCurrency.substring(infoCurrency.indexOf("Stack Size: ") + "Stack Size: ".length(), infoCurrency.indexOf("/"));
                            received += Integer.valueOf(infoCurrency);
                        }
                        startY = startY + 29;
                    }
                    startY = 159;
                    startX = startX + 29;
                }

                int toMax = 0;
                if (left > 0) {
                    toMax = countStocks - 5 * (countStocks / 5) + 1;
                } else {
                    toMax = countStocks - 5 * (countStocks / 5);
                }
                for (int i = 0; i < toMax; i++) {
                    robot.mouseMove(startX, startY);
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    robot.delay(40);
                    robot.keyPress(KeyEvent.VK_C);
                    robot.delay(40);
                    robot.keyRelease(KeyEvent.VK_C);
                    robot.delay(40);
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                    String infoCurrency = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString();
                    if (infoCurrency.toLowerCase().contains(currencyIncmin.toLowerCase())) {
                        infoCurrency = infoCurrency.substring(infoCurrency.indexOf("Stack Size: ") + "Stack Size: ".length(), infoCurrency.indexOf("/"));
                        received += Integer.valueOf(infoCurrency);
                    }
                    startY = startY + 29;
                }


                System.out.println("RECEIVED :" + received);
                if (received >= countIncmincurrency) {
                    System.out.println("BERRRRY GOOD");
                    robot.mouseMove(83, 496);
                    leftClick();
                } else {
                    robot.mouseMove(358, 495);
                    leftClick();
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }*/

            {
                try {
                    String temp = "Rarity: Currency " + "Scroll of Wisdom " + "-------- " + "Stack Size: 40/40 " + "--------";
                    int startX = 62;
                    int startY = 159;
                    int countIncmin = countIncmincurrency;
                    int count = 0;
                    robot.setAutoDelay(40);
                    robot.setAutoWaitForIdle(true);
                    robot.mouseMove(startX, startY);
                    for (int i = 0; i < 12; i++) {
                        for (int j = 0; j < 5; j++) {
                            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(temp), null);
                            robot.mouseMove(startX, startY);
                            robot.keyPress(KeyEvent.VK_CONTROL);
                            robot.keyPress(KeyEvent.VK_C);
                            robot.delay(40);
                            robot.keyRelease(KeyEvent.VK_C);
                            robot.keyRelease(KeyEvent.VK_CONTROL);
                            String infoCurrency = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString();
                            if (infoCurrency.toLowerCase().contains(currencyIncmin.toLowerCase())) {
                                infoCurrency = infoCurrency.substring(infoCurrency.indexOf("Stack Size: ") + "Stack Size: ".length(), infoCurrency.indexOf("/"));
                                count += Integer.valueOf(infoCurrency);
                            }
                            startY = startY + 29;
                            if (count >= countIncmin) {
                                break;
                            }
                        }
                        startY = 159;
                        startX = startX + 29;
                        if (count >= countIncmin) {
                            break;
                        }
                    }

                    LogListener listenerForTradeCalncel = new LogListener(Main.pathLog);

                    System.out.println("RECEIVED :" + count);
                    if (count >= countIncmincurrency) {
                        System.out.println("BERRRRY GOOD");
                        robot.mouseMove(83, 496);
                        leftClick();
                        int sleepTIme = 15000;
                        while (sleepTIme > 0) {
                            listenerForTradeCalncel.listenOntime(sleepTIme);
                            if (listenerForTradeCalncel.lastString.contains("Trade accepted.")
                                    ||listenerForTradeCalncel.lastString.contains("Trade cancelled")) {
                                this.openStash();
                                break;
                            }
                            else
                                break;
                        }
                        this.openStash();
                        robot.mouseMove(358, 495);
                        leftClick();
                    } else {
                        this.openStash();
                        robot.mouseMove(358, 495);
                        leftClick();
                    }

                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public void clearPlayerStash() {
        int startX = 462;
        int startY = 372;
        robot.setAutoDelay(40);
        robot.setAutoWaitForIdle(true);
        robot.mouseMove(startX, startY);
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 5; j++) {
                robot.mouseMove(startX, startY);
                robot.delay(10);
                robot.keyPress(KeyEvent.VK_CONTROL);
                leftClick();
                robot.keyRelease(KeyEvent.VK_CONTROL);
                startY = startY + 29;
            }
            startX = startX + 29;
            startY = 372;
        }
    }

    public void antiAFK() {
        try {
            this.robot = new Robot();
            robot.setAutoDelay(40);
            robot.setAutoWaitForIdle(true);

            robot.delay(1000);
            robot.mouseMove(2, 2);
            leftClick();
            robot.delay(500);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            robot.delay(500);
            type("/afkoff");
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
