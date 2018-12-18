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
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;

public class UnicastBMVNoBytes {

    private final String Username;
    private final String Password;
    private final String Group;
    private static long nextExpectedSequenceNumber = 0;
    private static ByteBuffer buffer;
    public static Socket clientSocket;
    private static volatile boolean connected = false;
    final private Potocol protocol;
    private static String service;

    private static final int BMV_GROUP_LENGTH = 1;
    private static final int BMV_USER_LENGTH = 6;
    private static final int BMV_PASSWORD_LENGTH = 10;
    private static final int BMV_LOGIN_REQUEST_LENGTH = 19;
    private static final int BMV_LOGIN_LENGTH = BMV_USER_LENGTH + BMV_PASSWORD_LENGTH + BMV_GROUP_LENGTH;

    private static final int BMV_SEQUENCE_LENGTH = 4;
    private static final int BMV_QUANTITY_LENGTH = 2;
    private static final int BMV_RESEND_REQUEST_LENGTH = 9;
    private static final int BMV_RESEND_LENGTH = BMV_SEQUENCE_LENGTH + BMV_QUANTITY_LENGTH + BMV_GROUP_LENGTH;

    private static final int BMV_INSTRUMENT_LENGTH = 4;
    private static final int BMV_SNAPSHOT_TYPE_LENGTH = 1;
    private static final int BMV_SNAPSHOT_REQUEST_LENGTH = 8;
    private static final int BMV_SNAPSHOT_LENGTH = BMV_INSTRUMENT_LENGTH + BMV_SNAPSHOT_TYPE_LENGTH + BMV_GROUP_LENGTH;

    private static final int BMV_SAMPLE_LENGTH = 1;
    private static final int BMV_SNAPSHOT_HOUR_LENGTH = 8;
    private static final int BMV_INDEX_SNAPSHOT_REQUEST_LENGTH = 12;
    private static final int BMV_INDEX_SNAPSHOT_LENGTH = BMV_SAMPLE_LENGTH + BMV_SNAPSHOT_HOUR_LENGTH + BMV_GROUP_LENGTH;

    private static final int BMV_LOGOUT_LENGTH = 2;

    private static final int BMV_LOGIN_RESPONSE_LENGTH = 2;

    public static String retransmissionQuantity = "";

    private static byte session[];
    public static String firstMessage = "", quantity = "", security = "", type = "";

    public UnicastBMVNoBytes() {
        Username = null;
        Password = null;
        Group = null;
        protocol = null;
    }

    public UnicastBMVNoBytes(String username, String password, String group, String host, int port, Potocol protocol, String receiverIP) throws IOException {
        Username = username;
        Password = password;
        Group = group;
        if (!receiverIP.equals("") && !receiverIP.equals(" ") && !receiverIP.equals("0.0.0.0") && !receiverIP.equals("  ") && !receiverIP.equals("   ") && !receiverIP.equals("    ")) {
            clientSocket.bind(new InetSocketAddress(InetAddress.getByName(receiverIP), 0));
        }
        clientSocket.connect(new InetSocketAddress(host, port), 300000);
        connected = true;
        buffer.order(ByteOrder.BIG_ENDIAN);
        this.protocol = protocol;
    }

    public UnicastBMVNoBytes(String username, String password, String group, String host, int port, Potocol protocol, String receiverIP, String firstMessage, String quantity) throws IOException {
        this.firstMessage = firstMessage;
        this.quantity = quantity;
        Username = username;
        Password = password;
        Group = group;
        if (!receiverIP.equals("") && !receiverIP.equals(" ") && !receiverIP.equals("0.0.0.0") && !receiverIP.equals("  ") && !receiverIP.equals("   ") && !receiverIP.equals("    ")) {
            clientSocket.bind(new InetSocketAddress(InetAddress.getByName(receiverIP), 0));
        }
        clientSocket.connect(new InetSocketAddress(host, port), 300000);
        connected = true;
        buffer.order(ByteOrder.BIG_ENDIAN);
        this.protocol = protocol;
    }

