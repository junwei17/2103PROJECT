/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HoRSManagementClient;

import ejb.session.stateless.AdminSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import entity.Employee;
import entity.Partner;
import entity.ReservationRoom;
import entity.RoomType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javafx.util.Pair;
import util.enumeration.AccessRightEnum;
import util.exception.EmployeeUsernameExistException;
import util.exception.InvalidAccessRightException;
import util.exception.PartnerExistException;
import util.exception.RoomNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wongj
 */
public class SystemAdministrationModule {
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private Employee currentEmployee;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private AdminSessionBeanRemote adminSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;

    public SystemAdministrationModule() {
    }

    public SystemAdministrationModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, Employee currentEmployee, PartnerSessionBeanRemote partnerSessionBeanRemote, AdminSessionBeanRemote adminSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote) {
        this();
        this.adminSessionBeanRemote =  adminSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void menuSystemAminstration() throws InvalidAccessRightException {
        if(currentEmployee.getAccessRightEnum() != AccessRightEnum.ADMINISTRATOR) {
            throw new InvalidAccessRightException("You do not have the ADMINISTRATOR rights to access the system administration module");
        }
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: System Adminstration ***\n");
            System.out.println("1: Create New Employee");
            System.out.println("2: View All Employees");
            System.out.println("3: Create New Partner");
            System.out.println("4: View All Partners");
            System.out.println("5: Allocate Rooms");
            System.out.println("6: Exit to previous menu");
            response = 0;
            
            while(response < 1 || response > 6) {
                System.out.println("> ");
                
                response = sc.nextInt();
                if(response == 1) {
                    doCreateNewEmployee();
                } else if (response == 2) {
                    doViewAllEmployees();
                } else if (response == 3){
                    doCreateNewPartner();
                } else if (response == 4) {
                    viewAllPartners();
                } else if (response == 5) {
                    doAllocateRooms();
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
    
    public void doCreateNewEmployee() {
        Scanner sc = new Scanner(System.in);
        Employee newEmployee = new Employee();
        
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: System Adminstration :: Create New Employee ***\n");
        System.out.println("Enter New Employee Username> ");
        newEmployee.setEmployeeUsername(sc.nextLine().trim());
        System.out.println("Enter New Employee Password> ");
        newEmployee.setPassword(sc.nextLine().trim());
        
        while(true) {
            System.out.println("Select Access Right (1: ADMINISTRATOR, 2: OPERATION_MANAGER, 3:SALES_MANAGER, 4: GUEST_RELATION_OFFICER)\n");
            Integer accessRight = sc.nextInt();
            
            if(accessRight >= 1 && accessRight <= 4) {
                newEmployee.setAccessRightEnum(AccessRightEnum.values()[accessRight-1]);
                break;
            } else {
                System.out.println("Invalid Option: Please Try Again! \n");
            }
        }
        try {
            Long newEmployeeId = employeeSessionBeanRemote.createNewEmployee(newEmployee);
            System.out.println("New Employeee created successfully, with the username of " + newEmployee.getEmployeeUsername()+ " and Employee Id of " + newEmployeeId + ".");
        } catch(EmployeeUsernameExistException ex) {
            System.out.println("An error has occured while creating the new Employee! The username already exist!\n");
        } catch (UnknownPersistenceException ex) {
            System.out.println("An unknown error has occured whilre creating new Employee! "+ ex.getMessage() + "\n");
        }
    }
    
    private void doViewAllEmployees(){
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: System Adminstration :: View All Employees ***\n");
        List<Employee> employees = employeeSessionBeanRemote.retrieveAllEmployees();
        System.out.printf("%8s%20s%20s%20s\n","Employee Id", "Access Right", "Username", "Password");
        for(Employee employee : employees) {
            System.out.printf("%8s%20s%20s%20s\n", employee.getEmployeeId(), employee.getAccessRightEnum(), employee.getEmployeeUsername(), employee.getPassword());
        }
        System.out.println("Press Any Key To Continue");
        sc.nextLine();
    }
    
    private void doCreateNewPartner() {
        Scanner sc = new Scanner(System.in);
        Partner newPartner = new Partner();
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: System Adminstration :: Create New Partner ***\n");
        System.out.println("Enter New Partner Username> ");
        newPartner.setUsername(sc.nextLine().trim());
        System.out.println("Enter New Partner Password> ");
        newPartner.setPassword(sc.nextLine().trim());
        
        try{
            //System.out.println(newPartner);
            Long newPartnerId = partnerSessionBeanRemote.createNewPartner(newPartner);
            System.out.println("Partner Account created successfully! \n");
        } catch(PartnerExistException ex) {
            System.out.println("An error has occured while creating the new Partner! The partner already exist!\n");
        } catch (UnknownPersistenceException ex) {
            System.out.println("An unknown error has occured whilre creating new Partner! "+ ex.getMessage() + "\n");
        }
    }
    
    private void viewAllPartners() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: System Adminstration :: View All Partners ***\n");
        
        List<Partner> partners = partnerSessionBeanRemote.retrieveAllPartners();
        System.out.println("Partners retrieved!");
        System.out.printf("%8s%20s%20s\n","Partner Id", "Partner Username", "Password");
        
        for(Partner partner : partners) {
            System.out.printf("%8s%20s%20s\n", partner.getPartnerId(), partner.getUsername(), partner.getPassword());
        }
        
        System.out.println("Press any key to continue...> ");
        sc.nextLine();
    }
    
    public void doAllocateRooms() {
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) :: System Adminstration :: Allocate Rooms ***\n");
        System.out.println("Enter the Date to Allocate Rooms (dd/MM/yyyy)> ");
        String start = sc.nextLine().trim();
        Date dateStart = null;
        if(null != start && start.trim().length() > 0){
            try {
                dateStart = format.parse(start);
            } catch (ParseException ex) {
                System.out.println("Problems parsing the given date!");
            }
        }
        List<Object[]> fufillList = adminSessionBeanRemote.reservationsToFulfill(dateStart);
        if(!fufillList.isEmpty()) {
            for(Object[] obj : fufillList){
                System.out.println("obj is " + (Date)obj[0] + (Date)obj[1] + ((ReservationRoom)obj[2]).getReservationRoomId());
                Long result = adminSessionBeanRemote.searchRooms((Date)obj[0], (Date)obj[1], ((ReservationRoom)obj[2]).getRoomType().getRoomTypeId());
                System.out.println("result here is " + result);
                try {
                if(result == -1) {
                    //create new Exception
                    result = adminSessionBeanRemote.searchRooms((Date)obj[0], (Date)obj[1], ((ReservationRoom)obj[2]).getRoomType().getRoomTypeId() + 1);
                    System.out.println("result there is " + result);
                    if(result == -1){
                        //create new Exception
                    } else {
                        System.out.println(roomSessionBeanRemote.viewRoomDetails(result).getRoomId() + " here");
                        adminSessionBeanRemote.setRoom((ReservationRoom)obj[2], roomSessionBeanRemote.viewRoomDetails(result) );
                        //((ReservationRoom)obj[2]).setRoom(roomSessionBeanRemote.viewRoomDetails(result));
                    }
                } else {
                    System.out.println("herereee");
                    System.out.println(roomSessionBeanRemote.viewRoomDetails(result).getRoomId() + " there");
                    ((ReservationRoom)obj[2]).setRoom(roomSessionBeanRemote.viewRoomDetails(result));
                }
                } catch (RoomNotFoundException ex) {
                    System.out.println("Room not Found!");
                }
                System.out.println("done");
            }
        }
    }
}