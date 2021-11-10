/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsrservationclient;

import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.VisitorSessionBeanRemote;
import entity.Guest;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import util.exception.GuestExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author harmo
 */
public class MainApp {
    private VisitorSessionBeanRemote visitorSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private Guest currentGuest;
    
    public MainApp() {
    }

    public MainApp(VisitorSessionBeanRemote visitorSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote) {
        this.visitorSessionBeanRemote = visitorSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
    }
    
    
    public void runApp() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to Hotel Reservation System (v1.0) ***\n");
            response = 0;
            System.out.println("1: Login");
            System.out.println("2. Register as Guest");
            if(currentGuest != null) {
                System.out.println("2: Logout");
            }
            System.out.println("3: Exit");
            while (response < 1 || response > 3) {
                System.out.print("> ");
                response = sc.nextInt();
            
                if(response == 1){
                    try{
                        doLogin();
                        System.out.println("Login successful!\n");
                        System.out.println("Welcome, " + currentGuest.getFirstName() + "!\n");
                        menuMain();
                    } catch(InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 2){
                    try{
                        doRegisterAsGuest();
                        System.out.println("Registration successful!\n");
                    }
                    catch (GuestExistException | UnknownPersistenceException | InputDataValidationException ex) {
                        System.out.println("Registration Error!: " + ex.getMessage());
                    }
                    
                } else if(response == 3){
                    break;
                }
                else {
                    System.out.println("Invalid Option, please try again!");
                }
            }
            if(response == 3){
                break;
            }
        }
        
    }
    
    private void doLogin() throws InvalidLoginCredentialException
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** HoRS Reservation Client :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentGuest = visitorSessionBeanRemote.guestLogin(username, password);      
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    private void doRegisterAsGuest() throws GuestExistException, UnknownPersistenceException, InputDataValidationException
    {
        Scanner scanner = new Scanner(System.in);
        String firstName = "";
        String lastName = "";
        String email = "";
        String address = "";
        String username = "";
        String password = "";
        
        System.out.println("*** HoRS Reservation Client :: Register ***\n");
        System.out.print("Enter first name> ");
        firstName = scanner.nextLine().trim();
        System.out.print("Enter last name> ");
        lastName = scanner.nextLine().trim();
        System.out.print("Enter email> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter address> ");
        address = scanner.nextLine().trim();
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0 && firstName.length() > 0 && lastName.length() > 0
                && email.length() > 0 && address.length() > 0)
        {
            visitorSessionBeanRemote.registerAsGuest(new Guest(firstName, lastName, email, address, username, password));
        }
        else 
        {
            throw new InputDataValidationException("Missing Data!");
        }
        
        
    }
    

    
            
    private void menuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Point-of-Sale (POS) System (v4.3) ***\n");
            System.out.println("You are login as " + currentGuest.getFirstName() + " " + currentGuest.getLastName() + ".\n");
            System.out.println("1: Reserve Hotel Room");
            System.out.println("2: View My Reservation Details");
            System.out.println("3: View All My Reservation Details");
            System.out.println("4: Logout");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    
                }
                else if(response == 2)
                {
                    break;
                }
                else if (response == 3)
                {
                    break;
                }
                else if (response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }
}
