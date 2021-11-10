/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HoRSManagementClient;

import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javafx.util.Pair;
import javax.persistence.Query;
import util.enumeration.AccessRightEnum;
import util.enumeration.AmenitiesEnum;
import util.enumeration.RateTypeEnum;
import util.exception.DeleteRoomException;
import util.exception.DeleteRoomRateException;
import util.exception.DeleteRoomTypeException;
import util.exception.InvalidAccessRightException;
import util.exception.RoomExistException;
import util.exception.RoomNotFoundException;
import util.exception.RoomRateExistException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRoomException;
import util.exception.UpdateRoomRateException;
import util.exception.UpdateRoomTypeException;

/**
 *
 * @author wongj
 */
public class HotelOperationModule {

    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private Employee currentEmployee;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    
    public HotelOperationModule() {
    }

    public HotelOperationModule(RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, Employee currentEmployee, RoomRateSessionBeanRemote roomRateSessionBeanRemote) {
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void menuHotelOperation() throws InvalidAccessRightException {
        if(currentEmployee.getAccessRightEnum() != AccessRightEnum.OPERATION_MANAGER) {
            if(currentEmployee.getAccessRightEnum() == AccessRightEnum.SALES_MANAGER) {
                menuRoomRates();
            } else {
                throw new InvalidAccessRightException("You do not have the OPERATION_MANAGER or SALES_MANAGER rights to access the system administration module");
            }
        }
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation ***\n");
            System.out.println("1: Create New Room Type");
            System.out.println("2: View Room Type");
            System.out.println("3: Update Room Type");
            System.out.println("4: Delete Room Type Details");
            System.out.println("5: View All Room Type");
            System.out.println("6: Create New Room");
            System.out.println("7: Update Room");
            System.out.println("8: Delete Room");
            System.out.println("9: View All Rooms");
            System.out.println("10: View Room Allocation Exception Report");
            System.out.println("11: Exit to the previous menu");
            response = 0;
            
            while(response < 1 || response > 11) {
                System.out.println("> ");
                
                response = sc.nextInt();
                if(response == 1) {
                    doCreateNewRoomType();
                } else if (response == 2) {
                    doViewRoomTypeDetails();
                } else if (response == 3) {
                    System.out.println("Enter RoomType ID> ");
                    Long roomTypeId = sc.nextLong();
                    try {
                        RoomType currentRoomType = roomTypeSessionBeanRemote.viewRoomTypeDetails(roomTypeId);
                        doUpdateRoomType(currentRoomType);
                    } catch (RoomTypeNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (response == 4) {
                    System.out.println("Enter RoomType ID> ");
                    Long roomTypeId = sc.nextLong();
                    try {
                        RoomType currentRoomType = roomTypeSessionBeanRemote.viewRoomTypeDetails(roomTypeId);
                        doDeleteRoomType(currentRoomType);
                    } catch (RoomTypeNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (response == 5) {
                    viewAllRoomTypes();
                } else if (response == 6) {
                    doCreateNewRoom();
                } else if (response == 7) {
                    System.out.println("Enter Room ID> ");
                    Long roomId = sc.nextLong();
                    try {
                        Room currentRoom = roomSessionBeanRemote.viewRoomDetails(roomId);
                        doUpdateRoom(currentRoom);
                    } catch (RoomNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (response == 8) {
                    System.out.println("Enter Room ID> ");
                    Long roomId = sc.nextLong();
                    try {
                        Room currentRoom = roomSessionBeanRemote.viewRoomDetails(roomId);
                        doDeleteRoom(currentRoom);
                    } catch (RoomNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (response == 9) {
                    viewAllRooms();
                } else if (response == 10) {
                    viewReport();
                }else if(response == 11) {
                    break;
                }
            }
            if(response == 11) {
                break;
            }
        }
    }
    
    public void doCreateNewRoomType() {
        Scanner sc = new Scanner(System.in);
        RoomType newRoomType = new RoomType();
        
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation :: Create New Room Type ***\n");
        System.out.println("Enter New Room Type Name> ");
        newRoomType.setName(sc.nextLine().trim());
        System.out.println("Enter New Room Type Description> ");
        newRoomType.setDescription(sc.nextLine().trim());
        System.out.println("Enter New Room Type Bed> ");
        newRoomType.setBed(sc.nextLine().trim());
        System.out.println("Enter New Room Type Size(Integer)> ");
        newRoomType.setSize(sc.nextInt());
        System.out.println("Enter New Room Type Capacity> ");
        newRoomType.setCapacity(sc.nextInt());
        while(true) {
            System.out.println("Select New Room Type Amenities (1: Basic Amenities, 2: Basic Amenities with Bathtub, 3: Basic Amenities with Pool, 4: Basic Ammenities with Bathtub and Pool) > ");
            Integer amenitiesEnum = sc.nextInt();
            
            if(amenitiesEnum >= 1 && amenitiesEnum <=4) {
              newRoomType.setAmenitiesEnum(AmenitiesEnum.values()[amenitiesEnum - 1]);  
              break;
            } else {
                System.out.println("Invalid Option! Please try again!");
            }
        }
        try {
            Long newRoomTypeId = roomTypeSessionBeanRemote.createRoomType(newRoomType);
            System.out.println("New Room Type " + newRoomTypeId + " is created!");
        } catch(UnknownPersistenceException ex) {
            System.out.println("An unknown error has occured while creating the room type!: " + ex.getMessage() + "\n");
        } catch(RoomExistException ex) {
            System.out.println("An error has occured while creating the new Room Type!: The Room Type already exists!\n");
        }
    }
    
    public void doViewRoomTypeDetails() { 
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation :: View Room Type ***\n");
        System.out.println("Enter Room Type ID> ");
        Long roomTypeId = sc.nextLong();
        
        try {
            RoomType roomType = roomTypeSessionBeanRemote.viewRoomTypeDetails(roomTypeId);
            System.out.printf("%8s%15s%20s%20s%20s%20s\n", "Room Type ID", "Name", "Description", "Size", "Bed", "Capacity");
            System.out.printf("%8s%20s%20s%20s%20s%20s\n", roomType.getRoomTypeId(), roomType.getName(), roomType.getDescription(), roomType.getSize(), roomType.getBed(), roomType.getCapacity());
            System.out.println("---------------------------");
            System.out.println("1: Update Room Type");
            System.out.println("2: Delete Room Type");
            System.out.println("3: Exit");
            System.out.println("> ");
            response = sc.nextInt();
            
            if(response == 1) {
                doUpdateRoomType(roomType);
            } else if(response == 2) {
                doDeleteRoomType(roomType);
            } else if (response == 3) {
                System.out.println("Room Type not updated or deleted!");
            }
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("An error has occured while retrieving the Room Type: " + ex.getMessage() + "\n");
        }
    }
    
    public void doUpdateRoomType(RoomType roomType) {
        Scanner sc = new Scanner(System.in);
        String input;
        Integer numberInput;
        
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation :: Update Room Type ***\n");
        System.out.println("Enter Room Type Name(blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0) {
            roomType.setName(input);
        }
        
        System.out.println("Enter Room Type Description(blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0) {
            roomType.setDescription(input);
        }
        
        System.out.println("Enter Room Type Bed(blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0) {
            roomType.setBed(input);
        }
        
        System.out.println("Enter Room Type Size(Enter -1 if no change)> ");
        numberInput = sc.nextInt();
        if(numberInput != -1) {
            roomType.setSize(numberInput);
        }
        
        System.out.println("Enter Room Type Capacity(Enter -1 if no change)> ");
        numberInput = sc.nextInt();
        if(numberInput != -1) {
            roomType.setCapacity(numberInput);
        }
        
        while(true) {
            System.out.println("Select New Room Type Amenities (1: Basic Amenities, 2: Basic Amenities with Bathtub, 3: Basic Amenities with Pool, 4: Basic Ammenities with Bathtub and Pool) > ");
            Integer amenitiesEnum = sc.nextInt();
            
            if(amenitiesEnum >= 1 && amenitiesEnum <=4) {
              roomType.setAmenitiesEnum(AmenitiesEnum.values()[amenitiesEnum - 1]);  
              break;
            } else {
                System.out.println("Invalid Option! Please try again!");
            }
        }
        try {
            roomTypeSessionBeanRemote.updateRoomType(roomType);
        } catch(RoomTypeNotFoundException | UpdateRoomTypeException ex) {
            System.out.println("An unknown error has occured while updating the room type!: " + ex.getMessage() + "\n");
        }
        
    }
    
    public void doDeleteRoomType(RoomType roomType) {
        Scanner sc = new Scanner(System.in);
        String input;
        
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation :: Delete Room Type ***\n");
        System.out.printf("Confirm Delete Room Type %s (Room Type ID: %d) (Enter 'Y' to Delete)> ", roomType.getName(), roomType.getRoomTypeId());
        input = sc.nextLine().trim();
        
        if(input.equals("Y")) {
            try {
                roomTypeSessionBeanRemote.deleteRoomType(roomType.getRoomTypeId());
                System.out.println("Room Type is successfully deleted!");
            } catch (RoomTypeNotFoundException | DeleteRoomTypeException ex){
                System.out.println("An error has occured while deleting the Room Type: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Room Type is not deleted!\n");
        }
    }
    
    public void viewAllRoomTypes() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation :: View All Room Types ***\n");
        List<RoomType> roomTypes = roomTypeSessionBeanRemote.viewAllRoomTypes();
        System.out.printf("%8s%15s%20s%20s%20s%20s\n", "Room Type ID", "Name", "Description", "Size", "Bed", "Capacity");
        for(RoomType roomType : roomTypes) {
            System.out.printf("%8s%20s%20s%20s%20s%20s\n", roomType.getRoomTypeId(), roomType.getName(), roomType.getDescription(), roomType.getSize(), roomType.getBed(), roomType.getCapacity());
        }
        System.out.println("Press any key to continue...> ");
        sc.nextLine();
    }
    
    public void doCreateNewRoom() {
        Scanner sc = new Scanner(System.in);
        Room newRoom = new Room();
        
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation :: Create New Room ***\n");
        while(true) {
            System.out.println("Enter New Room Satuts (Enter y for available and n for not available)> ");
            String status = sc.nextLine().trim();
            if(status.equals("y")) {
                newRoom.setStatus(true);
                break;
            } else if (status.equals("n")) {
                newRoom.setStatus(false);
                break;
            }
        }
        System.out.println("Enter Room Type ID> ");
        try { 
            Long roomTypeId = sc.nextLong();
            RoomType roomType = roomTypeSessionBeanRemote.viewRoomTypeDetails(roomTypeId);
            newRoom.setRoomType(roomType);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("An error has occured while retrieving the Room Type: " + ex.getMessage() + "\n");
        }
        System.out.println("Enter New Room Number> ");
        newRoom.setRoomNo(sc.nextInt());
        System.out.println("the room now is " + newRoom.getRoomId() + " " + newRoom.getRoomNo() + " " + newRoom.getStatus());
        try {
            Long newRoomId = roomSessionBeanRemote.createRoom(newRoom);
            System.out.println("New room " + newRoomId + " is created!");
        } catch(UnknownPersistenceException ex) {
            System.out.println("An unknown error has occured while creating the room type!: " + ex.getMessage() + "\n");
        } catch(RoomExistException ex) {
            System.out.println("An error has occured while creating the new Room!: The Room already exists!\n" + ex.getMessage());
        }
    }
    
    /*public void doViewRoomDetails() { 
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation :: View Room Type ***\n");
        System.out.println("Enter Room Type ID> ");
        Long roomTypeId = sc.nextLong();
        
        try {
            RoomType roomType = roomTypeSessionBeanRemote.viewRoomTypeDetails(roomTypeId);
            System.out.printf("%8s%15s%20s%20s%20s%20s\n", "Room Type ID", "Name", "Description", "Size", "Bed", "Capacity");
            System.out.printf("%8s%20s%20s%20s%20s%20s\n", roomType.getRoomTypeId(), roomType.getName(), roomType.getDescription(), roomType.getSize(), roomType.getBed(), roomType.getCapacity());
            System.out.println("---------------------------");
            System.out.println("1: Update Room Type");
            System.out.println("2: Delete Room Type");
            System.out.println("3: Exit");
            System.out.println("> ");
            response = sc.nextInt();
            
            if(response == 1) {
                doUpdateRoomType(roomType);
            } else if(response == 2) {
                doDeleteRoomType(roomType);
            } else if (response == 3) {
                System.out.println("Room Type not updated or deleted!");
            }
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("An error has occured while retrieving the Room Type: " + ex.getMessage() + "\n");
        }
    }*/
    
    public void doUpdateRoom(Room room) {
        Scanner sc = new Scanner(System.in);
        String input;
        Integer numberInput;
        
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation :: Update Room ***\n");
        System.out.println("Enter Room Status(blank if no change, y if available, n for not available)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0) {
            if(input.equals("y")){
                room.setStatus(true);
            } else if (input.equals("n")){
                room.setStatus(false);
            }
        }
        
        System.out.println("Enter Room Type Id for the room (Enter -1 if no change)> ");
        Long roomTypeId = sc.nextLong();
        if(roomTypeId != -1) {
            try { 
                RoomType roomType = roomTypeSessionBeanRemote.viewRoomTypeDetails(roomTypeId);
                room.setRoomType(roomType);
            } catch (RoomTypeNotFoundException ex) {
                System.out.println("An error has occured while retrieving the Room Type: " + ex.getMessage() + "\n");
            }
        }
        
        System.out.println("Enter Room Number(Enter -1 if no change)> ");
        numberInput = sc.nextInt();
        if(numberInput != -1) {
            room.setRoomNo(numberInput);
        }
        try {
            roomSessionBeanRemote.updateRoom(room);
        } catch(RoomNotFoundException | UpdateRoomException ex) {
            System.out.println("An unknown error has occured while updating the room!: " + ex.getMessage() + "\n");
        }
        
    }
    
    public void doDeleteRoom(Room room) {
        Scanner sc = new Scanner(System.in);
        String input;
        
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation :: Delete Room  ***\n");
        System.out.printf("Confirm Delete Room %s (Room Type ID: %d) (Enter 'Y' to Delete)> ", room.getRoomNo(), room.getRoomId());
        input = sc.nextLine().trim();
        
        if(input.equals("Y")) {
            try {
                roomSessionBeanRemote.deleteRoom(room.getRoomId());
                System.out.println("Room is successfully deleted!");
            } catch (RoomNotFoundException | DeleteRoomException ex){
                System.out.println("An error has occured while deleting the Room: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Room is not deleted!\n");
        }
    }
    
    public void viewAllRooms() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation :: View All Rooms ***\n");
        List<Room> rooms = roomSessionBeanRemote.viewAllRooms();
        System.out.printf("%8s%15s%20s%20s\n", "Room ID", "Room Type", "Room Number", "Room Status");
        for(Room room : rooms) {
            System.out.printf("%8s%20s%20s%20s\n", room.getRoomId(), room.getRoomType().getRoomTypeId(), room.getRoomNo(), room.getStatus());
        }
        System.out.println("Press any key to continue...> ");
        sc.nextLine();
    }
    
    public void viewReport() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation :: View Room Allocation Exception Report ***\n");
        
    }
    
    public void menuRoomRates() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation ***\n");
            System.out.println("1: Create New Room Rate");
            System.out.println("2: View Room Rate Details");
            System.out.println("3: Update Room Rate");
            System.out.println("4: Delete Room Rate");
            System.out.println("5: View All Room Rates");
            System.out.println("6: Exit to the previous menu");
            response = 0;
            
            while(response < 1 || response > 6) {
                System.out.println("> ");
                
                response = sc.nextInt();
                if(response == 1) {
                    doCreateNewRoomRate();
                } else if (response == 2) {
                    doViewRoomRateDetails();
                } else if (response == 3) {
                    doUpdateRoomRate();
                } else if (response == 4) {
                    System.out.print("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation :: Delete Room Rate  ***\n");
                    System.out.print("Enter Room Rate ID that will be deleted >");
                    Long roomRateId = sc.nextLong();
                    try {
                        RoomRate currentRoomRate = roomRateSessionBeanRemote.viewRoomRateDetails(roomRateId);
                        doDeleteRoomRate(currentRoomRate);
                    } catch(RoomRateNotFoundException ex) {
                        System.out.println("No room rate found!");
                    }
                    
                } else if (response == 5) {
                    viewAllRoomRates();
                }else if(response == 6) {
                    break;
                }
            }
            if(response == 6) {
                break;
            }
        }
    }
    
    public void doCreateNewRoomRate() {
        Scanner sc = new Scanner(System.in);
        RoomRate newRoomRate = new RoomRate();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation :: Create New Room Rate ***\n");
        System.out.println("Enter New Room Rate Name> ");
        newRoomRate.setName(sc.nextLine().trim());
        System.out.println("Enter y if Room Rate is disabled, n if Room Rate is enabled> ");
        String input = sc.nextLine().trim();
        if(input.equals("y")) {
            newRoomRate.setDisabled(false);
        } else {
            newRoomRate.setDisabled(true);
        }
        System.out.println("Enter New Room Rate Validity Start Date (dd/MM/yyyy)> ");
        String start = sc.nextLine().trim();
        Date date = null;
        if(null != start && start.trim().length() > 0){
            try {
               date = format.parse(start);
            } catch (ParseException ex) {
                System.out.println("Problems parsing the given date!");
            }
        } 
        newRoomRate.setValidityStartDate(date);
        System.out.println("Enter New Room Rate Validity End Date (dd/MM/yyyy)> ");
        String end = sc.nextLine().trim();
        Date dateEnd = null;
        if(null != end && end.trim().length() > 0){
            try {
               dateEnd = format.parse(end);
            } catch (ParseException ex) {
                System.out.println("Problems parsing the given date!");
            }
        } 
        newRoomRate.setValidityEndDate(dateEnd);
        while(true) {
            System.out.println("Select New Room Rate Type (1: Published, 2: Normal, 3: Peak, 4: Promotion) > ");
            Integer roomRateTypeEnum = sc.nextInt();
            
            if(roomRateTypeEnum >= 1 && roomRateTypeEnum <=4) {
              newRoomRate.setRateType(RateTypeEnum.values()[roomRateTypeEnum - 1]);  
              break;
            } else {
                System.out.println("Invalid Option! Please try again!");
            }
        }
        System.out.println("Enter New Room Rate Per Night> ");
        newRoomRate.setRatePerNight(sc.nextBigDecimal());
        System.out.println("Enter New Room Rate Room Type> ");
        try {
            RoomType roomType = roomTypeSessionBeanRemote.viewRoomTypeDetails(sc.nextLong());
            newRoomRate.setRoomType(roomType);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Room Type Not Found!");
        }
        try {
            Long newRoomRateId = roomRateSessionBeanRemote.createRoomRate(newRoomRate);
            System.out.println("New Room Rate " + newRoomRateId + " is created!");
        } catch(UnknownPersistenceException ex) {
            System.out.println("An unknown error has occured while creating the room Rate!: " + ex.getMessage() + "\n");
        } catch(RoomRateExistException ex) {
            System.out.println("An error has occured while creating the new Room Type!: The Room Type already exists!\n");
        }
    }
    
    private void doViewRoomRateDetails() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation :: View Room Rate Details ***\n");
        System.out.println("Enter Room Rate ID> ");
        Long roomRateId = sc.nextLong();
        
        try {
            RoomRate roomRate = roomRateSessionBeanRemote.viewRoomRateDetails(roomRateId);
            System.out.printf("%8s%15s%20s%20s%50s%50s%20s\n", "Room Rate ID", "Name", "Rate Type", "Rate Per Night", "Validity Start Date", "Validity End Date", "Disabled");
            System.out.printf("%8s%15s%20s%20s%50s%50s%20s\n", roomRate.getRoomRateId(), roomRate.getName(), roomRate.getRateType(), roomRate.getRatePerNight(), roomRate.getValidityStartDate().toString(), roomRate.getValidityEndDate().toString(), roomRate.isDisabled());
            System.out.println("---------------------------");
            System.out.println("> ");
            response = sc.nextInt();
        } catch (RoomRateNotFoundException ex) {
            System.out.println("An error has occured while retrieving the Room Rate: " + ex.getMessage() + "\n");
        }
    }
    
    public void doUpdateRoomRate() {
        Scanner sc = new Scanner(System.in);
        String input = "";
        Integer numberInput;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation :: Update Room Rate ***\n");
        System.out.println("Enter Room Rate ID> ");
        Long roomRateId = sc.nextLong();
        sc.nextLine();
        try {
            RoomRate roomRate = roomRateSessionBeanRemote.viewRoomRateDetails(roomRateId);
            System.out.println("Enter Room Rate Name (Enter n if no change)> ");
            String roomRateName = sc.nextLine().trim();
            if(!roomRateName.equals("n")) {
                roomRate.setName(roomRateName);
            }
            System.out.println("Enter Room Rate Validity Start Date (Enter n if no change)> ");
            String start = sc.nextLine().trim();
            if(!start.equals("n")) {
                Date dateStart = null;
                if(null != start && start.trim().length() > 0){
                  try {
                      dateStart = format.parse(start);
                      roomRate.setValidityStartDate(dateStart);
                  } catch (ParseException ex) {
                      System.out.println("Problems parsing the given date!");
                  }
                }
            }
            System.out.println("Enter Room Rate Validity Start Date (Enter n if no change)> ");
            String end = sc.nextLine().trim();
            if(!end.equals("n")) {
            Date dateEnd = null;
            if(null != end && end.trim().length() > 0){
              try {
                  dateEnd = format.parse(end);
                  roomRate.setValidityEndDate(dateEnd);
              } catch (ParseException ex) {
                  System.out.println("Problems parsing the given date!");
              }
            }
            
            }
            System.out.println("Enter Room Rate Type (Enter n if no change)> ");
            String roomRateType = sc.nextLine().trim();
            if(!roomRateType.equals("n")) {
                while(true) {
                    System.out.println("Select New Room Rate Type (1: Published, 2: Normal, 3: Peak, 4: Promotion) > ");
                    Integer roomRateTypeEnum = sc.nextInt();
            
                    if(roomRateTypeEnum >= 1 && roomRateTypeEnum <=4) {
                        roomRate.setRateType(RateTypeEnum.values()[roomRateTypeEnum - 1]);  
                        break;
                    } else {
                        System.out.println("Invalid Option! Please try again!");
                    }
                }
            }
            System.out.println("Enter Room Rate Per Night (Enter -1 if no change)> ");
            BigDecimal roomRatePerNight = sc.nextBigDecimal();
            if(roomRatePerNight.compareTo(new BigDecimal("-1")) != 0) {
                roomRate.setRatePerNight(roomRatePerNight);
            }
            System.out.println("Enter Room Rate Type ID (Enter -1 if no change)> ");
            int roomType = sc.nextInt();
            if(roomType != -1) {
                try {
                    RoomType toChange = roomTypeSessionBeanRemote.viewRoomTypeDetails(Long.valueOf(roomType));
                    roomRate.setRoomType(toChange);
                } catch (RoomTypeNotFoundException ex) {
                    System.out.println("Room Type not found!");
                }
            }
            try {
                roomRateSessionBeanRemote.updateRoomRate(roomRate);
            } catch(RoomRateNotFoundException | UpdateRoomRateException ex) {
            System.out.println("An unknown error has occured while updating the Room Rate!: " + ex.getMessage() + "\n");
        }
        } catch (RoomRateNotFoundException ex) {
            System.out.println("An error has occured while retrieving the Room Rate: " + ex.getMessage() + "\n");
        }  
    }
    
    public void doDeleteRoomRate(RoomRate currentRoomRate) {
        Scanner sc = new Scanner(System.in);
        String input;
        
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation :: Delete Room Rate  ***\n");
        System.out.printf("Confirm Delete Room %s (Room Type ID: %d) (Enter 'Y' to Delete)> ", currentRoomRate.getName(), currentRoomRate.getRoomRateId());
        input = sc.nextLine().trim();
        
        if(input.equals("Y")) {
            try {
                roomRateSessionBeanRemote.deleteRoomRate(currentRoomRate.getRoomRateId());
                System.out.println("Room Rate is successfully deleted!");
            } catch (RoomRateNotFoundException ex){
                System.out.println("An error has occured while deleting the Room Rate: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Room Rate is not deleted!\n");
        }
    }
    
    public void viewAllRoomRates() {
        System.out.println("cant do this shit");
        
    }
    
    
}