    public UnicastBMVNoBytes(String username, String password, String group, String host, int port, Potocol protocol, String receiverIP, String security, String type, String flag) throws IOException {
        this.security = security;
        this.type = type;
        Username = username;
        Password = password;
        Group = group;
        if (!receiverIP.equals("") && !receiverIP.equals(" ") && !receiverIP.equals("0.0.0.0") && !receiverIP.equals("  ") && !receiverIP.equals("   ") && !receiverIP.equals("    ")) {
            clientSocket.bind(new InetSocketAddress(InetAddress.getByName(receiverIP), 0));
        }
        clientSocket.connect(new InetSocketAddress(host, port), 300000);
        connected = true;
        buffer.order(ByteOrder.BIG_ENDIAN);
        this.protocol = protocol;
    }

    private static class PacketType {

        public static final byte BMV_login_response_type = '&';
        public static final byte BMV_resend_request_response_type = '*';
        public static final byte BMV_snapshot_request_response_type = '+';
        public static final byte BMV_snapshot_completed_type = '?';
        public static final byte BMV_login_request_type = '!';
        public static final byte BMV_resend_request_type = '#';
        public static final byte BMV_snapshot_request_type = '$';
        public static final byte BMV_index_snapshot_request_type = '-';
        public static final byte BMV_logout_request_type = '%';

    }

    private static class Packet {

        private final Charset DEFAULT_CHARSET = Charset.forName("US-ASCII");
        private static final byte PADDING = ' ';
        private byte type;
        private int length = 1;

        Packet(byte type, int length) {
            this.type = type;
            this.length += length;
        }

        Packet(byte type) {
            this.type = type;
        }

        void padRight(ByteBuffer buffer, String s, long length) {
            buffer.put(s.getBytes(DEFAULT_CHARSET));
            for (int i = s.length(); i < length; ++i) {
                buffer.put(PADDING);
            }
        }

        void padLeft(ByteBuffer buffer, String s, long length) {
            for (int i = s.length(); i < length; ++i) {
                buffer.put(PADDING);
            }
            buffer.put(s.getBytes(DEFAULT_CHARSET));
        }

        String stripLeft(byte[] buffer, int offset, int length) {
            int i = offset;
            for (; i < length + offset; i++) {
                if (buffer[i] != PADDING) {
                    break;
                }
            }
            return new String(buffer, i, length - (i - offset), DEFAULT_CHARSET);
        }

        String stripRight(byte[] buffer, int offset, int length) {
            int i = offset + length - 1;
            for (; i >= offset; i--) {
                if (buffer[i] != PADDING) {
                    break;
                }
            }
            return new String(buffer, offset, i - offset + 1, DEFAULT_CHARSET);
        }

        void encode(ByteBuffer buffer) {
            buffer.put((byte) length);
            buffer.put(type);
        }

        void encode(ByteBuffer buffer, int length) {
            buffer.put((byte) length);
            buffer.put(type);
        }

        void decode(byte[] buffer) {
        }
    }

