/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pongmult;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author c3rea
*/

public interface RegisterInterface extends Remote {
    public String delete(int id) throws RemoteException;
    public String insert(String GamerTag,String Fname,String Lname, String Email,String Plainpassword) throws RemoteException;
    public String update(int id,String Fname,String Lname, String Email) throws RemoteException;
    public ArrayList search(int id) throws RemoteException;
    public boolean Login (String GamerTag,String password) throws RemoteException;
 
}
