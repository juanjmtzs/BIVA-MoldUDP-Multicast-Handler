/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jmartinezs
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MulticastBMVNoBytes {

    private static MulticastSocket socket;
    private static InetAddress group;
    private static boolean continua = true;
    public static String receiverIP = "";
    public static int myLastReceived = 0;
    public static String userRecovery, passwordRecovery, ipRecovery, portRecovery, groupRecovery = "";
    public static String argsRecoverySnapshot[] = new String[10];

    public static String argsRecoveryGap[] = new String[9];

    public static ArrayList<String[]> ListRetransmissions = new ArrayList<String[]>();

    public static boolean GapsChecking = false;

    public static void kill() {
        continua = false;

        if (socket.isConnected()) {
            try {
                socket.leaveGroup(group);
            } catch (IOException ex) {
                Logger.getLogger(MulticastBMVNoBytes.class.getName()).log(Level.SEVERE, null, ex);
            }
            socket.disconnect();
            socket.close();
        }

    }

    public static void StartHandling(String[] args) throws UnknownHostException, IOException {
        try {
            myLastReceived = 0;
            userRecovery = args[5];
            passwordRecovery = args[6];
            ipRecovery = args[7];
            portRecovery = args[8];
            groupRecovery = args[9];

            argsRecoverySnapshot[0] = ipRecovery;
            argsRecoverySnapshot[1] = portRecovery;
            argsRecoverySnapshot[2] = "";
            argsRecoverySnapshot[3] = userRecovery;
            argsRecoverySnapshot[4] = passwordRecovery;
            argsRecoverySnapshot[5] = groupRecovery;
            argsRecoverySnapshot[6] = "";
            argsRecoverySnapshot[7] = "0";
            argsRecoverySnapshot[8] = "4";
            argsRecoverySnapshot[9] = "";

            argsRecoveryGap[0] = ipRecovery;
            argsRecoveryGap[1] = portRecovery;
            argsRecoveryGap[2] = "";
            argsRecoveryGap[3] = userRecovery;
            argsRecoveryGap[4] = passwordRecovery;
            argsRecoveryGap[5] = groupRecovery;
            argsRecoveryGap[6] = "";

            group = InetAddress.getByName(args[0]);
            final int port = Integer.parseInt(args[1]);

            String service = args[2];
            receiverIP = args[3];

            //String userHomeFolder = System.getProperty("user.home") + "\\LogsBMV";
            try {
                DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
                Date date = new Date();
                System.setOut(new PrintStream(new File(args[4], Instant.now().toEpochMilli() + "_BMVMulticast_" + dateFormat.format(date) + ".log")));
            } catch (FileNotFoundException ex) {

                System.out.println("[Error]{{\"Error\":{\"Description\":" + ex + "}}");

            }

            socket = new MulticastSocket(port);
            if ("".equals(receiverIP)) {
                socket.setInterface(InetAddress.getLocalHost());
            } else {
                socket.setInterface(InetAddress.getByName(receiverIP));
            }
            socket.joinGroup(group);
            socket.setSoTimeout(39600000);
            if (socket.isClosed() == true) {
                System.out.println("Disconected");
            } else {

                try {
                    continua = true;

                    DatagramPacket packet = new DatagramPacket(new byte[64000], 64000);
                    System.out.println("[{\"Service Name\":\"BMV Multicast\"}");
                    while (continua != false) {
                        socket.receive(packet);

                        int packetSize = packet.getLength();
                        byte len[];
                        byte grupo[];
                        byte session[];
                        byte sequence[];
                        byte messagesInPackage[];
                        byte timestamp[];
                        byte messages[];

                        len = Arrays.copyOfRange(packet.getData(), 0, 2);
                        messagesInPackage = Arrays.copyOfRange(packet.getData(), 2, 3);
                        grupo = Arrays.copyOfRange(packet.getData(), 3, 4);
                        session = Arrays.copyOfRange(packet.getData(), 4, 5);
                        sequence = Arrays.copyOfRange(packet.getData(), 5, 9);
                        timestamp = Arrays.copyOfRange(packet.getData(), 9, 17);
                        messages = Arrays.copyOfRange(packet.getData(), 17, packetSize);

                        BigInteger seqNum = new BigInteger(sequence);

                        //seqNum = seqNum.subtract(BigInteger.valueOf(1));
//                        MDBMV.Update.setText(""
//                                + "Session:  " + new BigInteger(session) + "\n"
//                                + "Total Messages:  " + seqNum + "\n"
//                                + "     Depth (1):  " + BMVNoBytes.count_depth + "\n"
//                                + "     Probable Allocation Price (2):  " + BMVNoBytes.count_probable_allocation_price + "\n"
//                                + "     Continuos Auction Beginning (3):  " + BMVNoBytes.count_continuos_auction_beginning + "\n"
//                                + "     Status Changes (4):  " + BMVNoBytes.count_status_changes + "\n"
//                                + "     To Middle Price Existing Quotes (5):  " + BMVNoBytes.count_to_middle_price_existing_quotes + "\n"
//                                + "     Local and Global Stock Markets (a):  " + BMVNoBytes.count_local_and_global_stock_markets + "\n"
//                                + "     Debt Metals and Money Market(b):  " + BMVNoBytes.count_debt_metals_and_money_market + "\n"
//                                + "     Capital Market Warrants (c):  " + BMVNoBytes.count_capital_market_warrants + "\n"
//                                + "     Capital Market TRACS (e):  " + BMVNoBytes.count_capital_market_TRACS + "\n"
//                                + "     Mutual Funds (f):  " + BMVNoBytes.count_mutual_funds + "\n"
//                                + "     Underlying Value on Warrants (y):  " + BMVNoBytes.count_underlying_value_on_warrants + "\n"
//                                + "     Tradability (E):  " + BMVNoBytes.count_tradability + "\n"
//                                + "     Trade Cancellation (H):  " + BMVNoBytes.count_trade_cancellation + "\n"
//                                + "     Weighted Average Price Settlement Prices (M):  " + BMVNoBytes.count_weighted_average_price_settlement_prices + "\n"
//                                + "     Best Offer (O):  " + BMVNoBytes.count_best_offer + "\n"
//                                + "     Capital Trades (P):  " + BMVNoBytes.count_capital_trades + "\n"
//                                + "     System Events (S):  " + BMVNoBytes.count_event_systems + "\n"
//                                + "     Virtual Trades (V):  " + BMVNoBytes.count_virtual_trades + "\n"
//                                + "     Mutual Fund Trades (Y):  " + BMVNoBytes.count_mutual_fund_trades + "\n"
//                                + "     Registry Operations (Z):  " + BMVNoBytes.count_registry_operations + "\n"
//                                + "     Derivatives Market Trades (Q):  " + BMVNoBytes.count_derivatives_market_trades + "\n"
//                                + "     Order Addition (A):  " + BMVNoBytes.count_order_addition + "\n"
//                                + "     Order Change (F):  " + BMVNoBytes.count_order_change + "\n"
//                                + "     Execution of Orders (C):  " + BMVNoBytes.count_execution_of_orders + "\n"
//                                + "     Volume Update (X):  " + BMVNoBytes.count_volume_update + "\n"
//                                + "     Orders Cancellation (D):  " + BMVNoBytes.count_orders_cancellation + "\n"
//                                + "     Instruments Statistics (R):  " + BMVNoBytes.count_instruments_statistics + "\n"
//                                + "     INAV's (G):  " + BMVNoBytes.count_inavs + "\n"
//                                + "     General Indexes (U):  " + BMVNoBytes.count_general_indexes + "\n"
//                                + "     Indexes Samples (W):  " + BMVNoBytes.count_indexes_samples + "\n"
//                                + "     Open Interest (I):  " + BMVNoBytes.count_open_interest + "\n"
//                                + "     Public Offerings (B):  " + BMVNoBytes.count_public_offerings + "\n"
//                                + "     Futures Operations and Swaps Derivatives Market (d):  " + BMVNoBytes.count_futures_operations_and_swaps_derivatives_market + "\n"
//                                + "     Derivatives Market Strategies (g):  " + BMVNoBytes.count_derivatives_market_strategies + "\n"
//                                + "     Dollar Buy Sell (r):  " + BMVNoBytes.count_dollar_buy_sell + "\n"
//                                + "     Short Sales Balances per Instrument (s):  " + BMVNoBytes.count_short_sales_balances_per_instrument + "\n"
//                                + "     Capital Markets Multiples (t):  " + BMVNoBytes.count_capital_markets_multiples + "\n"
//                                + "     Benchmarks (x):  " + BMVNoBytes.count_benchmarks + "\n"
//                                + "     Capitalization Rules (z):  " + BMVNoBytes.count_capitalization_rules + "\n"
//                                + "     Global and Local Catalog(J):  " + BMVNoBytes.count_global_and_local_catalog + "\n"
//                                + "     Turn Over Ratio per Security (o):  " + BMVNoBytes.count_turn_over_ratio_per_security + "\n"
//                                + "     Securities Suspension (K):  " + BMVNoBytes.count_securities_suspension + "\n"
//                                + "     Securities Unsuspension (l):  " + BMVNoBytes.count_securities_unsuspension + "\n"
//                                + "     Negotiation State Change SUB-RM (L):  " + BMVNoBytes.count_negotiation_state_change_SUBRM
//                                + "\n");
                        int mip = new BigInteger(messagesInPackage).intValue();
                        byte messagehelper[] = messages;
                        if (mip != 0) {
                            if (myLastReceived + 1 < seqNum.intValue()) {
                                int gap = seqNum.intValue() - myLastReceived-1;
                                int request = myLastReceived + 1;
                                myLastReceived = request + gap;

                                argsRecoveryGap[7] = request + "";
                                argsRecoveryGap[8] = gap + "";

                                System.out.println("[RECOVERY]{{\"GAP\":{\"Quantity\":" + gap + ",\"First Sequence\":" + request + ",\"Final Sequence\":" + (seqNum.intValue() - 1) + "}}");

                                if (gap > 1 && gap < 20000) {
//                                    
//                                    String [] argsRecovery=argsRecoveryGap;
//                                    ListRetransmissions.add(argsRecovery);

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {

                                                 new UnicastBMVNoBytes().StartHandling(argsRecoveryGap);

                                            } catch (Exception ex) {
                                                System.out.println("[Error]{{\"Error\":{\"Description\":" + ex + "}}");
                                            }
                                        }

                                    }).start();

                                } else if (gap > 20000) {

//                                    new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            try {
//
//                                                 new UnicastBMVNoBytes().StartHandling(argsRecoverySnapshot);
//
//                                            } catch (IOException ex) {
//                                                System.out.println("[Error]{{\"Error\":{\"Description\":" + ex + "}}");
//                                            }
//                                        }
//
//                                    }).start();
                                }

                            }
                            String jsonSonstructor = "";
                            jsonSonstructor = jsonSonstructor + "{";
                            jsonSonstructor = jsonSonstructor + "\"Next SeqNum\":" + seqNum + ",";
                            jsonSonstructor = jsonSonstructor + "\"Message\":{";
                            jsonSonstructor = jsonSonstructor + "\"Name\":\"Timestamp\",";

                            jsonSonstructor = jsonSonstructor + "\"Type\":";
                            jsonSonstructor = jsonSonstructor + "\"T\"";
                            jsonSonstructor = jsonSonstructor + ",";
                            jsonSonstructor = jsonSonstructor + "\"Timestamp\":";
                            jsonSonstructor = jsonSonstructor + new BigInteger(timestamp);
                            jsonSonstructor = jsonSonstructor + "}}";
                            System.out.println("," + jsonSonstructor);

                            for (int x = 1; x <= mip; x++) {
                                int size = new BigInteger(Arrays.copyOfRange(messagehelper, 0, 2)).intValue();
                                byte messageToDecode[] = Arrays.copyOfRange(messagehelper, 2, size + 2);
                                ByteBuffer message = ByteBuffer.wrap(messageToDecode);

                                Potocol protocol = new BMVNoBytes();
                                String JSON = protocol.parse(message, Long.parseLong(seqNum + ""));
                                System.out.println("," + JSON);

                                myLastReceived = seqNum.intValue();
                                seqNum = seqNum.add(BigInteger.valueOf(1));
                                if (mip > 1) {

                                    messages = Arrays.copyOfRange(messagehelper, size + 2, messagehelper.length);
                                    messagehelper = messages;
                                } else {
                                    break;
                                }
                            }
                        } else {
                            if (myLastReceived < seqNum.intValue()) {
                                int gap = seqNum.intValue() - myLastReceived-1;
                                int request = myLastReceived + 1;
                                myLastReceived = request + gap;

                                argsRecoveryGap[7] = request + "";
                                argsRecoveryGap[8] = gap + "";

                                System.out.println("[RECOVERY]{{\"GAP\":{\"Quantity\":" + gap + ",\"First Sequence\":" + request + ",\"Final Sequence\":" + (seqNum.intValue()-1) + "}}");

                                if (gap > 1 && gap < 20000) {

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {

                                                new UnicastBMVNoBytes().StartHandling(argsRecoveryGap);

                                            } catch (Exception ex) {
                                                System.out.println("[Error]{{\"Error\":{\"Description\":" + ex + "}}");
                                            }
                                        }

                                    }).start();

                                } else if (gap > 20000) {

//                                    try {
//
//                                        UnicastBMVNoBytes.StartHandling(argsRecoverySnapshot);
//
//                                    } catch (IOException ex) {
//                                        System.out.println("[Error]{{\"Error\":{\"Description\":" + ex + "}}");
//                                    }
                                }

                            }
                        }

                        packet = new DatagramPacket(new byte[64000], 64000);
                    }

                } catch (IOException | NumberFormatException e) {

                    if (e.getMessage().equals("Receive timed out")) {
                        System.out.println("]");
                        System.out.println("Time Out");

                    } else {

                        System.out.println("]");
                        System.out.println("While Connected: " + e);
                    }
                }

            }

        } catch (Exception e) {
            if (e.getMessage().equals("Receive timed out")) {
                System.out.println("]");
                System.out.println("Time Out");
            } else {
                System.out.println("]");
                System.out.println("While Connected: " + e);
            }

        }
    }

    public static void GapChecker() throws UnknownHostException, IOException, InterruptedException {

        String argsRecovery[];
        GapsChecking = true;

        while (!ListRetransmissions.isEmpty()) {
            argsRecovery = ListRetransmissions.get(0);
            ListRetransmissions.remove(0);
            try {

                new UnicastBMVNoBytes().StartHandling(argsRecovery);
                Thread.sleep(1000);

            } catch (IOException ex) {
                System.out.println("[Error]{{\"Error\":{\"Description\":" + ex + "}}");
            }

        }
        GapsChecking = false;
    }
}
