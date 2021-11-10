/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import entity.Employee;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.exception.EmployeeUsernameExistException;
import util.exception.InvalidAccessRightException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author harmo
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;

    @EJB
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager em;

    
       
    @PostConstruct
    public void postConstruct()
    {
        try
        {
            employeeSessionBeanLocal.retrieveEmployeeByUsername("sysadmin");
        }
        catch(InvalidAccessRightException ex)
        {
            initializeData();
        }
    }
    
    private void initializeData() throws EmployeeUsernameExistException, UnknownPersistenceException 
    {
        try
        {
            employeeSessionBeanLocal.createNewEmployee(new Employee("sysadmin", "password", AccessRightEnum.ADMINISTRATOR));
            employeeSessionBeanLocal.createNewEmployee(new Employee("opmanager", "password", AccessRightEnum.OPERATION_MANAGER));
            employeeSessionBeanLocal.createNewEmployee(new Employee("salesmanager", "password", AccessRightEnum.SALES_MANAGER));
            employeeSessionBeanLocal.createNewEmployee(new Employee("guestrelo", "password", AccessRightEnum.EMPLOYEE));
            
            
        } catch
        
        
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
