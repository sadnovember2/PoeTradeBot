package com.company;

import org.apache.commons.net.time.TimeTCPClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    static String pathLog = "E:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\logs\\Client.txt";
    static String pathCurrencyLots = "E:\\Edu\\poetrad3bot\\TradeLots.xls";
    String nameOwner = "Morty#3479";
    static String timekey = "";

    public static void main(String[] args) {
        new Main().getPath();
        System.out.println("Enter path to CLient.txt");
        Scanner scanner = new Scanner(System.in);
        pathLog = scanner.nextLine();
        new Main().listenCMD();
    }

    private void listenCMD() {
        Scanner scanner = new Scanner(System.in);
        if (checkTimekey(timekey)) {
            System.out.println("Key is good! Hello " + nameOwner + " I`ll sugget you to type start!");
            Bot bot = null;
            String cmdText = "";
            while (true) {
                cmdText = scanner.nextLine();
                if (cmdText.toLowerCase().contains("start")) {
                    bot = new Bot(pathLog, pathCurrencyLots);
                    bot.run();
                }
                if (cmdText.toLowerCase().contains("stop") || cmdText.toLowerCase().contains("stop")) {
                    System.exit(0);
                }
            }
        } else {
            System.out.println("Bad key!");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }

    private boolean checkTimekey(String timeKey) {
        long currentTime = getTime();
        byte[] keyToDEcode = new byte[0];
        try {
            keyToDEcode = timeKey.getBytes("UTF-16BE");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String decoded = decode(keyToDEcode, nameOwner);
        //System.out.println(decoded);
        Long timeX = Long.parseLong(decoded.substring(decoded.indexOf(" until ") + " until ".length(), decoded.indexOf(" +tradebotv")));
        if (currentTime <= timeX) {
            return true;
        } else
            return false;
    }

    private static void getPath() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("path to cfg = ");
        String cfg = scanner.nextLine();
        System.out.println(cfg);
        try {
            List<String> cfgs = Files.readAllLines(Paths.get(cfg));
            pathLog = cfgs.get(0);
            pathCurrencyLots = cfgs.get(1);
            timekey = cfgs.get(2);
            //System.out.println(pathLog + "\n" + pathCurrencyLots + "\n" + timekey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long getTime() {
        String string = "0";
        try {
            TimeTCPClient client = new TimeTCPClient();
            try {
                // Set timeout of 60 seconds
                client.setDefaultTimeout(60000);
                // Connecting to time server
                // Other time servers can be found at : http://tf.nist.gov/tf-cgi/servers.cgi#
                // Make sure that your program NEVER queries a server more frequently than once every 4 seconds
                client.connect("time.nist.gov");
                string = String.valueOf(client.getDate().getTime());
            } finally {
                client.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Long.parseLong(string);
    }

    public static String decode(byte[] pText, String pKey) {
        byte[] res = new byte[pText.length];
        byte[] key = pKey.getBytes();

        for (int i = 0; i < pText.length; i++) {
            res[i] = (byte) (pText[i] ^ key[i % key.length]);
        }

        return new String(res);
    }

}
