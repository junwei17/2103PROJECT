/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.FrontOfficeModuleSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import entity.Partner;
import entity.Reservation;
import entity.ReservationRoom;
import entity.RoomType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
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
    private FrontOfficeModuleSessionBeanLocal frontOfficeModuleSessionBeanLocal;

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
    public List<Object[]> searchRooms(@WebParam(name = "startDate") String startDate, @WebParam(name = "endDate") String endDate) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy"); 

            Date dateStart = format.parse(startDate);
            Date dateEnd = format.parse(endDate);
        
        return frontOfficeModuleSessionBeanLocal.searchRooms(dateStart, dateEnd);
    }
    
    @WebMethod(operationName = "partnerLogin")
    public Partner partnerLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws InvalidLoginCredentialException {

        System.out.println("hi");
        Partner partner = partnerSessionBeanLocal.partnerLogin(username, password);
        partner.getReservations().size();

        em.detach(partner);

        for(Reservation r : partner.getReservations())
        {
            em.detach(r);
            r.setPartner(null);
        }
        System.out.println("bye");
        return partner;
    }
    
    @WebMethod(operationName = "viewAllReservationsPartner")
    public List<Reservation> viewAllReservationsPartner(@WebParam(name = "partnerId") Long partnerId) throws PartnerNotFoundException, ReservationNotFoundException {

        List<Reservation> reservations = reservationSessionBeanLocal.viewAllReservationsPartner(partnerId);

        for(Reservation r : reservations)
        {
            em.detach(r);
            r.setPartner(null);
            r.setReservationRooms(null);
            r.setGuest(null);
        }
        
        
        return reservations;
    }
    
    @WebMethod(operationName = "viewReservationDetails")
    public Reservation viewReservationDetails(@WebParam(name = "reservationId") Long reservationId) throws ReservationNotFoundException {

        Reservation reservation = reservationSessionBeanLocal.viewReservationDetails(reservationId);
        
        em.detach(reservation);
        
        reservation.setPartner(null);
        reservation.setGuest(null);
        for(ReservationRoom rr : reservation.getReservationRooms())
        {
            em.detach(rr);
            rr.setReservation(null);
        }
        
        return reservation;
    }
    
 
    
}
