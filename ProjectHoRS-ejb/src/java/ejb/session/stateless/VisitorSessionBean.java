/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Visitor;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.GuestExistException;
import util.exception.GuestNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author harmo
 */
@Stateless
public class VisitorSessionBean implements VisitorSessionBeanRemote, VisitorSessionBeanLocal {

    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager entityManager;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    
    
    public VisitorSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Guest retrieveGuestByUsername(String username) throws GuestNotFoundException
    {
        Query query = entityManager.createQuery("SELECT g FROM Guest g WHERE s.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (Guest)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new GuestNotFoundException("Staff Username " + username + " does not exist!");
        }
    }
    
    @Override
    public Guest guestLogin(String username, String password) throws InvalidLoginCredentialException
    {
        try
        {
            Guest guest = retrieveGuestByUsername(username);
            
            if(guest.getPassword().equals(password))
            {
                guest.getReservations().size();
                return guest;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        }
        catch(GuestNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

    @Override
    public Long registerAsGuest(Guest newGuest) throws GuestExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Visitor>>constraintViolations = validator.validate(newGuest);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                entityManager.persist(newGuest);
                entityManager.flush();

                return newGuest.getGuestId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new GuestExistException();
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Visitor>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
