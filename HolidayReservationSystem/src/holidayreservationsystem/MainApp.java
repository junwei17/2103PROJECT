/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import static java.util.Collections.list;
import java.util.List;
import java.util.Scanner;
import ws.client.AnyTypeArray;
import ws.client.HolidayReservationWebService_Service;
import ws.client.InvalidLoginCredentialException;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.ParseException_Exception;
import ws.client.Partner;
import ws.client.PartnerNotFoundException_Exception;
import ws.client.Reservation;
import ws.client.ReservationNotFoundException_Exception;
import ws.client.RoomType;

/**
 *
 * @author harmo
 */
public class MainApp {
    
    private HolidayReservationWebService_Service service;
    private Partner currentPartner;
    
    MainApp(){}
    
    MainApp(HolidayReservationWebService_Service service)
    {
        this.service = service;
    }
    
    public void runApp() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to Holiday Reservation System (v1.0) ***\n");
            response = 0;
            System.out.println("1: Login");
            System.out.println("2. Search Rooms");
            System.out.println("3: Exit");
            while (response < 1 || response > 3) {
                System.out.print("> ");
                response = sc.nextInt();
            
                if(response == 1){
                    try{
                        doLogin();
                        System.out.println("Welcome, " + currentPartner.getUsername()+ "!\n");
                        menuMain();
                    } catch(InvalidLoginCredentialException_Exception ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 2){
                    doSearchRoom();
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
    
    private void doLogin() throws InvalidLoginCredentialException_Exception
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** Holisday Reservation System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        

        currentPartner = service.getHolidayReservationWebServicePort().partnerLogin(username, password);
 
    }
    private void menuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** HoRS Reservation Client***\n");
            System.out.println("You are login as " + currentPartner.getUsername() + ".\n");
            System.out.println("1: Reserve Hotel Room");
            System.out.println("2: View My Reservation Details");
            System.out.println("3: View All My Reservation Details");
            System.out.println("4: Logout\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    //doReserveRoom();
                }
                else if(response == 2)
                {
                    doViewReservationDetails();
                }
                else if (response == 3)
                {
                   doViewAllReservations();
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
    
    private void doViewReservationDetails(){
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Holiday Reservation System :: View Reservation Details ***\n");
        System.out.println("Reservation ID > ");
        Long reservationId = sc.nextLong();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        try
        {
        
            Reservation reservation = service.getHolidayReservationWebServicePort().viewReservationDetails(reservationId);
            System.out.printf("%8s%15s%20s%20s\n", "ReservationID", "StartDate", "EndDate", "fee");
            System.out.printf("%8s%20s%20s%20s\n", reservation.getReservationId(), format.format(reservation.getStartDate()), format.format(reservation.getEndDate()), reservation.getFee());
        } catch (ReservationNotFoundException_Exception ex)
        {
            System.out.println("No Reservation Found!");
        }
    }
    
    private void doViewAllReservations() 
    {
        System.out.println("*** HoRS Reservation Client :: View All My Reservations ***\n");
        try
        {
            List<Reservation> reservations = service.getHolidayReservationWebServicePort().viewAllReservationsPartner(currentPartner.getPartnerId());
            System.out.printf("%8s%15s%20s%20s%20s%20s\n", "ReservationID", "StartDate", "EndDate", "fee");
            for (Reservation reservation : reservations)
            {
                System.out.printf("%8s%20s%20s%20s\n", reservation.getReservationId(), reservation.getStartDate(), reservation.getEndDate(), reservation.getFee());
            }
        }
        catch(PartnerNotFoundException_Exception | ReservationNotFoundException_Exception ex)
        {
            System.out.println("Error! " + ex.getMessage());
        }
    }
    
    private void doSearchRoom()
    {
        System.out.println("*** HoRS Reservation Client :: View All My Reservations ***\n");
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Enter Start Date (dd/MM/yyyy) >");
        String startDate = sc.nextLine().trim();
        System.out.println("Enter End Date (dd/MM/yyyy) >");
        String endDate = sc.nextLine().trim();
        
        try
        {
            List<AnyTypeArray> list = service.getHolidayReservationWebServicePort().searchRooms(startDate, endDate);
            System.out.printf("%15s%30s%30s%30s\n", "Option", "Room Type", "Available Rooms", "Reservation Amount");
            int count = 1;
            for(AnyTypeArray item : list){
            List<Object> obj = item.getItem();
            System.out.printf("%15s%30s%30s%30s\n", count,((RoomType)obj.get(0)).getName(), (Long)obj.get(1), ((RoomType)obj.get(0)).getRoomRates().get(0).getRatePerNight());
            count++;
        }
        }
        catch (ParseException_Exception ex)
        {
            System.out.println("Error parsing dates!");
        }
        
      
    }
    
}
