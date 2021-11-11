    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystem;

import java.util.Scanner;
import ws.client.HolidayReservationWebService_Service;

/**
 *
 * @author harmo
 */
public class HolidayReservationSystem {
    public static HolidayReservationWebService_Service service;
    
    
    public static void main(String[] args) 
    {
        MainApp mainApp = new MainApp(service);
        mainApp.runApp();
    }
    
       
    
}
