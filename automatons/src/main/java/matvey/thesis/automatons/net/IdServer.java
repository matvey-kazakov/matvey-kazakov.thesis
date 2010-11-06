// $Id: IdServer.java,v 1.1 2006/02/18 07:00:34 matvey Exp $
/**
 * Date: 28.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.net;

import matvey.thesis.automatons.core.local.LocalAutomatonIdFactory;
import matvey.utils.MKLogger;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.logging.Logger;

/**
 * @author Matvey Kazakov
 */
public class IdServer extends Thread {

    public static final int ID_SERVER_PORT = 20001;
    private static Logger logger = MKLogger.createLogger("IdServer");

    public static void main(String[] args) throws IOException {
        IdServer idServer = new IdServer();
        idServer.start();
    }

    private LocalAutomatonIdFactory localAutomatonIdFactory = new LocalAutomatonIdFactory(0);


    public IdServer() {
        super("IdServer");
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(ID_SERVER_PORT);
            while (!interrupted()) {
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                Object request = new ObjectInputStream(inputStream).readObject();
                Object response = null;
                if (request instanceof String) {
                    response = localAutomatonIdFactory.getId((String)request);
                } else {
                    response = localAutomatonIdFactory.getSID(((Long)request).longValue());
                }
                logger.finer(request + " -> " + response);
                OutputStream outputStream = socket.getOutputStream();
                new ObjectOutputStream(outputStream).writeObject(response);
                socket.close();
            }
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

/*
 * $Log: IdServer.java,v $
 * Revision 1.1  2006/02/18 07:00:34  matvey
 * just added
 *
 */