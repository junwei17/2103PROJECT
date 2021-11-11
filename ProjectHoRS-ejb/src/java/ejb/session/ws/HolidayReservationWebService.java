/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import entity.Partner;
import entity.Reservation;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author harmo
 */
@WebService(serviceName = "HolidayReservationWebService")
@Stateless()
public class HolidayReservationWebService {

    @EJB
    private ReservationSessionBeanLocal reservationSessionBeanLocal;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;
    
    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager em;

    @EJB
    private RoomSessionBeanLocal roomSessionBeanLocal;
    
    

    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "searchRooms")
    public List<Object[]> searchRooms(@WebParam(name = "n") String txt) {
        return null;
    }
    
    @WebMethod(operationName = "partnerLogin")
    public Partner partnerLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws InvalidLoginCredentialException {

        Partner partner = partnerSessionBeanLocal.partnerLogin(username, password);


        em.detach(partner);

        for(Reservation r : partner.getReservations())
        {
            em.detach(r);
            r.setPartner(null);
        }
        return partner;
    }
    
    @WebMethod(operationName = "viewAllReservations")
    public List<Reservation> viewAllReservations(@WebParam(name = "partnerId") Long partnerId)  {

        Partner partner = em.find(Partner.class, partnerId);

        em.detach(partner);

        for(Reservation r : partner.getReservations())
        {
            em.detach(r);
            r.setPartner(null);
        }
        
        
        return partner.getReservations();
    }
    
    @WebMethod(operationName = "viewReservationDetails")
    public Reservation viewReservationDetails(@WebParam(name = "reservationId") Long reservationId) throws ReservationNotFoundException {

        return reservationSessionBeanLocal.viewReservationDetails(reservationId);
    }
    
    
    
}
