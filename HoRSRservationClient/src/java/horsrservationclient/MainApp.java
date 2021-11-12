/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsrservationclient;

import ejb.session.stateless.FrontOfficeModuleSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.VisitorSessionBeanRemote;
import entity.Guest;
import entity.Reservation;
import entity.ReservationRoom;
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
import util.exception.ReservationRoomExistException;
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
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    
    public MainApp() {
    }

    public MainApp(VisitorSessionBeanRemote visitorSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote,
            FrontOfficeModuleSessionBeanRemote frontOfficeModuleSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote) {
        this.visitorSessionBeanRemote = visitorSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.frontOfficeModuleSessionBeanRemote = frontOfficeModuleSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
    }

    
    
    
    public void runApp() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to Hotel Reservation System (v1.0) ***\n");
            response = 0;
            System.out.println("1: Login");
            System.out.println("2. Register as Guest");
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
                    doRegisterAsGuest();
                } else if(response == 3){
                    doSearchRoom();
                } else if (response == 4) {
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
    
    private void doRegisterAsGuest()
    {
        Scanner scanner = new Scanner(System.in);
        
        
        System.out.println("*** HoRS Reservation Client :: Register ***\n");
        System.out.print("Enter first name> ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter last name> ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Enter email> ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter address> ");
        String address = scanner.nextLine().trim();
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        
        try
        {
            visitorSessionBeanRemote.registerAsGuest(new Guest(firstName, lastName, email, address, username, password));
            System.out.println("Registration successful!\n");
        } catch(GuestExistException ex)
        {
            System.out.println("Guest already exists!\n");
        } catch (UnknownPersistenceException ex)
        {
            System.out.println("Error!" + ex.getMessage() + "\n");
        }
        
        
    }
    

    
            
    private void menuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** HoRS Reservation Client***\n");
            System.out.println("You are login as " + currentGuest.getFirstName() + " " + currentGuest.getLastName() + ".\n");
            System.out.println("1: Search Hotel Room");
            System.out.println("2: Reserve Hotel Room");
            System.out.println("3: View My Reservation Details");
            System.out.println("4: View All My Reservation Details");
            System.out.println("5: Logout\n");
            response = 0;
            
            while(response < 1 || response > 5)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1)
                {
                    doSearchRoom();
                }
                else if(response == 2)
                {
                    doReserveRoom();
                }
                else if(response == 3)
                {
                    doViewReservationDetails();
                }
                else if (response == 4)
                {
                    doViewAllReservations();
                }
                else if (response == 5)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                        }
            }
            
            if(response == 5)
            {
                break;
            }
        }
    }
    
    public List<Object[]> doSearchRoom() {
        Scanner sc = new Scanner(System.in);
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
            System.out.printf("%15s%30s%30s%30s\n", count,((RoomType)obj[0]).getName(), (Long)obj[1], (roomRateSessionBeanRemote.getFee(((RoomType)obj[0]).getRoomTypeId(),dateStart, dateEnd)));
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
            
            RoomType roomType = (RoomType)list.get(option-1)[0];
            BigDecimal fee = roomRateSessionBeanRemote.getFee(roomType.getRoomTypeId(), dateStart, dateEnd);
            BigDecimal totalFee = fee.multiply(BigDecimal.valueOf(number));
            
            newReservation.setFee(totalFee);  
            try {
                Long reservationId = frontOfficeModuleSessionBeanRemote.createReservation(newReservation);
                visitorSessionBeanRemote.addReservation(currentGuest.getVisitorId(), reservationId);
                System.out.println("Successful! Total Price: " + totalFee );
                for (int i = 0; i < number; i++)
                {
                    ReservationRoom reservationRoom = new ReservationRoom();
                    reservationRoom.setRoomType((RoomType)list.get(option - 1)[0]);
                    frontOfficeModuleSessionBeanRemote.reserveRoom(reservationRoom , reservationId);
                    
                }
            } catch(ReservationExistException | ReservationRoomExistException | GuestNotFoundException | ReservationNotFoundException ex) {
                System.out.println("Reservation already exists!");
            } catch (UnknownPersistenceException ex) {
                System.out.println("Unknown error!");
            }
        }
    }
    
    public List<Object[]> doSearchRoom(Date dateStart, Date dateEnd) {
        System.out.printf("%15s%30s%30s%30s\n", "Option", "Room Type", "Available Rooms", "Reservation Amount");
        List<Object[]> list = frontOfficeModuleSessionBeanRemote.searchRooms(/*format.format(cal.getTime())*/ dateStart, dateEnd);
        int count = 1;
        for(Object[] obj : list){
            BigDecimal fee = roomRateSessionBeanRemote.getFee(((RoomType)obj[0]).getRoomTypeId(),dateStart, dateEnd);
            System.out.printf("%15s%30s%30s%30s\n", count,((RoomType)obj[0]).getName(), (Long)obj[1], (roomRateSessionBeanRemote.getFee(((RoomType)obj[0]).getRoomTypeId(),dateStart, dateEnd)));
            count++;
        }
        return list;
    }
    
    private void doViewReservationDetails(){
        Scanner sc = new Scanner(System.in);
        System.out.println("*** HoRS Reservation Client :: View Reservation Details ***\n");
        System.out.println("Reservation ID > ");
        Long reservationId = sc.nextLong();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        try
        {
        
            Reservation reservation = reservationSessionBeanRemote.viewReservationDetails(reservationId);
            System.out.printf("%8s%15s%20s%20s\n", "ReservationID", "StartDate", "EndDate", "fee");
            System.out.printf("%8s%20s%20s%20s\n", reservation.getReservationId(), format.format(reservation.getStartDate()), format.format(reservation.getEndDate()), reservation.getFee());
        } catch (ReservationNotFoundException ex)
        {
            System.out.println("No Reservation Found!");
        }
    }
    
    private void doViewAllReservations() 
    {
        System.out.println("*** HoRS Reservation Client :: View All My Reservations ***\n");
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        try
        {
            List<Reservation> reservations = reservationSessionBeanRemote.viewAllReservations(currentGuest.getVisitorId());
            System.out.printf("%8s%15s%20s%20s\n", "ReservationID", "StartDate", "EndDate", "fee");
            for (Reservation reservation : reservations)
            {
                System.out.printf("%8s%20s%20s%20s\n", reservation.getReservationId(), format.format(reservation.getStartDate()), format.format(reservation.getEndDate()), reservation.getFee());
            }
        }
        catch(GuestNotFoundException | ReservationNotFoundException ex)
        {
            System.out.println("Error! " + ex.getMessage());
        }
    }
      
}
