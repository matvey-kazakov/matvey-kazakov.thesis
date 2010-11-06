// $Id: SocketEventReceiver.java,v 1.1 2006/02/18 07:00:34 matvey Exp $
/**
 * Date: 14.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.net;

import matvey.thesis.automatons.core.AutomatonEvent;
import matvey.thesis.automatons.core.AutomatonEventReceiver;

import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectOutputStream;

/**
 * @author Matvey Kazakov
 */
public class SocketEventReceiver implements AutomatonEventReceiver {
    private int port;
    private Socket socket;
    private InetAddress host;


    public SocketEventReceiver(String hostName, int port) throws UnknownHostException {
        this.port = port;
        host = InetAddress.getByName(hostName);
    }

    public void sendEvent(AutomatonEvent event) {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
            socket = null;
        }
        if (socket != null) {
            try {
                OutputStream outputStream = socket.getOutputStream();
                new ObjectOutputStream(outputStream).writeObject(event);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

/*
 * $Log: SocketEventReceiver.java,v $
 * Revision 1.1  2006/02/18 07:00:34  matvey
 * just added
 *
 */