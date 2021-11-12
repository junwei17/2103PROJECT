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
import entity.Visitor;
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
        /*Query query = em.createQuery("SELECT r.roomType, COUNT(r) FROM Room r WHERE r.roomId NOT IN (SELECT rr.room.roomId FROM ReservationRoom rr WHERE rr.reservation.startDate < :inEndDate AND rr.reservation.endDate > :inStartDate) GROUP BY r.roomType");
        //Query query = em.createQuery("SELECT table1.roomType, (table1.number - table2.number) AS available FROM (SELECT r.roomType AS roomType, COUNT(r) AS number FROM Room r GROUP BY r.roomType)table1, (SELECT rr.roomType AS roomType, COUNT(rr) AS number FROM ReservationRoom rr WHERE rr.reservation.startDate < :inEndDate AND rr.reservation.endDate > :inStartDate GROUP BY rr.roomType)table2 WHERE table1.roomType = table2.roomType)");
        query.setParameter("inEndDate", endDate);
        query.setParameter("inStartDate", startDate);
        return query.getResultList();*/
        List<Object[]> availableList = searchAvailableRooms();
        List<Object[]> inUseList = searchRoomsInUse(startDate, endDate);
        for(Object[] available : availableList){
            RoomType roomType = (RoomType)available[0];
            Long count = (Long)available[1];
            for(Object[] inUse : inUseList) {
                RoomType roomType2 = (RoomType)inUse[0];
                Long count2 = (Long)inUse[1];
                if(roomType2.getRoomTypeId() == roomType.getRoomTypeId()) {
                    count -= count2;
                }
                available[1] = count;
            }
        }
        return availableList;
    }
    
    //@Override
    public List<Object[]> searchRoomsInUse(Date startDate, Date endDate) {
        Query query = em.createQuery("SELECT rr.roomType AS roomType, COUNT(rr) AS number FROM ReservationRoom rr WHERE rr.reservation.startDate < :inEndDate AND rr.reservation.endDate > :inStartDate GROUP BY rr.roomType");
        query.setParameter("inEndDate", endDate);
        query.setParameter("inStartDate", startDate);
        return query.getResultList();
    }
    
    //@Override
    public List<Object[]> searchAvailableRooms() {
        Query query = em.createQuery("SELECT r.roomType AS roomType, COUNT(r) AS number FROM Room r GROUP BY r.roomType");
        return query.getResultList();
    }
    
    @Override
    public List<ReservationRoom> allocatedRooms(Long visitorId) {
        Query query = em.createQuery("SELECT rr FROM ReservationRoom rr WHERE rr.reservation.visitor.visitorId = :inVisitorId");
        query.setParameter("inVisitorId", visitorId);
        return query.getResultList();
    }
    
    @Override
    public Long createReservation(Reservation newReservation, Long visitorId) throws ReservationExistException, UnknownPersistenceException{
         try {
            Visitor visitor = em.find(Visitor.class, visitorId);
            newReservation.setVisitor(visitor);
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
    
    @Override
    public void removeGuest(Long visitorId) {
        Query query = em.createQuery("SELECT rr FROM ReservationRoom rr WHERE rr.reservation.visitor.visitorId = :inVisitorId");
        query.setParameter("inVisitorId", visitorId);
        List<ReservationRoom> list = (List<ReservationRoom>)query.getResultList();
        for(ReservationRoom rr : list){
            em.remove(rr);
            System.out.println("removing" + rr.getReservationRoomId());
        }
        em.flush();
        System.out.println("removing");
        Query query1 = em.createQuery("SELECT r FROM Reservation r WHERE r.visitor.visitorId = :inVisitorId");
        query1.setParameter("inVisitorId", visitorId);
        Reservation r = (Reservation)query1.getSingleResult();
        System.out.println(r.getReservationId());
        em.remove(r);
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
    
