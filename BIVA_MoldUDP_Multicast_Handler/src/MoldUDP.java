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
import java.io.File;
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
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MoldUDP {

    private static MulticastSocket socket;
    private static InetAddress group;
    public static String receiverIP = "";
    private static boolean keepItUp = true;
    public static int myLastReceived = 0;
    public static String userRecovery, passwordRecovery, ipRecovery, portRecovery, groupRecovery = "";
    public static String session;

    public static void kill() {
        keepItUp = false;
        if (socket.isConnected()) {
            try {
                socket.leaveGroup(group);
            } catch (IOException ex) {
                Logger.getLogger(MoldUDP.class.getName()).log(Level.SEVERE, null, ex);
            }
            socket.disconnect();
            socket.close();
        }

    }

    public static void StartHandling(String[] args) throws UnknownHostException, IOException {
        try {

            myLastReceived = 0;
            ipRecovery = args[5];
            portRecovery = args[6];

            group = InetAddress.getByName(args[0]);
            final int port = Integer.parseInt(args[1]);

            String service = args[2];
            receiverIP = args[3];

            try {
                DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
                Date date = new Date();
                System.setOut(new PrintStream(new File(args[4], Instant.now().toEpochMilli() + "_BIVATVMulticast_" + dateFormat.format(date) + ".log")));
            } catch (Exception e) {
                System.out.println("Error " + e);

            }

            socket = new MulticastSocket(port);
            socket.setInterface(InetAddress.getByName(receiverIP));
            socket.joinGroup(group);
            socket.setSoTimeout(7200000);
            if (socket.isClosed() == true) {
                System.out.println("Disconected");

            } else {

                try {
                    keepItUp = true;

                    DatagramPacket packet = new DatagramPacket(new byte[64000], 64000);
                    System.out.println("[{\"Service Name\":\"BIVA TV Multicast\"}");
                    while (keepItUp != false) {
                        socket.receive(packet);
                        int packetSize = packet.getLength();
                        byte session[] = new byte[10];
                        byte sequence[] = new byte[8];
                        byte messagesInPackage[] = new byte[2];
                        byte messages[] = new byte[packet.getLength() - 20];

                        session = Arrays.copyOfRange(packet.getData(), 0, 10);
                        MoldUDP.session = new String(session);
                        sequence = Arrays.copyOfRange(packet.getData(), 10, 18);
                        messagesInPackage = Arrays.copyOfRange(packet.getData(), 18, 20);
                        messages = Arrays.copyOfRange(packet.getData(), 20, packetSize);

                        BigInteger seqNum = new BigInteger(sequence);
                        seqNum = seqNum.subtract(BigInteger.valueOf(1));

                        int mip = new BigInteger(messagesInPackage).intValue();
                        byte messagehelper[] = messages;

                        if (mip != 0) {
                            if (myLastReceived + 1 < seqNum.intValue()) {

                                int gap = seqNum.intValue() - myLastReceived;
                                int request = myLastReceived + 1;
                                myLastReceived = request + gap;
                                int finalSeq = seqNum.intValue();
                                System.out.println("[RECOVERY]{{\"GAP\":{\"Quantity\":" + gap + ",\"First Sequence\":" + request + ",\"Final Sequence\":" + finalSeq + "}}");
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {

                                            String argsRecovery[] = new String[5];
                                            argsRecovery[0] = ipRecovery;
                                            argsRecovery[1] = portRecovery;
                                            argsRecovery[2] = MoldUDP.session;
                                            argsRecovery[3] = request + "";
                                            argsRecovery[4] = finalSeq + "";
                                            new RequesterMoldUDP().StartHandling(argsRecovery);

                                        } catch (Exception ex) {
                                        }
                                    }

                                }).start();

                            }

                            for (int x = 1; x <= mip; x++) {

                                new BigInteger(Arrays.copyOfRange(messagehelper, 0, 2)).intValue();
                                int size = new BigInteger(Arrays.copyOfRange(messagehelper, 0, 2)).intValue();
                                byte messageToDecode[] = Arrays.copyOfRange(messagehelper, 2, size + 2);
                                ByteBuffer message = ByteBuffer.wrap(messageToDecode);

                                seqNum = seqNum.add(BigInteger.valueOf(1));
                                myLastReceived = seqNum.intValue();
                                Potocol protocol = new DecoderITCHBIVA();
                                String JSON = protocol.parse(message, Long.parseLong(seqNum + ""));
                                System.out.println("," + JSON);

                                if (mip > 1) {
                                    messages = new byte[messagehelper.length - size - 2];
                                    messages = Arrays.copyOfRange(messagehelper, size + 2, messagehelper.length);
                                    messagehelper = messages;
                                } else {
                                    break;
                                }
                            }
                        } else {
                            if (myLastReceived < seqNum.intValue()) {
                                int gap = seqNum.intValue() - myLastReceived;
                                int request = myLastReceived + 1;
                                myLastReceived = request + gap - 1;

                                int finalSeq = seqNum.intValue();
                                System.out.println("[RECOVERY]{{\"GAP\":{\"Quantity\":" + gap + ",\"First Sequence\":" + request + ",\"Final Sequence\":" + finalSeq + "}}");

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {

                                            String argsRecovery[] = new String[5];
                                            argsRecovery[0] = ipRecovery;
                                            argsRecovery[1] = portRecovery;
                                            argsRecovery[2] = MoldUDP.session;
                                            argsRecovery[3] = request + "";
                                            argsRecovery[4] = finalSeq + "";
                                            RequesterMoldUDP.StartHandling(argsRecovery);
                                        } catch (Exception ex) {
                                        }
                                    }

                                }).start();

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
                        System.out.println("While Connected: " + e.getStackTrace());
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
                System.out.println("While Connected: " + e.getStackTrace());
            }

        }
    }
}
