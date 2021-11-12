/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HoRSManagementClient;

import ejb.session.stateless.AdminSessionBeanRemote;
import ejb.session.stateless.FrontOfficeModuleSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.VisitorSessionBeanRemote;
import entity.Employee;
import entity.Exceptions;
import entity.Reservation;
import entity.ReservationRoom;
import entity.RoomType;
import entity.Visitor;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javafx.util.Duration;
import util.enumeration.AccessRightEnum;
import util.enumeration.ExceptionTypeEnum;
import util.exception.ExceptionExistException;
import util.exception.InvalidAccessRightException;
import util.exception.ReservationExistException;
import util.exception.ReservationRoomExistException;
import util.exception.RoomNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.VisitorNotFoundException;

/**
 *
 * @author wongj
 */
public class FrontOfficeModule {
    private FrontOfficeModuleSessionBeanRemote frontOfficeModuleSessionBeanRemote;
    private Employee currentEmployee;
    private VisitorSessionBeanRemote visitorSessionBeanRemote;
    private AdminSessionBeanRemote adminSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    
    public FrontOfficeModule() {
    }

    public FrontOfficeModule(FrontOfficeModuleSessionBeanRemote frontOfficeModuleSessionBeanRemote, Employee currentEmployee, VisitorSessionBeanRemote visitorSessionBeanRemote, AdminSessionBeanRemote adminSessionBeanRemote, RoomSessionBeanRemote remoteSessionBeanRemote) {
        this.frontOfficeModuleSessionBeanRemote = frontOfficeModuleSessionBeanRemote;
        this.currentEmployee = currentEmployee;
        this.visitorSessionBeanRemote = visitorSessionBeanRemote;
        this.adminSessionBeanRemote = adminSessionBeanRemote;
        this.roomSessionBeanRemote = remoteSessionBeanRemote;
    }
    
