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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RoomNotFoundException;

/**
 *
 * @author harmo
 */
@Stateless
public class AdminSessionBean implements AdminSessionBeanRemote, AdminSessionBeanLocal {
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager em;

    @Override
    public void allocateRoom(Date startDate)
    {
        /*List<Object[]> fufillList = reservationsToFulfill(startDate);
        if(!fufillList.isEmpty()) {
            for(Object[] obj : fufillList){
                System.out.println("obj is " + (Date)obj[0] + (Date)obj[1] + ((ReservationRoom)obj[2]).getRoomType().getRoomTypeId());
                Long result = searchRooms((Date)obj[0], (Date)obj[1], ((ReservationRoom)obj[2]).getRoomType().getRoomTypeId());
                System.out.println("result here is " + result);
                try {
                if(result == -1) {
                    //create new Exception
                    result = searchRooms((Date)obj[0], (Date)obj[1], ((ReservationRoom)obj[2]).getRoomType().getRoomTypeId() + 1);
                    System.out.println("result there is " + result);
                    if(result == -1){
                        //create new Exception
                    } else {
                        System.out.println(roomSessionBeanLocal.viewRoomDetails(result).getRoomId() + " here");
                        ((ReservationRoom)obj[2]).setRoom(roomSessionBeanLocal.viewRoomDetails(result));
                    }
                } else {
                    System.out.println("herereee");
                    System.out.println(roomSessionBeanLocal.viewRoomDetails(result).getRoomId() + " there");
                    ((ReservationRoom)obj[2]).setRoom(roomSessionBeanLocal.viewRoomDetails(result));
                }
                } catch (RoomNotFoundException ex) {
                    System.out.println("Room not Found!");
                }
            }
        }
        
        /*Date current = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Query query = em.createQuery("SELECT r FROM Reservation r");
        List<Reservation> reservations = query.getResultList();
        
        for (Reservation r: reservations)
        {
            if (fmt.format(r.getEndDate()).equals(fmt.format(current)))
            {
                for (ReservationRoom rr: r.getReservationRooms())
                {
                    rr.getRoom().setReservationRoom(null);
                    rr.setRoom(null);
                }
            }
        }
        
        for (Reservation r: reservations)
        {
            if (fmt.format(r.getStartDate()).equals(fmt.format(current)))
            {
                
            }
        }*/
    }

    @Override
    public List<Object> searchRooms(Date startDate, Date endDate, Long roomTypeId) {
        Query query = em.createQuery("SELECT r.roomId FROM Room r WHERE r.roomType.roomTypeId = :inRoomTypeId AND r.roomId NOT IN (SELECT rr.room.roomId FROM ReservationRoom rr WHERE rr.reservation.startDate < :inEndDate AND rr.reservation.endDate > :inStartDate)");
        query.setParameter("inEndDate", endDate);
        query.setParameter("inStartDate", startDate);
        query.setParameter("inRoomTypeId", roomTypeId);
        return query.getResultList();
    }
    
    @Override
    public List<Object[]> reservationsToFulfill(Date startDate) {
        Query query = em.createQuery("SELECT r.startDate, r.endDate, rr FROM ReservationRoom rr JOIN Reservation r WHERE r.startDate = :inStartDate AND rr.reservation = r AND rr.room = null");
        query.setParameter("inStartDate", startDate);
        return query.getResultList();
    }
    
    @Override
    public void setRoom(Long reservationRoomId, Long roomId) {
        ReservationRoom reservationRoom = em.find(ReservationRoom.class, reservationRoomId);
        Room room = em.find(Room.class, roomId);
        reservationRoom.setRoom(room);
        em.persist(reservationRoom);
    }
    
    @Override
    public List<Exceptions> retrieveAllEmployees() {
        Query query = entityManager.createQuery("SELECT e FROM Employee e");
        return query.getResultList();
    }
}
