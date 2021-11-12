/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Partner;
import entity.Reservation;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.GuestNotFoundException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author harmo
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager em;

    @Override
    public Reservation viewReservationDetails(Long reservationId) throws ReservationNotFoundException 
    {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.ReservationId = :inReservationId");
        query.setParameter("inReservationId", reservationId);
        
        try {
            return(Reservation)query.getSingleResult();
        }catch (NoResultException | NonUniqueResultException ex) {
            throw new ReservationNotFoundException("The Reservation for " + reservationId + " does not exists!");
        }
        
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
    
    @Override
    public List<Reservation> viewAllReservationsPartner(Long partnerId) throws PartnerNotFoundException, ReservationNotFoundException
    {
        Partner partner = em.find(Partner.class, partnerId);
        if (partner == null) 
        {
            throw new PartnerNotFoundException("Guest not found!");
        }
        
        if(partner.getReservations().isEmpty())
        {
            throw new ReservationNotFoundException("No Reservation Found!"); 
        }
        else
        {
            return partner.getReservations();
        }
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
