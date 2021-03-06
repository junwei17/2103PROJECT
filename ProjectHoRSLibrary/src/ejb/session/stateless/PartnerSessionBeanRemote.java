/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import entity.Reservation;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerExistException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wongj
 */
@Remote
public interface PartnerSessionBeanRemote {
    public Long createNewPartner(Partner newPartner) throws UnknownPersistenceException, PartnerExistException;
    public List<Partner> retrieveAllPartners();
    public Partner partnerLogin(String username, String password) throws InvalidLoginCredentialException;

    public Partner retrievePartnerByUsername(String username) throws PartnerNotFoundException;
    public Long addReservation(Long partnerId, Long reservationId) throws PartnerNotFoundException, ReservationNotFoundException;
}
