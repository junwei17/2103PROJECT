/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Reservation;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.GuestNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author harmo
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager em;

    @Override
    public String viewReservationDetails(Long reservationId) throws ReservationNotFoundException 
    {
        Reservation reservation = em.find(Reservation.class, reservationId);
        if (reservation == null) 
        {
            throw new ReservationNotFoundException("No Reservation Found!");
        }
        
        String reservationDetails = "Reservation Start Date: " + reservation.getStartDate() + ".\n";
        reservationDetails += "Reservation End Date: " + reservation.getEndDate() + ".\n";
        reservationDetails += "Number of Rooms: " + reservation.getNumberOfRooms() + ".\n";
        reservationDetails += "Price: " + reservation.getFee() + ".\n";
        return reservationDetails;
    }
    
    @Override
    public List<Reservation> viewAllReservations(Long guestId) throws GuestNotFoundException, ReservationNotFoundException
    {
        Guest guest = em.find(Guest.class, guestId);
        if (guest == null) 
        {
            throw new GuestNotFoundException("Guest not found!");
        }
        
        if(guest.getReservations().isEmpty())
        {
            throw new ReservationNotFoundException("No Reservation Found!"); 
        }
        else
        {
            return guest.getReservations();
        }
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
