/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HoRSManagementClient;

import ejb.session.stateless.FrontOfficeModuleSessionBeanRemote;
import entity.Employee;
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
import util.enumeration.AccessRightEnum;
import util.exception.InvalidAccessRightException;
import util.exception.ReservationExistException;
import util.exception.ReservationRoomExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wongj
 */
public class FrontOfficeModule {
    private FrontOfficeModuleSessionBeanRemote frontOfficeModuleSessionBeanRemote;
    private Employee currentEmployee;
    
    public FrontOfficeModule() {
    }

    public FrontOfficeModule(FrontOfficeModuleSessionBeanRemote frontOfficeModuleSessionBeanRemote, Employee currentEmployee) {
        this.frontOfficeModuleSessionBeanRemote = frontOfficeModuleSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void menuFrontOffice() throws InvalidAccessRightException {
        if(currentEmployee.getAccessRightEnum() != AccessRightEnum.GUEST_RELATION_OFFICER) {
            throw new InvalidAccessRightException("You do not have the GUEST_RELATION_OFFICER rights to access the front office module");
        }
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Front Office ***\n");
            System.out.println("1: Search Room");
            System.out.println("2: Reserve Room");
            System.out.println("3: Create New Partner");
            System.out.println("4: View All Partners");
            System.out.println("5: Exit to previous menu");
            response = 0;
            
            while(response < 1 || response > 7) {
                System.out.println("> ");
                
                response = sc.nextInt();
                if(response == 1) {
                   doSearchRoom();
                } else if (response == 2) {
                   doReserveRoom();
                } else if (response == 3){
              
                } else if (response == 4) {
               
                }
                else if(response == 5) {
                    break;
                }
            }
            if(response == 5) {
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
                for (int i = 0; i < number; i++)
                {
                    ReservationRoom reservationRoom = new ReservationRoom();
                    reservationRoom.setRoomType((RoomType)list.get(option - 1)[0]);
                    frontOfficeModuleSessionBeanRemote.reserveRoom(reservationRoom , newReservation.getReservationId());
                    
                }
            } catch(ReservationExistException | UnknownPersistenceException | ReservationRoomExistException ex) {
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
}
