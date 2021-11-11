/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Exceptions;
import entity.ReservationRoom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.ExceptionTypeEnum;
import util.exception.ExceptionExistException;
import util.exception.RoomNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author harmo
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @EJB
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @EJB
    private ExceptionReportLocal exceptionReportLocal;

    @EJB
    private AdminSessionBeanLocal adminSessionBeanLocal;

    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager em;
    
    
    

    @Schedule(hour = "2", minute = "0", second = "0", info = "allocateRoomTimer")    
    // For testing purpose, we are allowing the timer to trigger every 5 seconds instead of every 5 minutes
    // To trigger the timer once every 5 minutes instead, use the following the @Schedule annotation
    // @Schedule(hour = "*", minute = "*/5", info = "productEntityReorderQuantityCheckTimer")
    public void allocateRoomTimer()
    {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
        System.out.println("********** EjbTimerSessionBean.allocateRoomTimer(): Timeout at " + timeStamp);
        
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        
        List<Object[]> fufillList = adminSessionBeanLocal.reservationsToFulfill(date);
        if(!fufillList.isEmpty()) {
            for(Object[] obj : fufillList){
                List<Object> result = adminSessionBeanLocal.searchRooms((Date)obj[0], (Date)obj[1], ((ReservationRoom)obj[2]).getRoomType().getRoomTypeId());
                try {
                if(result.size() == 0) {
                    result = adminSessionBeanLocal.searchRooms((Date)obj[0], (Date)obj[1], ((ReservationRoom)obj[2]).getRoomType().getRoomTypeId() + 1);
                    if(result.size() == 0){
                        try {
                            Exceptions newExceptions = new Exceptions(((ReservationRoom)obj[2]).getReservation().getReservationId(), (((ReservationRoom)obj[2]).getReservationRoomId()), (((ReservationRoom)obj[2]).getRoomType().getRoomTypeId()), ExceptionTypeEnum.values()[1]);
                            exceptionReportLocal.createExceptions(newExceptions);
                        } catch (ExceptionExistException | UnknownPersistenceException ex) {
                            System.out.println("Unable to make Exception in Exception Report!");
                        }
                    } else {
                        adminSessionBeanLocal.setRoom(((ReservationRoom)obj[2]).getReservationRoomId(), roomSessionBeanLocal.viewRoomDetails((Long)result.get(0)).getRoomId());
                        try {
                            Exceptions newExceptions = new Exceptions(((ReservationRoom)obj[2]).getReservation().getReservationId(), (((ReservationRoom)obj[2]).getReservationRoomId()), (((ReservationRoom)obj[2]).getRoomType().getRoomTypeId()), ExceptionTypeEnum.values()[0]);
                            exceptionReportLocal.createExceptions(newExceptions);
                        } catch (ExceptionExistException | UnknownPersistenceException ex) {
                            System.out.println("Unable to make Exception in Exception Report!");
                        }
                    }
                } else {
                    adminSessionBeanLocal.setRoom(((ReservationRoom)obj[2]).getReservationRoomId(), roomSessionBeanLocal.viewRoomDetails((Long)result.get(0)).getRoomId());
                }
                } catch (RoomNotFoundException ex) {
                    System.out.println("Room not Found!");
                }
            }
        }
    
        
        
    }
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
