/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HoRSManagementClient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author wongj
 */
public class MainApp {
    
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private Employee currentEmployee;
            
    public MainApp() {
    }
    
    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
    }
    
    public void runApp() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Welcome to Hotel Reservation System (v1.0) ***\n");
        System.out.println("1: Login");
        System.out.println("2: Create new employee\n");
        System.out.print("> ");
        int response = sc.nextInt();
        
        if(response == 1){
            try{
                doLogin();
                System.out.println("Login successful!\n");
                System.out.println("Welcome! " + currentEmployee.getEmployeeUsername() + "\n");
            } catch(InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
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
}
