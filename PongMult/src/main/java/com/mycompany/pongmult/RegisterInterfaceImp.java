/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pongmult;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;



/**
 *
 * @author c3rea
 */
public class RegisterInterfaceImp extends UnicastRemoteObject implements RegisterInterface  {
    String url = "jdbc:mysql://192.168.215.220:3306/pongusers";

    public RegisterInterfaceImp() throws RemoteException{   
        super();
    }
    
//    @Override
//    public String delete(int id) throws RemoteException {
//    try {       
//                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pongusers", "appuser", "");
//                Statement St = (Statement)con.createStatement();
//                String sql = "DELETE FROM pongapplication WHERE id = ?";
//                PreparedStatement pstmt = con.prepareStatement(sql); 
//                pstmt.setInt(1, id);
//                int rowsAffected = pstmt.executeUpdate();
//        
//                if (rowsAffected > 0) {
//                    return "Account deleted successfully";
//                } else {
//                    return "No account found with the given ID";
//                }
//
//            } catch (SQLException ex) {
//                Logger.getLogger(RegisterInterfaceImp.class.getName()).log(Level.SEVERE, null, ex);
//                return ex.toString();
//            }    
//  }

    @Override
    public String insert(String GamerTag, String Fname, String Lname, String Email,String password) throws RemoteException {
        try {
             Connection con = DriverManager.getConnection(url, "appuser", "");
            Statement St = (Statement)con.createStatement();
             String hashedpassword = BCrypt.hashpw(password, BCrypt.gensalt()); //hashinf pq // read more

            String sql = "INSERT INTO pongapplication (GamerTag, Fname, Lname, Email,password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql); 
                pstmt.setString(1, GamerTag);
                pstmt.setString(2, Fname);
                pstmt.setString(3, Lname);
                pstmt.setString(4, Email);
                pstmt.setString(5, hashedpassword); 
                pstmt.executeUpdate();
             return "Account created successfully";
             
        } catch (SQLException ex) {
            Logger.getLogger(RegisterInterfaceImp.class.getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }
    
    }
    
     

//    @Override
//    public String update(int id, String Fname, String Lname, String Email) throws RemoteException {
//     try {
//            Connection con = DriverManager.getConnection("jdbc:mysql//local;host:3306","appuser", "pongapp");
//            Statement St = (Statement)con.createStatement();
//            String sql = "UPDATE pongapplication SET Fname = ?, Lname = ?, Email = ? WHERE id = ?";
//            PreparedStatement pstmt = con.prepareStatement(sql); 
//                pstmt.setInt(1, id);
//                pstmt.setString(2, Fname);
//                pstmt.setString(3, Lname);
//                pstmt.setString(4, Email);
//             // Execute the update
//            int rowsAffected = pstmt.executeUpdate();
//
//            if (rowsAffected > 0) {
//                return "Account updated successfully";
//            } else {
//                return "No account found with the given ID";
//            }
//
//            } catch (SQLException ex) {
//                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
//                return ex.toString();
//            }
//
//    }

//    @Override
//    public ArrayList search(int id) throws RemoteException {
//            ArrayList<String[]> results = new ArrayList<>();
//
//    try {
//         Connection con = DriverManager.getConnection("jdbc:mysql://local;host:3306","appuser", "");
//         Statement St = (Statement)con.createStatement();
//         String sql = "SELECT * FROM pongapplication WHERE id = ?";
//         PreparedStatement pstmt = con.prepareStatement(sql); 
//         pstmt.setInt(1, id);
//                  
//            // Execute the query
//        try (ResultSet rs = pstmt.executeQuery()) {
//            // Process the results
//            while (rs.next()) {
//                String[] record = new String[5]; // Adjust size based on number of columns you are retrieving
//                record[0] = rs.getString("id"); // id
//                record[1] = rs.getString("GamerTag"); // GamerTag
//                record[2] = rs.getString("Fname"); // First Name
//                record[3] = rs.getString("Lname"); // Last Name
//                record[4] = rs.getString("Email"); // Email
//                results.add(record);
//            }
//        }
//
//               } catch (SQLException ex) {
//                   Logger.getLogger(RegisterInterfaceImp.class.getName()).log(Level.SEVERE, null, ex);
//                    throw new RemoteException("Error: " + ex.getMessage(), ex); 
//               }
//        return results;
//
//       }

    @Override
    public boolean Login(String GamerTag, String password) {
        String sql = "SELECT password FROM pongapplication WHERE gamerTag = ?";
        String gamerTag;
        try {
         Connection con = DriverManager.getConnection(url, "appuser", "pongapp");
            Statement St = (Statement)con.createStatement();
         PreparedStatement pstmt = con.prepareStatement(sql);
         pstmt.setString(1, GamerTag);
                
            try (ResultSet resultSet = pstmt.executeQuery()) {
                    if (resultSet.next()) {
                        String hashedPassword = resultSet.getString("password");
                        boolean valid =  BCrypt.checkpw(password, hashedPassword);
                        
                        if(valid == true){
                        gamerTag = GamerTag;
                        }
                        return valid;
                    }
                }
        }catch(SQLException ex) {
                         Logger.getLogger(RegisterInterfaceImp.class.getName()).log(Level.SEVERE, null, ex);
                     }
      return false;
    }
    }