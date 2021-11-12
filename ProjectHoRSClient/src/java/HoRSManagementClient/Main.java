/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HoRSManagementClient;

//import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.AdminSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ExceptionReportRemote;
import ejb.session.stateless.FrontOfficeModuleSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import ejb.session.stateless.VisitorSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author wongj
 */
public class Main {

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    @EJB
    private static PartnerSessionBeanRemote partnerSessionBeanRemote;
    @EJB
    private static RoomSessionBeanRemote roomSessionBeanRemote;
    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    @EJB
    private static RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    @EJB
    private static FrontOfficeModuleSessionBeanRemote frontOfficeModuleSessionBeanRemote;
    @EJB
    private static AdminSessionBeanRemote adminSessionBeanRemote;
    @EJB
    private static ExceptionReportRemote exceptionReportRemote;
    @EJB
    private static VisitorSessionBeanRemote visitorSessionBeanRemote;
    
    public static void main(String[] args) {
         MainApp mainApp = new MainApp(adminSessionBeanRemote, employeeSessionBeanRemote, partnerSessionBeanRemote, roomTypeSessionBeanRemote, roomSessionBeanRemote, roomRateSessionBeanRemote, frontOfficeModuleSessionBeanRemote, exceptionReportRemote, visitorSessionBeanRemote);
         mainApp.runApp();
    }
    
}
