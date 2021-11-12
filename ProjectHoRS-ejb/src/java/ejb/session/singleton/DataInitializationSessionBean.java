
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import ejb.session.stateless.VisitorSessionBeanLocal;
import entity.Employee;
import entity.Guest;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.enumeration.RateTypeEnum;
import util.exception.EmployeeUsernameExistException;
import util.exception.GuestExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.RoomExistException;
import util.exception.RoomRateExistException;
import util.exception.RoomTypeNotFoundException;
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
    private VisitorSessionBeanLocal visitorSessionBeanLocal;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

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
    
    private void initializeData() 
    {
        try
        {
            employeeSessionBeanLocal.createNewEmployee(new Employee("sysadmin", "password", AccessRightEnum.ADMINISTRATOR));
            employeeSessionBeanLocal.createNewEmployee(new Employee("opmanager", "password", AccessRightEnum.OPERATION_MANAGER));
            employeeSessionBeanLocal.createNewEmployee(new Employee("salesmanager", "password", AccessRightEnum.SALES_MANAGER));
            employeeSessionBeanLocal.createNewEmployee(new Employee("guestrelo", "password", AccessRightEnum.GUEST_RELATION_OFFICER));
            
            roomTypeSessionBeanLocal.createRoomType(new RoomType("Deluxe Room"));
            roomTypeSessionBeanLocal.createRoomType(new RoomType("Premier Room"));
            roomTypeSessionBeanLocal.createRoomType(new RoomType("Family Room"));
            roomTypeSessionBeanLocal.createRoomType(new RoomType("Junior Suite"));
            roomTypeSessionBeanLocal.createRoomType(new RoomType("Grand Suite"));
           
            
            RoomType roomType = roomTypeSessionBeanLocal.retreieveRoomTypeByName("Deluxe Room");
            roomSessionBeanLocal.createRoom(new Room("0101", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0201", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0301", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0401", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0501", true), roomType.getRoomTypeId());
            roomRateSessionBeanLocal.createRoomRate(new RoomRate("Deluxe Room Published", RateTypeEnum.PUBLISHED, new BigDecimal("100")), roomType.getRoomTypeId());
            roomRateSessionBeanLocal.createRoomRate(new RoomRate("Deluxe Room Normal", RateTypeEnum.NORMAL, new BigDecimal("50")), roomType.getRoomTypeId());
            roomType = roomTypeSessionBeanLocal.retreieveRoomTypeByName("Premier Room");
            roomSessionBeanLocal.createRoom(new Room("0102", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0202", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0302", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0402", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0502", true), roomType.getRoomTypeId());
            roomRateSessionBeanLocal.createRoomRate(new RoomRate("Premier Room Published", RateTypeEnum.PUBLISHED, new BigDecimal("200")), roomType.getRoomTypeId());
            roomRateSessionBeanLocal.createRoomRate(new RoomRate("Premier Room Normal", RateTypeEnum.NORMAL, new BigDecimal("100")), roomType.getRoomTypeId());
            roomType = roomTypeSessionBeanLocal.retreieveRoomTypeByName("Family Room");            
            roomSessionBeanLocal.createRoom(new Room("0103", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0203", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0303", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0403", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0503", true), roomType.getRoomTypeId());
            roomRateSessionBeanLocal.createRoomRate(new RoomRate("Family Room Published", RateTypeEnum.PUBLISHED, new BigDecimal("300")), roomType.getRoomTypeId());
            roomRateSessionBeanLocal.createRoomRate(new RoomRate("Family Room Normal", RateTypeEnum.NORMAL, new BigDecimal("150")), roomType.getRoomTypeId());
            roomType = roomTypeSessionBeanLocal.retreieveRoomTypeByName("Junior Suite");
            roomSessionBeanLocal.createRoom(new Room("0104", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0204", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0304", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0404", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0504", true), roomType.getRoomTypeId());
            roomRateSessionBeanLocal.createRoomRate(new RoomRate("Junior Suite Published", RateTypeEnum.PUBLISHED, new BigDecimal("400")), roomType.getRoomTypeId());
            roomRateSessionBeanLocal.createRoomRate(new RoomRate("Junior Suite Normal", RateTypeEnum.NORMAL, new BigDecimal("200")), roomType.getRoomTypeId());
            roomType = roomTypeSessionBeanLocal.retreieveRoomTypeByName("Grand Suite");
            roomSessionBeanLocal.createRoom(new Room("0105", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0205", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0305", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0405", true), roomType.getRoomTypeId());
            roomSessionBeanLocal.createRoom(new Room("0505", true), roomType.getRoomTypeId());
            roomRateSessionBeanLocal.createRoomRate(new RoomRate("Grand Suite Published", RateTypeEnum.PUBLISHED, new BigDecimal("500")), roomType.getRoomTypeId());
            roomRateSessionBeanLocal.createRoomRate(new RoomRate("Grand Suite Normal", RateTypeEnum.NORMAL, new BigDecimal("250")), roomType.getRoomTypeId());
            
            
        } catch (EmployeeUsernameExistException | UnknownPersistenceException | RoomExistException | RoomRateExistException  | RoomTypeNotFoundException ex)
        {
            ex.printStackTrace();
        }
        
    
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
