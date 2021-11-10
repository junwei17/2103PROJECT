/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Remote;
import util.exception.InvalidAccessRightException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wongj
 */
@Remote
public interface EmployeeSessionBeanRemote {
    public Long createNewEmployee(Employee newEmployee) throws EmployeeUsernameExistException, UnknownPersistenceException;
    public Employee retrieveEmployeeByUsername(String username) throws InvalidAccessRightException;
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;
    public List<Employee> retrieveAllEmployees();
    //public String test();
}
