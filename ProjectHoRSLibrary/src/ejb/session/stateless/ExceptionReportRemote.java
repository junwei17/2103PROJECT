/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Exceptions;
import java.util.List;
import javax.ejb.Remote;
import util.exception.ExceptionExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wongj
 */
@Remote
public interface ExceptionReportRemote {
    public Long createExceptions(Exceptions exception) throws ExceptionExistException, UnknownPersistenceException;
    public List<Exceptions> retrieveAllExceptions();
}
