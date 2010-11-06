// $Id: SocketEventSupplier.java,v 1.1 2006/02/18 07:00:34 matvey Exp $
/**
 * Date: 14.05.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.thesis.automatons.net;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import matvey.thesis.automatons.core.AutomatonEvent;
import matvey.thesis.automatons.core.AutomatonEventReceiver;
import matvey.thesis.automatons.core.AutomatonEventSupplier;

/**
 * @author Matvey Kazakov
 */
public class SocketEventSupplier implements AutomatonEventSupplier{
    private int port;
    private List<AutomatonEventReceiver> receivers = new ArrayList<AutomatonEventReceiver>();

    public SocketEventSupplier(int port) {
        this.port = port;
        new EventListenerThread().start();
    }

    public void addReceiver(AutomatonEventReceiver receiver) {
        synchronized(receivers) {
            receivers.add(receiver);
        }
    }

    public void removeReceiver(AutomatonEventReceiver receiver) {
        synchronized(receivers) {
            receivers.remove(receiver);
        }
    }

    private void fireEvent(AutomatonEvent event) {
        AutomatonEventReceiver[] automatonEventReceivers = receivers.toArray(new AutomatonEventReceiver[receivers.size()]);
        for(AutomatonEventReceiver receiver: automatonEventReceivers) {
            receiver.sendEvent(event);
        }
    }

    private class EventListenerThread extends Thread {

        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                while (!interrupted()) {
                    Socket socket = serverSocket.accept();
                    InputStream inputStream = socket.getInputStream();
                    AutomatonEvent event = (AutomatonEvent)new ObjectInputStream(inputStream).readObject();
                    socket.close();
                    fireEvent(event);
                }
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

/*
 * $Log: SocketEventSupplier.java,v $
 * Revision 1.1  2006/02/18 07:00:34  matvey
 * just added
 *
 */