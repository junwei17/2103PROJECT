/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Exceptions;
import entity.Room;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.ExceptionExistException;
import util.exception.RoomExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wongj
 */
@Stateless
public class ExceptionReport implements ExceptionReportRemote, ExceptionReportLocal {

    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager em;

    @Override
    public Long createExceptions(Exceptions exception) throws ExceptionExistException, UnknownPersistenceException {
        try {
            em.persist(exception);
            em.flush();
        
            return exception.getExceptionId();
        } catch (PersistenceException ex) {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new ExceptionExistException("here");
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


}
