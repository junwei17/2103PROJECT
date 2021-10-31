/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HoRSManagementClient;

import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.RoomType;
import java.util.Scanner;
import util.enumeration.AmenitiesEnum;
import util.exception.RoomExistException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRoomTypeException;

/**
 *
 * @author wongj
 */
public class HotelOperationModule {

    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    
    public HotelOperationModule() {
    }

    public HotelOperationModule(RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote) {
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
    }
    
    public void menuHotelOperation() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: Hotel Operation ***\n");
            System.out.println("1: Create New Room Type");
            System.out.println("2: View Room Type Details");
            System.out.println("3: Update Room Type");
            System.out.println("4: Delete Room Type");
            System.out.println("5: View All Room Type");
            System.out.println("6: Exit to the previous menu");
            response = 0;
            
            while(response < 1 || response > 7) {
                System.out.println("> ");
                
                response = sc.nextInt();
                if(response == 1) {
                    doCreateNewRoomType();
                } else if (response == 2) {
                    doViewRoomTypeDetails();
                } else if (response == 3){
                    doUpdateRoomType();
                } else if (response == 4) {
                    doDeleteRoomType();
                } else if (response == 5) {
                    viewAllRoomTypes();
                }
                else if(response == 6) {
                    break;
                }
            }
            if(response == 6) {
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
        System.out.println("Enter New Room Type Size> ");
        newRoomType.setSize(sc.nextInt());
        System.out.println("Enter New Room Type Bed> ");
        newRoomType.setBed(sc.nextLine().trim());
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
            System.out.printf("%8s%20s%20s%d%20s%d", "Room Type ID", "Name", "Description", "Size", "Bed", "Capacity");
            System.out.printf("%8s%20s%20s%d%20s%d", roomType.getRoomTypeId(), roomType.getName(), roomType.getDescription(), roomType.getSize(), roomType.getBed(), roomType.getCapacity());
            System.out.println("---------------------------");
            System.out.println("1:Update Room Type");
            System.out.println("2:Delete Room Type");
            System.out.println("> ");
            response = sc.nextInt();
            
            if(response == 1) {
                doUpdateRoomType(roomType);
            } else if(response == 2) {
                doDeleteRoomType(roomType);
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
        
        System.out.println("Enter Room Type Size(Enter -1 if no change)> ");
        numberInput = sc.nextInt();
        if(numberInput == -1) {
            roomType.setSize(numberInput);
        }
        
        System.out.println("Enter Room Type Bed(blank if no change)> ");
        input = sc.nextLine().trim();
        if(input.length() > 0) {
            roomType.setBed(input);
        }
        
        System.out.println("Enter Room Type Capacity(blank if no change)> ");
        numberInput = sc.nextInt();
        if(numberInput == -1) {
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
            System.out.println("An unknown error has occured while creating the room type!: " + ex.getMessage() + "\n");
        }
        
    }
    
    public void doDeleteRoomType(RoomType roomType) {
        
    }
}
