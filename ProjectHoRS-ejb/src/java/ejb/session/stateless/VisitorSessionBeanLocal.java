/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Guest;
import javax.ejb.Local;
import util.exception.GuestExistException;
import util.exception.GuestNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author harmo
 */
@Local
public interface VisitorSessionBeanLocal {

    public Long registerAsGuest(Guest newGuest) throws GuestExistException, UnknownPersistenceException, InputDataValidationException;

    public Guest retrieveGuestByUsername(String username) throws GuestNotFoundException;

    public Guest guestLogin(String username, String password) throws InvalidLoginCredentialException;
    
}