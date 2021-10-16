/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projecthorsclient;

//import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author wongj
 */
public class Main {

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    
    public static void main(String[] args) {
         MainApp mainApp = new MainApp(employeeSessionBeanRemote);
         mainApp.runApp();
    }
    
}
