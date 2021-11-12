/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Visitor;
import javax.ejb.Local;
import util.exception.GuestExistException;
import util.exception.GuestNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.VisitorNotFoundException;

/**
 *
 * @author harmo
 */
@Local
public interface VisitorSessionBeanLocal {

    public Long registerAsGuest(Guest newGuest) throws GuestExistException, UnknownPersistenceException, InputDataValidationException;

    public Guest retrieveGuestByUsername(String username) throws GuestNotFoundException;

    public Guest guestLogin(String username, String password) throws InvalidLoginCredentialException;

    public Visitor retrieveVisitorByEmail(String email) throws VisitorNotFoundException;

    public Long createVisitor(Visitor newVisitor) throws UnknownPersistenceException;

    public Long addReservation(Long guestId, Long reservationId) throws GuestNotFoundException, ReservationNotFoundException;
    
}
