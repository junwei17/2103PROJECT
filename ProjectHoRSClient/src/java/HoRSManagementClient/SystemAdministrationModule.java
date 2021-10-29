/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HoRSManagementClient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import entity.Employee;
import entity.Partner;
import java.util.List;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.exception.EmployeeUsernameExistException;
import util.exception.InvalidAccessRightException;
import util.exception.PartnerExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wongj
 */
public class SystemAdministrationModule {
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private Employee currentEmployee;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;

    public SystemAdministrationModule() {
    }

    public SystemAdministrationModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, Employee currentEmployee, PartnerSessionBeanRemote partnerSessionBeanRemote) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
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
            System.out.println("5: Exit to previous menu");
            response = 0;
            
            while(response < 1 || response > 7) {
                System.out.println("> ");
                
                response = sc.nextInt();
                if(response == 1) {
                    doCreateNewEmployee();
                } else if (response == 2) {
                    doViewAllEmployees();
                } else if (response == 3){
                    doCreateNewPartner();
                }
                else if(response == 5) {
                    break;
                }
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
            System.out.println("Select Access Right (1: ADMINISTRATOR, 2: OPERATION_MANAGER, 3:SALES_MANAGER, 4: EMPLOYEE)\n");
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
}