/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import entity.Reservation;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerExistException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wongj
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager em;

    public PartnerSessionBean() {
    }

    public Long createNewPartner(Partner newPartner) throws UnknownPersistenceException, PartnerExistException {
        try {
            em.persist(newPartner);
            em.flush();
            return newPartner.getPartnerId();
        } catch(PersistenceException ex) {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if(ex.getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new PartnerExistException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    public List<Partner> retrieveAllPartners() {
        Query query = em.createQuery("SELECT p FROM Partner p");
        return query.getResultList();
    }
    
    public Partner retrievePartnerByUsername(String username) throws PartnerNotFoundException
    {
        try
        {
            Query query = em.createQuery("SELECT p FROM Partner p WHERE p.username = :inUsername");
            query.setParameter("inUsername", username);
        
            return (Partner)query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex)
        {
            throw new PartnerNotFoundException("Partner Username " + username + " does not exist!");
        }
    }
    
    @Override
    public Partner partnerLogin(String username, String password) throws InvalidLoginCredentialException
    {
        try
        {   
            Partner partner = retrievePartnerByUsername(username);
            
            if(partner.getPassword().equals(password))
            {
                partner.getReservations().size();                
                return partner;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        }
        catch (PartnerNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
                
    }

    @Override
    public Long addReservation(Long partnerId, Long reservationId) throws PartnerNotFoundException, ReservationNotFoundException
    {
        Partner partner = em.find(Partner.class, partnerId);
        Reservation reservation = em.find(Reservation.class, reservationId);

        if (partner== null) 
        {
            throw new PartnerNotFoundException("Guest with id " + partnerId + "not found!");
        } else if (reservation == null)
        {
            throw new ReservationNotFoundException("Reservation with id " + reservationId + "not found!");
        } else
        {
            partner.getReservations().add(reservation);
            reservation.setPartner(partner);
        }

        return partner.getPartnerId();
    }
}
