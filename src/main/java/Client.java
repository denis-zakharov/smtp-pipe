/*
 * Copyright (c) 1996, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * msgmultisendsample creates a simple multipart/mixed message and sends it.
 * Both body parts are text/plain.
 * <p>
 * usage: <code>java msgmultisendsample <i>to from smtp true|false</i></code>
 * where <i>to</i> and <i>from</i> are the destination and
 * origin email addresses, respectively, and <i>smtp</i>
 * is the hostname of the machine that has smtp server
 * running.  The last parameter either turns on or turns off
 * debugging during sending.
 *
 * @author	Max Spivak
 */
public class Client {
    static String msgText1 = "This is a message body.\nHere's line two.";
    static String msgText2 = "This is the text in the message attachment.";

    public static void main(String[] args) throws NoSuchProviderException {
/*        if (args.length != 4) {
            System.out.println("usage: java msgmultisend <to> <from> <smtp> true|false");
            return;
        }

        String to = args[0];
        String from = args[1];
        String host = args[2];
        boolean debug = Boolean.valueOf(args[3]).booleanValue();*/
        String[] to = new String[]{"denis.zakharov@db.com", "asya.berezova@db.com", "dizaharov@gmail.com"};
        String from = "dizaharov@gmail.com";
        String host = "localhost";
        int port = 26000;
        boolean debug = true;

        // create some properties and get the default Session
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, null);
        session.setDebug(debug);


        try (Transport transport = session.getTransport("smtp")) {
            transport.connect();
            for (int i = 0; i < to.length; i++) {
                String toAddr = to[i];
                // create a message
                MimeMessage msg = new MimeMessage(session);

                msg.setFrom(new InternetAddress(from));
                InternetAddress[] address = {new InternetAddress(toAddr)};
                msg.setRecipients(Message.RecipientType.TO, address);
                msg.setSubject("JavaMail APIs Multipart Test " + i);
                msg.setSentDate(new Date());

                // create and fill the first message part
                MimeBodyPart mbp1 = new MimeBodyPart();
                mbp1.setText(msgText1);

                // create and fill the second message part
                MimeBodyPart mbp2 = new MimeBodyPart();
                // Use setText(text, charset), to show it off !
                mbp2.setText(msgText2, "us-ascii");

                // create the Multipart and its parts to it
                Multipart mp = new MimeMultipart();
                mp.addBodyPart(mbp1);
                mp.addBodyPart(mbp2);

                // add the Multipart to the message
                msg.setContent(mp);

                // send the message
                //Transport.send(msg);
                transport.sendMessage(msg, address);
            }
        } catch (MessagingException mex) {
            mex.printStackTrace();
            Exception ex = null;
            if ((ex = mex.getNextException()) != null) {
                ex.printStackTrace();
            }
        }
    }
}
