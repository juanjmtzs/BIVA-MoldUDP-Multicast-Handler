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
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class RequesterMoldUDP {

    private static InetAddress group;
    private static DatagramSocket clientSocket;

    public static void kill() throws IOException {
        clientSocket.close();

    }

    public static void StartHandling(String args[]) throws Exception {

        clientSocket = new DatagramSocket();
        clientSocket.setSoTimeout(2000);
        InetAddress IPAddress = InetAddress.getByName(args[0]);
        byte[] sendData = new byte[20];
        byte[] receiveData = new byte[64000];

        byte[] sessBytes = new byte[10];

        String session = args[2];
        int sequence = Integer.parseInt(args[3]);
        int messages = Integer.parseInt(args[4]);
        sessBytes = session.getBytes();

        int seqHelper = (int) sequence;
        int messagHelper = (int) messages - (int) sequence;

        try {
            while (seqHelper <= (int) sequence + messagHelper) {

                sendData = buildRequest(sessBytes, (long) seqHelper, (short) 60);

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, Integer.parseInt(args[1]));

                clientSocket.send(sendPacket);
                DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(packet);

                int packetSize = packet.getLength();
                byte sessionRec[] = new byte[10];
                byte sequenceRec[] = new byte[8];
                byte messagesInPackage[] = new byte[2];
                byte messagesRec[] = new byte[packet.getLength() - 20];

                sessionRec = Arrays.copyOfRange(packet.getData(), 0, 10);
                sequenceRec = Arrays.copyOfRange(packet.getData(), 10, 18);
                messagesInPackage = Arrays.copyOfRange(packet.getData(), 18, 20);
                messagesRec = Arrays.copyOfRange(packet.getData(), 20, packetSize);

                BigInteger seqNum = new BigInteger(sequenceRec);
                seqNum = seqNum.subtract(BigInteger.valueOf(1));

                int mip = new BigInteger(messagesInPackage).intValue();
                byte messagehelper[] = messagesRec;
                for (int y = 1; y <= mip; y++) {

                    new BigInteger(Arrays.copyOfRange(messagehelper, 0, 2)).intValue();
                    int size = new BigInteger(Arrays.copyOfRange(messagehelper, 0, 2)).intValue();
                    byte messageToDecode[] = Arrays.copyOfRange(messagehelper, 2, size + 2);
                    ByteBuffer message = ByteBuffer.wrap(messageToDecode);

                    seqNum = seqNum.add(BigInteger.valueOf(1));
                    if (seqNum.intValue() == (int) messages + 1) {
                        break;
                    }

                    Potocol protocol = (Potocol) new DecoderITCHBIVA();
                    String JSON = protocol.parse(message, Long.parseLong(seqNum + ""));
                    System.out.println("," + JSON);

                    if (mip > 1) {
                        messagesRec = new byte[messagehelper.length - size - 2];
                        messagesRec = Arrays.copyOfRange(messagehelper, size + 2, messagehelper.length);
                        messagehelper = messagesRec;
                    } else {
                        break;
                    }
                }

                seqHelper = seqNum.intValue() + 1;

            }

            clientSocket.close();
            CompletedRequest();
        } catch (Exception e) {

            System.out.println("Something Went Wrong: " + "\n\n" + e);

        }
    }

    public static void CompletedRequest() {
        System.out.println("[RECOVERY]{\"Quantity\":\"Retransmission Completed.\"}");
    }

    static byte[] buildRequest(byte[] session, long sequenceNumber, short messageCount) {
        ByteBuffer buf = ByteBuffer.allocate(20);

        for (int i = 0; i < Math.max(0, 10 - session.length); ++i) {
            buf.put((byte) ' ');
        }
        for (int i = 0; i < Math.min(10, session.length); ++i) {
            buf.put(session[i]);
        }

        buf.putLong(sequenceNumber);

        buf.putShort(messageCount);

        return buf.array();
    }

}
