/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsrservationclient;

import ejb.session.stateless.FrontOfficeModuleSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.VisitorSessionBeanRemote;
import entity.Guest;
import entity.Reservation;
import entity.RoomType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.GuestExistException;
import util.exception.GuestNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationExistException;
import util.exception.ReservationNotFoundException;
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
    private FrontOfficeModuleSessionBeanRemote frontOfficeModuleSessionBeanRemote;
    
    public MainApp() {
    }

    public MainApp(VisitorSessionBeanRemote visitorSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, FrontOfficeModuleSessionBeanRemote frontOfficeModuleSessionBeanRemote) {
        this.visitorSessionBeanRemote = visitorSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.frontOfficeModuleSessionBeanRemote = frontOfficeModuleSessionBeanRemote;
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
            System.out.println("3: Search Rooms");
            System.out.println("4: Exit");
            while (response < 1 || response > 4) {
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
                    doSearchRoom();
                } else if(response == 4) {
                    break;
                }
                else {
                    System.out.println("Invalid Option, please try again!");
                }
            }
            if(response == 4){
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
                    doReserveRoom();
                }
                else if(response == 2)
                {
                    System.out.println("Reservation ID> ");
                    Long reservationId = scanner.nextLong();
                    
                    try
                    {
                        String reservationDetails = doViewReservationDetails(reservationId);
                        System.out.println(reservationDetails);
                    }
                    catch (ReservationNotFoundException ex)
                    {
                        System.out.println("Error!" + ex.getMessage());
                    }
                }
                else if (response == 3)
                {
                    try
                    {
                        List<Reservation> reservations = doViewAllReservations();
                        for (Reservation reservation : reservations)
                        {
                            String reservationDetails = "Reservation Start Date: " + reservation.getStartDate() + ".\n";
                            reservationDetails += "Reservation End Date: " + reservation.getEndDate() + ".\n";
                            reservationDetails += "Number of Rooms: " + reservation.getNumberOfRooms() + ".\n";
                            reservationDetails += "Price: " + reservation.getFee() + ".\n";
                            System.out.println(reservationDetails);
                        }
                    }
                    catch (ReservationNotFoundException ex)
                    {
                        System.out.println("Error! " + ex.getMessage());
                    }
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
    
    public List<Object[]> doSearchRoom() {
        Scanner sc = new Scanner(System.in);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Start here");
        System.out.println("Enter Start Date (dd/MM/yyyy)> ");
            String start = sc.nextLine().trim();
                Date dateStart = null;
                if(null != start && start.trim().length() > 0){
                  try {
                      dateStart = format.parse(start);
                  } catch (ParseException ex) {
                      System.out.println("Problems parsing the given date!");
                  }
                }
            System.out.println("Enter End Date (dd/MM/yyyy)> ");
            String end = sc.nextLine().trim();
            Date dateEnd = null;
            if(null != end && end.trim().length() > 0){
              try {
                  dateEnd = format.parse(end);
              } catch (ParseException ex) {
                  System.out.println("Problems parsing the given date!");
              }
            }
            
        System.out.printf("%15s%30s%30s%30s\n", "Option", "Room Type", "Available Rooms", "Reservation Amount");
        List<Object[]> list = frontOfficeModuleSessionBeanRemote.searchRooms(/*format.format(cal.getTime())*/ dateStart, dateEnd);
        int count = 1;
        for(Object[] obj : list){
            System.out.printf("%15s%30s%30s%30s\n", count,((RoomType)obj[0]).getName(), (Long)obj[1], ((RoomType)obj[0]).getRoomRates().get(0).getRatePerNight());
            count++;
        }
        System.out.println("end here");
        return list;
    }
    
    public void doReserveRoom() {
        Scanner sc = new Scanner(System.in);
        //Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Enter Start Date (dd/MM/yyyy)> ");
        String start = sc.nextLine().trim();
        Date dateStart = null;
        if(null != start && start.trim().length() > 0){
            try {
                dateStart = format.parse(start);
            } catch (ParseException ex) {
                System.out.println("Problems parsing the given date!");
            }
        }
        System.out.println("Enter End Date (dd/MM/yyyy)> ");
        String end = sc.nextLine().trim();
        Date dateEnd = null;
        if(null != end && end.trim().length() > 0){
            try {
                dateEnd = format.parse(end);
            } catch (ParseException ex) {
                System.out.println("Problems parsing the given date!");
            }
        }
        List<Object[]> list = doSearchRoom(dateStart,dateEnd);
        System.out.print("Enter an Option selection (as seen in the left column) > ");
        int option = sc.nextInt();
        System.out.println("Enter number of rooms required > ");
        int number = sc.nextInt();
        if ((Long)list.get(option - 1)[1] < number) {
            System.out.println("Unable to make a reservation as the required room is over the number of available rooms!");
        } else {
            Reservation newReservation = new Reservation();
            newReservation.setStartDate(dateStart);
            newReservation.setEndDate(dateEnd);
            newReservation.setFee(((RoomType)list.get(option-1)[0]).getRoomRates().get(0).getRatePerNight().multiply(BigDecimal.valueOf((dateEnd.getTime() - dateStart.getTime()) / 1000 / 60 / 60 / 24)));
            try {
                frontOfficeModuleSessionBeanRemote.createReservation(newReservation);
            } catch(ReservationExistException | UnknownPersistenceException ex) {
                System.out.println("Reservation already exists!");
            }
        }
        
        
    }
    
    public List<Object[]> doSearchRoom(Date dateStart, Date dateEnd) {
        System.out.printf("%15s%30s%30s%30s\n", "Option", "Room Type", "Available Rooms", "Reservation Amount");
        List<Object[]> list = frontOfficeModuleSessionBeanRemote.searchRooms(/*format.format(cal.getTime())*/ dateStart, dateEnd);
        int count = 1;
        for(Object[] obj : list){
            System.out.printf("%15s%30s%30s%30s\n", count,((RoomType)obj[0]).getName(), (Long)obj[1], ((RoomType)obj[0]).getRoomRates().get(0).getRatePerNight());
            count++;
        }
        System.out.println("end here");
        return list;
    }
    
    private String doViewReservationDetails(Long reservationId) throws ReservationNotFoundException{
        return reservationSessionBeanRemote.viewReservationDetails(reservationId);
    }
    
    private List<Reservation> doViewAllReservations() throws ReservationNotFoundException
    {
        try
        {
            return reservationSessionBeanRemote.viewAllReservations(currentGuest.getGuestId());
        }
        catch(GuestNotFoundException | ReservationNotFoundException ex)
        {
            throw new ReservationNotFoundException(ex.getMessage());
        }
    }
        
}
