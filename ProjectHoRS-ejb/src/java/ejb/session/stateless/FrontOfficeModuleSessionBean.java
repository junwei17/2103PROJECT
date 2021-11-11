/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.ReservationRoom;
import entity.Room;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.ReservationExistException;
import util.exception.ReservationRoomExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wongj
 */
@Stateless
public class FrontOfficeModuleSessionBean implements FrontOfficeModuleSessionBeanRemote, FrontOfficeModuleSessionBeanLocal {

    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager em;
    
    @Override
    public List<Object[]> searchRooms(Date startDate, Date endDate) {
        Query query = em.createQuery("SELECT r.roomType, COUNT(r) FROM Room r WHERE r NOT IN (SELECT rr.room FROM ReservationRoom rr WHERE rr.reservation.startDate < :inEndDate AND rr.reservation.endDate > :inStartDate) GROUP BY r.roomType");
        query.setParameter("inEndDate", endDate);
        query.setParameter("inStartDate", startDate);
        return query.getResultList();
    }
    
    @Override
    public Long createReservation(Reservation newReservation) throws ReservationExistException, UnknownPersistenceException{
         try {
            em.persist(newReservation);
            em.flush();
        
            return newReservation.getReservationId();
        } catch (PersistenceException ex) {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new ReservationExistException();
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
    public Long reserveRoom(ReservationRoom newReservationRoom , Long reservationId) throws ReservationRoomExistException, UnknownPersistenceException{
         try {
            Reservation reservation = em.find(Reservation.class, reservationId);
            newReservationRoom.setReservation(reservation);
            reservation.getReservationRooms().add(newReservationRoom);
            em.persist(newReservationRoom);
            em.flush();
        
            return newReservationRoom.getReservationRoomId();
        } catch (PersistenceException ex) {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new ReservationRoomExistException();
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
    
    
    /*@Override
    public List*/

}

    /*@PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager em;
    
    public List<Pair> searchRooms(Date check_in_date, Date check_out_date) {
        Query query = em.createQuery("SELECT r.roomType, COUNT(r) FROM Room r GROUP BY r.roomType ORDER BY r.roomType ASC");
        //Long numberOfRooms = (Long)query.getSingleResult();
        return query.getResultList();
    }*/
    