    public void menuFrontOffice() throws InvalidAccessRightException {
        if(currentEmployee.getAccessRightEnum() != AccessRightEnum.GUEST_RELATION_OFFICER) {
            throw new InvalidAccessRightException("You do not have the GUEST_RELATION_OFFICER rights to access the front office module");
        }
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Front Office ***\n");
            System.out.println("1: Walk-in Search Room");
            System.out.println("2: Walk-in Reserve Room");
            System.out.println("3: Check-in Guest");
            System.out.println("4: Check-out Guest");
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
                    doCheckInGuest();
                } else if (response == 4) {
                    doCheckOutGuest();
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
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Front Office :: Search Room ***\n");
            Date dateStart = new Date();
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
            System.out.printf("%15s%30s%30s%30s\n", count,((RoomType)obj[0]).getName(), (Long)obj[1], ((RoomType)obj[0]).getRoomRates().get(0).getRatePerNight().multiply(new BigDecimal(((dateEnd.getTime() - dateStart.getTime()) / (24*60*60*1000)))));
            count++;
        }
        System.out.println("end here");
        return list;
    }
    
    public void doReserveRoom() {
        String input = "";
        Scanner sc = new Scanner(System.in);
        //Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Front Office :: Reserve Room ***\n");
        System.out.println("Visitor Details \n");
        Visitor visitor = new Visitor();
        System.out.println("Enter Visitor Address > ");
        input = sc.nextLine().trim();
        visitor.setAddress(input);
        System.out.println("Enter Visitor Email > ");
        input = sc.nextLine().trim();
        visitor.setEmail(input);
        System.out.println("Enter Visitor First Name > ");
        input = sc.nextLine().trim();
        visitor.setFirstName(input);
        System.out.println("Enter Visitor Last Name > ");
        input = sc.nextLine().trim();
        visitor.setLastName(input);
        Long visitorId = new Long(-1);
        try {
            visitorId = visitorSessionBeanRemote.createVisitor(visitor);
        } catch (UnknownPersistenceException ex) {
            System.out.println("Unknown Error!");
        }
        //System.out.println("Enter Start Date (dd/MM/yyyy)> ");
        //String start = sc.nextLine().trim();
        Date dateStart = new Date();
        
        /*if(null != start && start.trim().length() > 0){
            try {
                dateStart = format.parse(start);
            } catch (ParseException ex) {
                System.out.println("Problems parsing the given date!");
            }
        }*/
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
        if(!visitorId.equals(-1)){
        if ((Long)list.get(option - 1)[1] < number) {
            System.out.println("Unable to make a reservation as the required room is over the number of available rooms!");
        } else {
            Reservation newReservation = new Reservation();
            newReservation.setStartDate(dateStart);
            newReservation.setEndDate(dateEnd);
            newReservation.setFee(((RoomType)list.get(option-1)[0]).getRoomRates().get(0).getRatePerNight().multiply(new BigDecimal(((dateEnd.getTime() - dateStart.getTime()) / (24*60*60*1000)))));
            try {
                Long reservationId = frontOfficeModuleSessionBeanRemote.createReservation(newReservation, visitorId);
                System.out.println("Can Reserve room!");
                for (int i = 0; i < number; i++)
                {
                    //System.out.println(i + " number");
                    ReservationRoom reservationRoom = new ReservationRoom();
                    reservationRoom.setRoomType((RoomType)list.get(option - 1)[0]);
                    //System.out.println(i + " number2");
                    Long reservationRoomId = frontOfficeModuleSessionBeanRemote.reserveRoom(reservationRoom , reservationId);
                    //System.out.println(reservationRoomId + "id");
                    List<Object> result = adminSessionBeanRemote.searchRooms(dateStart, dateEnd,((RoomType)list.get(option - 1)[0]).getRoomTypeId() );
                    //System.out.println(result);
                    if(result.size() == 0) {
                        result = adminSessionBeanRemote.searchRooms(dateStart, dateEnd,((RoomType)list.get(option - 1)[0]).getNextHigherRoomType());
                          if(result.size() == 0){
                              System.out.println("Both the current Room Type and Next Highest Room Type do not have available rooms for reservation!");
                          } else {
                              //adminSessionBeanRemote.setRoom(reservationRoom.getReservationRoomId(), roomSessionBeanRemote.viewRoomDetails((Long)result.get(0)).getRoomId());
                              System.out.println("There are no more available rooms for the current Room Type but there are available rooms for the Next Highest Room Type!");
                          }
                        //
                    } else {
                        //System.out.println((Long)result.get(0) + " here");
                        //System.out.println(reservationRoom.getReservationRoomId());
                        adminSessionBeanRemote.setRoom(reservationRoomId, (Long)result.get(0));
                        //System.out.println((Long)result.get(0) + " there");
                        System.out.println("This will be the allocated room for this reservation: " + (Long)result.get(0) + " for Room Type " + ((RoomType)list.get(option - 1)[0]).getRoomTypeId());
                        //
                    }
                    
                }
                    
               
                
            } catch(ReservationExistException | ReservationRoomExistException ex) {
                System.out.println("Reservation already exists!");
            } catch (UnknownPersistenceException ex) {
                System.out.println("Unknown error!");
            }
        }
        } else {
            System.out.println("Unknown error!");
        }
    }
    
    public List<Object[]> doSearchRoom(Date dateStart, Date dateEnd) {
        System.out.printf("%15s%30s%30s%30s\n", "Option", "Room Type", "Available Rooms", "Reservation Amount");
        List<Object[]> list = frontOfficeModuleSessionBeanRemote.searchRooms(dateStart, dateEnd);
        int count = 1;
        for(Object[] obj : list){
            System.out.printf("%15s%30s%30s%30s\n", count,((RoomType)obj[0]).getName(), (Long)obj[1], (((RoomType)obj[0]).getRoomRates().get(0).getRatePerNight()).multiply(new BigDecimal((dateEnd.getTime() - dateStart.getTime()) / (24*60*60*1000))));
            count++;
        }
        return list;
    }
    
    public void doCheckInGuest() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Front Office :: Check In Guest ***\n");
        System.out.println("Enter Visitor Email > ");
        String input = sc.nextLine().trim();
        try {
            Visitor currentVisitor = visitorSessionBeanRemote.retrieveVisitorByEmail(input);
            System.out.printf("%15s%30s%30s%30s\n", "Reservation ID", "Reservation Room ID", "RoomType", "Room");
            List<ReservationRoom> list = frontOfficeModuleSessionBeanRemote.allocatedRooms(currentVisitor.getVisitorId());
            for(ReservationRoom reservationRoom : list){
                System.out.printf("%15s%30s%30s%30s\n", reservationRoom.getReservation().getReservationId(), reservationRoom.getReservationRoomId(), reservationRoom.getRoom().getRoomType().getRoomTypeId(), reservationRoom.getRoom().getRoomNo());
            }
        } catch(VisitorNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    public void doCheckOutGuest() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Front Office :: Check Out Guest ***\n");
        System.out.println("Enter Visitor Email > ");
        String input = sc.nextLine().trim();
        //System.out.println(input);
        try {
            
            Visitor currentVisitor = visitorSessionBeanRemote.retrieveVisitorByEmail(input);
            //System.out.println(currentVisitor.getVisitorId());
            frontOfficeModuleSessionBeanRemote.removeGuest(currentVisitor.getVisitorId());
        } catch(VisitorNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
