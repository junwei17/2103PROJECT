/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.RoomSessionBeanLocal;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author harmo
 */
@WebService(serviceName = "HolidayReservationWebService")
@Stateless()
public class HolidayReservationWebService {

    @EJB
    private RoomSessionBeanLocal roomSessionBeanLocal;

    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "searchRooms")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
}
