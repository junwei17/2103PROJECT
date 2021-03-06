/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.InvalidAccessRightException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wongj
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager entityManager;

    public EmployeeSessionBean() {
    }

    @Override
    public Long createNewEmployee(Employee newEmployee) throws EmployeeUsernameExistException, UnknownPersistenceException
    {
        try
        {
            entityManager.persist(newEmployee);
            entityManager.flush();

            return newEmployee.getEmployeeId();
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new EmployeeUsernameExistException();
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
    
    @Override
    public Employee retrieveEmployeeByUsername(String username) throws InvalidAccessRightException
    {
        System.out.println("doing the query next");
        Query query = entityManager.createQuery("SELECT e FROM Employee e WHERE e.employeeUsername = :inemployeeUsername");
        System.out.println("query passed");
        query.setParameter("inemployeeUsername", username);
        
        try
        {
            return (Employee)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new InvalidAccessRightException("Employee Username " + username + " does not exist!");
        }
    }
    
    @Override
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException
    {
        //System.out.println("reached login");
        try
        {   
            //System.out.println("Searching for matching username");
            Employee staffEntity = retrieveEmployeeByUsername(username);
            
            if(staffEntity.getPassword().equals(password))
            {
                //staffEntity.getSaleTransactionEntities().size();                
                return staffEntity;
            }
            else
            {
                //System.out.println("found nothing");
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        }
        catch(InvalidAccessRightException ex)
        {
            System.out.println("found nothing2");
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    /*@Override
    public String test() {
        System.out.println("remote does work");
        return "Hello";
    }*/
    @Override
    public List<Employee> retrieveAllEmployees() {
        Query query = entityManager.createQuery("SELECT e FROM Employee e");
        return query.getResultList();
    }
    
}

/*class Storage {
    RoomType roomType;
    int count;
    
    public String toString() {
        return this.roomType.getRoomTypeId() + " " + this.count;
    }
}*/
