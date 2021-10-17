/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HoRSManagementClient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.exception.EmployeeUsernameExistException;
import util.exception.InvalidAccessRightException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wongj
 */
public class SystemAdminstrationModule {
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private Employee currentEmployee;

    public SystemAdminstrationModule() {
    }

    public SystemAdminstrationModule(EmployeeSessionBeanRemote employeeSessionBeanRemote) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
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
            response = 0;
            
            while(response < 1 || response > 7) {
                System.out.println("> ");
                
                response = sc.nextInt();
                if(response == 1) {
                    doCreateNewEmployee();
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
            System.out.println("Select Access Right (1: ADMINISTRATOR, 2: OPERATION_MANAGER, 3:SALES_MANAGER, 4: EMPLOYEE\n");
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
    
    
}
