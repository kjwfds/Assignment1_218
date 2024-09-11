package pong.server;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import pong.rmi.PongRemoteServer;

public class PongServer {

    public static void main(String args[]) throws Exception {

        try {
           
                // Set the system property for RMI server hostname
            String serverIP = "192.168.108.220"; // Replace with the server's LAN IP address
            System.setProperty("java.rmi.server.hostname", serverIP);

            // Create and bind the remote object
                 Remote remote = new PongRemoteServer();
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.bind("RegisterInterface", remote);

            System.out.println("Server is running at " + serverIP + ":1099");
        } catch (RemoteException ex) {
            Logger.getLogger(PongServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
