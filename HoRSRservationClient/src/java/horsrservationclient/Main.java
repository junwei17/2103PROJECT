/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsrservationclient;

import ejb.session.stateless.FrontOfficeModuleSessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.VisitorSessionBeanRemote;

/**
 *
 * @author harmo
 */
public class Main {

    @EJB
    private static FrontOfficeModuleSessionBeanRemote frontOfficeModuleSessionBeanRemote;

    @EJB
    private static VisitorSessionBeanRemote visitorSessionBeanRemote;
    @EJB
    private static RoomSessionBeanRemote roomSessionBeanRemote;
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    

    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(visitorSessionBeanRemote, roomSessionBeanRemote, reservationSessionBeanRemote, frontOfficeModuleSessionBeanRemote);
        mainApp.runApp();
    }
    
}
