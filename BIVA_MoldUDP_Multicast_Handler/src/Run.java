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
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Run {

    public static void main(String args[]) {

        if (args.length != 6) {
            System.out.println("Arguments: Group Port ReceiverInterfaceIP FolderName(XXXXFeedA/XXXXFeedB) IPRecovery PortRecovery");

            System.out.println(args.length);
            System.out.println(Arrays.toString(args));
            System.exit(0);
        }

        try {
            String myargs[] = new String[7];
            myargs[0] = args[0];
            myargs[1] = args[1];
            myargs[2] = "BIVA TV Multicast";
            myargs[3] = args[2];
            myargs[4] = args[3];
            myargs[5] = args[4];
            myargs[6] = args[5];

            MoldUDP.StartHandling(myargs);
        } catch (IOException ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
