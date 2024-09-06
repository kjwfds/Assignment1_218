/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pongmult;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author c3rea
 */
public class Server {
    
    public static void main(String [] args) throws RemoteException{
      try {
            // Set the system property for RMI server hostname
            String serverIP = "192.168.215.220"; // Replace with the server's LAN IP address
            System.setProperty("java.rmi.server.hostname", serverIP);

            // Create and bind the remote object
            RegisterInterfaceImp R = new RegisterInterfaceImp();
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.bind("RegisterInterface", R);

            System.out.println("Server is running at " + serverIP + ":1099");
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
