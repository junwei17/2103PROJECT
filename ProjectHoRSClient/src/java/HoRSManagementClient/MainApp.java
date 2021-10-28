/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HoRSManagementClient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author wongj
 */
public class MainApp {
    
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private Employee currentEmployee;
    private SystemAdministrationModule systemAdministrationModule;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
            
    public MainApp() {
    }
    
    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
    }
    
    public void runApp() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to Hotel Reservation System (v1.0) ***\n");
            response = 0;
            System.out.println("1: Login");
            if(currentEmployee != null) {
                System.out.println("2: Logout");
            }
            System.out.println("3: Exit");
            while (response < 1 || response > 3) {
                System.out.print("> ");
                response = sc.nextInt();
            
                if(response == 1){
                    try{
                        doLogin();
                        System.out.println("Login successful!\n");
                        System.out.println("Welcome, " + currentEmployee.getEmployeeUsername() + "!\n");
                        systemAdministrationModule = new SystemAdministrationModule(employeeSessionBeanRemote, currentEmployee, partnerSessionBeanRemote);
                        menuMain();
                    } catch(InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 2){
                    String message = doLogout();
                    System.out.println(message);
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
    
    private void doLogin() throws InvalidLoginCredentialException
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** HoRS System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            //System.out.println("checking for current employee 1" + username + " " + password);
            //System.out.println(employeeSessionBeanRemote.test());
            currentEmployee = employeeSessionBeanRemote.employeeLogin(username, password);      
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    public String doLogout() {
        if(this.currentEmployee == null) {
            return ("No employee currently logged in!");
        } else {
            this.currentEmployee = null;
            return ("Employee successfully logged out");
        }
    }
    
    private void menuMain() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true){
            System.out.println("*** HoRS System ***\n");
            System.out.println("You are login as " + currentEmployee.getEmployeeUsername() + " with " + currentEmployee.getAccessRightEnum().toString() + " rights\n");
            System.out.println("1: System Adminstration");
            System.out.println("2: Return to main menu to logout\n");
            response = 0;
            
            while (response < 1 || response > 2) {
                System.out.print(">");
                response = sc.nextInt();
                
                if(response == 1){
                    try {
                        systemAdministrationModule.menuSystemAminstration();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    break;
                }
            }
            if(response == 2){
                break;
            }
        }
    }
}
