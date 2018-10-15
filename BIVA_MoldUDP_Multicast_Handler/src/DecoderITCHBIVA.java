/*
 * Copyright (C) 2018 Juan J. Martínez
 * 
 * All rights reserved. This complete software or any portion thereof
 * can be used as reference but may not be reproduced in any manner 
 * whatsoever without the express written permission of the owner.
 * 
 * The purpose of this is to be consulted and used as a referece of 
 * functionallyty.
 * 
 * Developed in Mexico City
 * First version, 2018
 *
 */

/**
 *
 * @author Juan J. Martínez
 * @email juanjmtzs@gmail.com
 * @phone +52-1-55-1247-8044
 * @linkedin https://www.linkedin.com/in/juanjmtzs/
 *
 */


import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class DecoderITCHBIVA implements Potocol {

    public static int count_add_order = 0;
    public static int count_broken_trade = 0;
    public static int count_order_executed_with_price = 0;
    public static int count_order_delete = 0;
    public static int count_order_executed = 0;
    public static int count_order_replace = 0;
    public static int count_trading_action = 0;
    public static int count_indicative_price_quantity = 0;
    public static int count_price_tick = 0;
    public static int count_quantity_tick = 0;
    public static int count_stock_directory = 0;
    public static int count_warrants_directory = 0;
    public static int count_system_event = 0;
    public static int count_time_stamp = 0;
    public static int count_participant_directory = 0;
    public static int count_trade_message = 0;
    public static int count_glimpse_snapshot = 0;
    public static int count_reference_price = 0;
    public static int count_best_bid_offer = 0;
    public static int count_news_message = 0;

    static final byte add_order = 'A';
    static final byte broken_trade = 'B';
    static final byte order_executed_with_price = 'C';
    static final byte order_delete = 'D';
    static final byte order_executed = 'E';
    static final byte order_replace = 'U';
    static final byte trading_action = 'H';
    static final byte indicative_price_quantity = 'I';
    static final byte price_tick = 'L';
    static final byte quantity_tick = 'M';
    static final byte stock_directory = 'R';
    static final byte warrants_directory = 'W';
    static final byte system_event = 'S';
    static final byte time_stamp = 'T';
    static final byte participant_directory = 'F';
    static final byte trade_message = 'P';
    static final byte glimpse_snapshot = 'G';
    static final byte reference_price = 'X';
    static final byte best_bid_offer = 'Q';
    static final byte news_message = 'N';

    @Override
    public String parse(ByteBuffer message, long seqNum) {
        byte type = message.get(0);
        byte[] bytes;
        BigInteger bigInt;
        String jsonSonstructor = "";
        jsonSonstructor = jsonSonstructor + "{";
        jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
        jsonSonstructor = jsonSonstructor + "\"Message\":{";

        switch (type) {
            case add_order:
                count_add_order++;

                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Add Order\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 5, 13);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Order Number\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 13, 14);
                    jsonSonstructor = jsonSonstructor + "\"Order Verb\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 14, 22);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Quantity\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 22, 26);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Orderbook\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 26, 30);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Price\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + "}}";

                } catch (UnsupportedEncodingException ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }
                break;
            case broken_trade:
                count_broken_trade++;
                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Busted Trade\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 5, 13);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Match Number\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 13, 14);
                    jsonSonstructor = jsonSonstructor + "\"Reason\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";

                } catch (UnsupportedEncodingException ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }

                break;
            case order_executed_with_price:
                count_order_executed_with_price++;
                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Order Executed With Price\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 5, 13);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Order Number\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 13, 21);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Executed Quantity\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 21, 29);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Match Number\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 29, 30);
                    jsonSonstructor = jsonSonstructor + "\"Trade Indicator\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 30, 31);
                    jsonSonstructor = jsonSonstructor + "\"Printable\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 31, 35);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Execution Price\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + "}}";
                } catch (UnsupportedEncodingException ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }
                break;
            case order_delete:
                count_order_delete++;
                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Order Deleted\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 5, 13);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Order Number\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + "}}";
                } catch (Exception ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }
                break;
            case order_executed:
                count_order_executed++;
                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Order Executed\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 5, 13);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Order Number\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 13, 21);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Executed Quantity\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 21, 29);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Match Number\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 29, 30);
                    jsonSonstructor = jsonSonstructor + "\"Trade Indicator\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                } catch (UnsupportedEncodingException ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }
                break;
            case order_replace:
                count_order_replace++;
                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Order Updated\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 5, 13);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Original Order Number\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 13, 21);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"New Order Number\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 21, 29);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Quantity\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 29, 33);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Price\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + "}}";
                } catch (Exception ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }
                break;
            case trading_action:
                count_trading_action++;
                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Trading Action\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 5, 9);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Orderbook\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 9, 10);
                    jsonSonstructor = jsonSonstructor + "\"Trading State\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 10, 11);
                    jsonSonstructor = jsonSonstructor + "\"Reason\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                } catch (UnsupportedEncodingException ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }

                break;
            case indicative_price_quantity:
                count_indicative_price_quantity++;
                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Indicative Price/Quantity\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 5, 13);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Theorical Opening Quantity\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 13, 17);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Orderbook\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 17, 21);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Best Bid\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 21, 25);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Best Offer\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 25, 29);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Theorical Opening Price\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 29, 30);
                    jsonSonstructor = jsonSonstructor + "\"Cross Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                } catch (UnsupportedEncodingException ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }
                break;

            case price_tick:
                count_price_tick++;
                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Price Tick\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 5, 9);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Tick Size Table Id\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 9, 13);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Tick Size\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 13, 17);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Price Start\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + "}}";
                } catch (Exception ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }
                break;
            case quantity_tick:
                count_quantity_tick++;
                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Quantity Tick\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 5, 9);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Tick Size Table Id\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 9, 17);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Tick Size\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 17, 25);

                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Quantity Start\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + "}}";
                } catch (Exception ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }
                break;
            case stock_directory:
                count_stock_directory++;
                try {


                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Orderbook Directory\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 5, 9);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Orderbook\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";
                    

                    bytes = Arrays.copyOfRange(message.array(), 9, 21);
                    jsonSonstructor = jsonSonstructor + "\"ISIN\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 21, 36);
                    jsonSonstructor = jsonSonstructor + "\"Sec Code\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 36, 39);
                    jsonSonstructor = jsonSonstructor + "\"Currency\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 39, 47);
                    jsonSonstructor = jsonSonstructor + "\"Group\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 47, 55);

                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Minimum Quantity\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 55, 59);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Quantity Tick Size Table Id\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 59, 63);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Quantity Decimals\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 63, 67);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Price Tick Size Table Id\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 67, 71);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Price Decimals\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 71, 75);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Delisting or Maturity Date\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 75, 79);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Delisting Time\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 79, 80);
                    jsonSonstructor = jsonSonstructor + "\"Turnover Ratio\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 80, 83);
                    jsonSonstructor = jsonSonstructor + "\"Quotation Basis\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 83, 95);
                    jsonSonstructor = jsonSonstructor + "\"Instrument\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 95, 96);
                    jsonSonstructor = jsonSonstructor + "\"Listing Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 96, 100);
                    jsonSonstructor = jsonSonstructor + "\"Listing Exchange\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";

                } catch (UnsupportedEncodingException ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }
                break;
            case system_event:
                count_system_event++;
                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"System Event\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 5, 13);
                    jsonSonstructor = jsonSonstructor + "\"Group:\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 13, 14);
                    jsonSonstructor = jsonSonstructor + "\"Event Code\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 14, 18);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Orderbook\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + "}}";
                } catch (UnsupportedEncodingException ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }
                break;
            case time_stamp:
                count_time_stamp++;
                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Time Stamp\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Seconds\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + "}}";
                } catch (Exception ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }
                break;
            case participant_directory:
                count_participant_directory++;
                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Participant Directory\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 5, 9);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Participant Id\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";
                    bytes = Arrays.copyOfRange(message.array(), 9, 21);
                    jsonSonstructor = jsonSonstructor + "\"Participant Code\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                } catch (UnsupportedEncodingException ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }

                break;
            case trade_message:
                count_trade_message++;
                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Trade\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 5, 13);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Executed Quantity\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 13, 17);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Orderbook\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 17, 18);
                    jsonSonstructor = jsonSonstructor + "\"Printable\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 18, 22);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Execution Price\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 22, 30);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Match Number\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 30, 31);
                    jsonSonstructor = jsonSonstructor + "\"Trade Indicator\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                } catch (UnsupportedEncodingException ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }
                break;
            case glimpse_snapshot:
                count_glimpse_snapshot++;
                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Glimpse Snapshot\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 9);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Sequence Number TV\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + "}}";

                } catch (Exception ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }
                break;
            case reference_price:
                count_reference_price++;
                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Orderbook Reference Price\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 5, 9);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Orderbook\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";


                    bytes = Arrays.copyOfRange(message.array(), 9, 13);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Reference Price\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";


                    bytes = Arrays.copyOfRange(message.array(), 13, 14);
                    jsonSonstructor = jsonSonstructor + "\"Price Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";
                  

                    bytes = Arrays.copyOfRange(message.array(), 14, 15);
                    jsonSonstructor = jsonSonstructor + "\"Reason\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "UTF-8") + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";

                    
                } catch (UnsupportedEncodingException ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }
                break;
            case best_bid_offer:
                count_best_bid_offer++;
                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Best Bid Offer\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 5, 9);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Orderbook\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 9, 13);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Best Bid\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 13, 21);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Best Bid Size\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 21, 25);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Best Offer\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 25, 33);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Best Offer Size\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + "}}";
                } catch (Exception ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }
                break;
            case news_message:
                count_news_message++;
                try {

                    jsonSonstructor = jsonSonstructor + "\"Name\":\"News\",";

                    jsonSonstructor = jsonSonstructor + "\"Type\":";
                    jsonSonstructor = jsonSonstructor + "\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 1, 5);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 5, 9);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Orderbook\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 9, 13);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"News Id\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    bytes = Arrays.copyOfRange(message.array(), 13, 17);
                    bigInt = new BigInteger(bytes);
                    jsonSonstructor = jsonSonstructor + "\"Participant Id\":";
                    jsonSonstructor = jsonSonstructor + bigInt;
                    jsonSonstructor = jsonSonstructor + ",";

                    int i = 17;

                    bytes = new byte[80];
                    int x = 0;
                    while (true) {
                        if (message.get(i) == 0) {
                            break;
                        }
                        bytes[x] = message.get(i);
                        x++;
                        i++;
                    }

                    jsonSonstructor = jsonSonstructor + "\"Title\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "Windows-1258") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    i++;
                    bytes = new byte[255];
                    x = 0;
                    while (true) {
                        if (message.get(i) == 0) {
                            break;
                        }
                        bytes[x] = message.get(i);
                        x++;
                        i++;
                    }

                    jsonSonstructor = jsonSonstructor + "\"Reference\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "Windows-1258") + "\"";
                    jsonSonstructor = jsonSonstructor + ",";

                    i++;
                    bytes = new byte[511];
                    x = 0;
                    while (true) {
                        if (message.get(i) == 0) {
                            break;
                        }

                        bytes[x] = message.get(i);
                        x++;
                        i++;
                    }
                    jsonSonstructor = jsonSonstructor + "\"News Text\":";
                    jsonSonstructor = jsonSonstructor + "\"" + new String(bytes, "Windows-1258") + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";

                } catch (UnsupportedEncodingException ex) {
                    jsonSonstructor = "";
                    jsonSonstructor = jsonSonstructor + "{";
                    jsonSonstructor = jsonSonstructor + "\"SeqNum\":" + seqNum + ",";
                    jsonSonstructor = jsonSonstructor + "\"Message\":{";
                    jsonSonstructor = jsonSonstructor + "\"Name\":\"Error While Decoding\",";
                    jsonSonstructor = jsonSonstructor + "\"Type\":\"" + (char) type + "\"";
                    jsonSonstructor = jsonSonstructor + "}}";
                }
                break;
            default:
                jsonSonstructor = jsonSonstructor + "\"Name\":\"Unsupported Message Type - " + (char) type + "\"}}";
                break;
        }
        return jsonSonstructor;
    }
}