    public static void close() {
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException ignore) {
            }

        }
    }

    private static void sendBuffer() throws IOException {
        send(buffer);
    }

    private static void send(ByteBuffer b) throws IOException {
        clientSocket.getOutputStream().write(b.array(), 0, b.position());
        clientSocket.getOutputStream().flush();
        b.clear();
    }

    private byte readBytes(DataInputStream in) throws IOException {
        for (;;) {
            try {
                return in.readByte();
            } catch (SocketTimeoutException e) {

            }
        }
    }

    private void loopFactory() throws IOException {
        final DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        while (connected == true && clientSocket.isConnected() == true) {
            byte[] header = new byte[17];
            byte len[];

            in.readFully(header, 0, 17);
            len = Arrays.copyOfRange(header, 0, 2);
            byte[] messages = new byte[new BigInteger(len).intValue() - 17];
            in.readFully(messages);
            byte type = messages[2];

            byte grupo[];
            byte sequence[];
            byte messagesInPackage[];
            byte timestamp[];

            len = Arrays.copyOfRange(header, 0, 2);
            messagesInPackage = Arrays.copyOfRange(header, 2, 3);
            grupo = Arrays.copyOfRange(header, 3, 4);
            session = Arrays.copyOfRange(header, 4, 5);
            sequence = Arrays.copyOfRange(header, 5, 9);
            timestamp = Arrays.copyOfRange(header, 9, 17);

            BigInteger seqNum = new BigInteger(sequence);
            //seqNum = seqNum.subtract(BigInteger.valueOf(1));

            ByteBuffer bb = ByteBuffer.allocate(new BigInteger(len).intValue() + 2);
            bb.put(header);
            bb.put(messages);

            switch (type) {

                case PacketType.BMV_login_response_type:
                    System.out.println(""
                            + "[RECOVERY-IN]{"
                            + "\"Message\":{"
                            + "\"Name\":" + "\"" + "Login Response" + "\"" + ","
                            + "\"Type\":" + "\"" + "&" + "\"" + ","
                            + "\"Status\":" + "\"" + (char) messages[3] + "\""
                            + "}}"
                    );
                    if (((char) messages[3]) == 'A') {
                        if (!"".equals(firstMessage) && !"".equals(quantity)) {
                            ResendRequest(Group, firstMessage, quantity);
                        } else if (!"".equals(security) && !"".equals(type)) {
                            SnapshotRequest(Group, security, UnicastBMVNoBytes.type);
                        }
                        firstMessage = "";
                        quantity = "";
                        security = "";
                        UnicastBMVNoBytes.type = "";
                    }

                    break;
                case PacketType.BMV_resend_request_response_type:
                    retransmissionQuantity = "" + new BigInteger(Arrays.copyOfRange(messages, 8, 10));
                    System.out.println(""
                            + "[RECOVERY-IN]{"
                            + "\"Message\":{"
                            + "\"Name\":" + "\"" + "Resend Request Response" + "\"" + ","
                            + "\"Type\":" + "\"" + "*" + "\"" + ","
                            + "\"Group\":" + "\"" + new BigInteger(Arrays.copyOfRange(messages, 3, 4)) + "\"" + ","
                            + "\"First Message\":" + "\"" + new BigInteger(Arrays.copyOfRange(messages, 4, 8)) + "\"" + ","
                            + "\"Quantity\":" + "\"" + new BigInteger(Arrays.copyOfRange(messages, 8, 10)) + "\"" + ","
                            + "\"Status\":" + "\"" + (char) messages[10] + "\""
                            + "}}"
                    );

                    break;
                case PacketType.BMV_snapshot_request_response_type:
                    System.out.println(""
                            + "[RECOVERY-IN]{"
                            + "\"Message\":{"
                            + "\"Name\":" + "\"" + "Snapshot Request Response" + "\"" + ","
                            + "\"Type\":" + "\"" + "+" + "\"" + ","
                            + "\"Quantity\":" + "\"" + new BigInteger(Arrays.copyOfRange(messages, 3, 7)) + "\"" + ","
                            + "\"Status\":" + "\"" + (char) messages[7] + "\"" + ","
                            + "\"Type\":" + "\"" + new BigInteger(Arrays.copyOfRange(messages, 8, 9)) + "\""
                            + "}}"
                    );
                    break;

                case PacketType.BMV_snapshot_completed_type:
                    System.out.println(""
                            + "[RECOVERY-IN]{"
                            + "\"Message\":{"
                            + "\"Name\":" + "\"" + "Snapshot Completed" + "\"" + ","
                            + "\"Type\":" + "\"" + "?" + "\"" + ","
                            + "\"Sequence Number\":" + "\"" + new BigInteger(Arrays.copyOfRange(messages, 3, 7)) + "\"" + ","
                            + "\"Market Data Group\":" + "\"" + new BigInteger(Arrays.copyOfRange(messages, 7, 8)) + "\"" + ","
                            + "\"Type\":" + "\"" + new BigInteger(Arrays.copyOfRange(messages, 8, 9)) + "\""
                            + "}}"
                    );

                    break;
                default:
                    if (Long.parseLong(seqNum + "") == 0) {
                        break;
                    }
                    int mip = new BigInteger(messagesInPackage).intValue();
                    byte messagehelper[] = messages;
                    for (int x = 1; x <= mip; x++) {
                        int size = new BigInteger(Arrays.copyOfRange(messagehelper, 0, 2)).intValue();
                        byte messageToDecode[] = Arrays.copyOfRange(messagehelper, 2, size + 2);
                        ByteBuffer message = ByteBuffer.wrap(messageToDecode);

                        Potocol protocol = new DecoderINTRABMV();
                        String JSONmessage = protocol.parse(message, Long.parseLong(seqNum + ""));
                        System.out.println("," + JSONmessage);

                        if (mip > 1) {

                            messages = Arrays.copyOfRange(messagehelper, size + 2, messagehelper.length);
                            messagehelper = messages;
                        } else {
                            break;
                        }
                        seqNum = seqNum.add(BigInteger.valueOf(1));
                    }
                    break;
            }
        }
    }

    public static void LoginRequest(String group, String user, String password) throws IOException {
        if (clientSocket != null) {

            if (clientSocket.isConnected() == true) {

                try {
                    final Packet login = new Packet(PacketType.BMV_login_request_type);
                    final ByteBuffer ByteBufferAux;
                    ByteBufferAux = ByteBuffer.allocate(BMV_LOGIN_REQUEST_LENGTH);
                    login.encode(ByteBufferAux, BMV_LOGIN_REQUEST_LENGTH);

                    ByteBufferAux.put(Byte.valueOf(group));
                    login.padRight(ByteBufferAux, user, BMV_USER_LENGTH);
                    login.padRight(ByteBufferAux, password, BMV_PASSWORD_LENGTH);
                    System.out.println("[RECOVERY-OUT]{"
                            + "\"Message\":{"
                            + "\"Name\":" + "\"" + "Login Request" + "\"" + ","
                            + "\"Type\":" + "\"" + "!" + "\"" + ","
                            + "\"Group\":" + group + ","
                            + "\"User\":" + "\"" + user + "\"}}");

                    send(ByteBufferAux);

                } catch (Exception e) {

                    String messageError = "Socket closed" + "\n" + e;

                }
            } else {
                String messageError = "Socket closed" + "\n";

            }

        } else {

            try {
                String messageError = "Socket closed" + "\n";
            } catch (Exception e) {
            }

        }

    }

    public static void ResendRequest(String group, String firstMessage, String quantity) throws IOException {
        if (clientSocket != null) {

            if (clientSocket.isConnected() == true) {

                try {
                    final Packet resend = new Packet(PacketType.BMV_resend_request_type);
                    final ByteBuffer ByteBufferAux;
                    ByteBufferAux = ByteBuffer.allocate(BMV_RESEND_REQUEST_LENGTH);
                    resend.encode(ByteBufferAux, BMV_RESEND_REQUEST_LENGTH);

                    ByteBufferAux.put(Byte.valueOf(group));
                    ByteBufferAux.putInt(Integer.parseInt(firstMessage));
                    ByteBufferAux.putShort(Short.parseShort(quantity));

                    System.out.println(""
                            + "[RECOVERY-OUT]{"
                            + "\"Message\":{"
                            + "\"Name\":" + "\"" + "Resend Request" + "\"" + ","
                            + "\"Type\":" + "\"" + "!" + "\"" + ","
                            + "\"Group\":" + group + ","
                            + "\"First Message\":" + "\"" + firstMessage + "\"" + ","
                            + "\"Quantity\":" + "\"" + quantity + "\""
                            + "}}");

                    send(ByteBufferAux);

                } catch (Exception e) {

                    String messageError = "Socket closed" + "\n" + e;

                }
            } else {
                String messageError = "Socket closed" + "\n";
            }

        } else {

            try {
                String messageError = "Socket closed" + "\n";
            } catch (Exception e) {
                System.out.println("" + e);
            }

        }

    }

    public static void SnapshotRequest(String group, String instrument, String type) throws IOException {
        if (clientSocket != null) {

            if (clientSocket.isConnected() == true) {

                try {
                    final Packet resend = new Packet(PacketType.BMV_snapshot_request_type);
                    final ByteBuffer ByteBufferAux;
                    ByteBufferAux = ByteBuffer.allocate(BMV_SNAPSHOT_REQUEST_LENGTH);
                    resend.encode(ByteBufferAux, BMV_SNAPSHOT_REQUEST_LENGTH);

                    ByteBufferAux.put(Byte.valueOf(group));
                    ByteBufferAux.putInt(Integer.parseInt(instrument));
                    ByteBufferAux.put(Byte.valueOf(type));

                    System.out.println(""
                            + "[RECOVERY-OUT]{"
                            + "\"Message\":{"
                            + "\"Name\":" + "\"" + "Snapshot Request" + "\"" + ","
                            + "\"Type\":" + "\"" + "$" + "\"" + ","
                            + "\"Group\":" + group + ","
                            + "\"Instrument ID\":" + "\"" + instrument + "\"" + ","
                            + "\"Snapshot Type\":" + "\"" + type + "\""
                            + "}}");

                    send(ByteBufferAux);

                } catch (Exception e) {

                    String messageError = "Socket closed" + "\n" + e;

                }
            } else {
                String messageError = "Socket closed" + "\n";
            }

        } else {

            try {
                String messageError = "Socket closed" + "\n";
            } catch (Exception e) {
                System.out.println("" + e);
            }

        }

    }

    public static void IndexSnapshotRequest(String group, String sample, String hour) throws IOException {
        if (clientSocket != null) {

            if (clientSocket.isConnected() == true) {

                try {
                    final Packet resend = new Packet(PacketType.BMV_index_snapshot_request_type);
                    final ByteBuffer ByteBufferAux;
                    ByteBufferAux = ByteBuffer.allocate(BMV_INDEX_SNAPSHOT_REQUEST_LENGTH);
                    resend.encode(ByteBufferAux, BMV_INDEX_SNAPSHOT_REQUEST_LENGTH);

                    ByteBufferAux.put(Byte.valueOf(group));
                    resend.padRight(ByteBufferAux, sample, 1);
                    ByteBufferAux.putLong(Long.parseLong(hour));

                    System.out.println(""
                            + "[RECOVERY-OUT]{"
                            + "\"Message\":{"
                            + "\"Name\":" + "\"" + "Snapshot Request" + "\"" + ","
                            + "\"Type\":" + "\"" + "$" + "\"" + ","
                            + "\"Group\":" + group + ","
                            + "\"Sample\":" + "\"" + sample + "\"" + ","
                            + "\"Recovery Hour\":" + "\"" + hour + "\""
                            + "}}");

                    send(ByteBufferAux);

                } catch (Exception e) {

                    String messageError = "Socket closed" + "\n" + e;

                }
            } else {
                String messageError = "Socket closed" + "\n";
            }

        } else {

            try {
                String messageError = "Socket closed" + "\n";
            } catch (Exception e) {
                System.out.println("" + e);
            }

        }

    }

    private static void logoff() {
        try {
            final Packet logoff = new Packet(PacketType.BMV_logout_request_type);
            final ByteBuffer ByteBufferAux;
            ByteBufferAux = ByteBuffer.allocate(BMV_LOGOUT_LENGTH);
            logoff.encode(ByteBufferAux);
            //System.out.println("[RECOVERY-OUT] Logoff: " + Arrays.toString(ByteBufferAux.array()));
            send(ByteBufferAux);

        } catch (IOException e) {

            System.out.println("[Error]{{\"Error\":{\"Description\":" + e + "}}");

        }
    }

    public void start() throws IOException {
        LoginRequest(Group, Username, Password);
        loopFactory();

    }

    public static void stop() {
        connected = false;
        logoff();
        if (nextExpectedSequenceNumber > 0) {

        }

    }

    public static void kill() {
        stop();
        close();

    }

    public void StartHandling(String[] args) throws IOException {
        clientSocket = new Socket();
        buffer = ByteBuffer.allocate(4 * 1024);

        try {
            final UnicastBMVNoBytes s;
            switch (args.length) {
                case 7:
                    s = new UnicastBMVNoBytes(args[3], args[4], args[5], args[0], Integer.parseInt(args[1]), new DecoderINTRABMV(), args[6]);
                    break;
                case 9:
                    s = new UnicastBMVNoBytes(args[3], args[4], args[5], args[0], Integer.parseInt(args[1]), new DecoderINTRABMV(), args[6], args[7], args[8]);
                    break;
                default:
                    s = new UnicastBMVNoBytes(args[3], args[4], args[5], args[0], Integer.parseInt(args[1]), new DecoderINTRABMV(), args[6], args[7], args[8], args[9]);
                    break;
            }
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    s.stop();
                }
            });
            s.start();
        } catch (IOException | NumberFormatException e) {

            

            System.out.println("[Error]{{\"Error\":{\"Description\":" + e + "}}");
            close();
        }
    }

}
