/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.DeleteRoomException;
import util.exception.RoomExistException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRoomException;

/**
 *
 * @author harmo
 */
@Stateless
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal1;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager em;
    
    
    

    public RoomSessionBean() {
    }
    
    @Override
    public Long createRoom(Room newRoom, Long roomTypeId) throws RoomExistException, UnknownPersistenceException {
        try {
            RoomType roomType = em.find(RoomType.class, roomTypeId);
            newRoom.setRoomType(roomType);
            roomType.getRooms().add(newRoom);
            em.persist(newRoom);
            em.flush();
        
            return newRoom.getRoomId();
        } catch (PersistenceException ex) {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new RoomExistException("here");
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
    public Room viewRoomDetails(Long roomId) throws RoomNotFoundException {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomId = :inRoomId");
        query.setParameter("inRoomId", roomId);
        
        try {
            return(Room)query.getSingleResult();
        }catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomNotFoundException("The Room Type for " + roomId + " does not exists!");
        }
        
    }

    @Override
    public void updateRoom(Room room) throws RoomNotFoundException, UpdateRoomException {
        if(room != null && room.getRoomId() != null) {
            Room toBeUpdated = viewRoomDetails(room.getRoomId());
            if(toBeUpdated.getRoomNo().equals(room.getRoomNo())) {
                toBeUpdated.setRoomType(room.getRoomType());
                toBeUpdated.setStatus(room.getStatus());      
            } else {
                throw new UpdateRoomException("Room Number of Room record to be updated does not match the existing record");
            }
        } else {
            throw new RoomNotFoundException("Room not found or Room ID not provided!");
        }
    }
    
    @Override
    public void deleteRoom(Long roomId) throws RoomNotFoundException, DeleteRoomException {
        Room toBeRemoved = viewRoomDetails(roomId);
        if(!toBeRemoved.getStatus()) {
            em.remove(toBeRemoved);
        } else {
            throw new DeleteRoomException("Room ID " + roomId + " is currently in used, thus it cannot be deleted!");
        }
    }
    
    @Override
    public List<Room> viewAllRooms() {
        Query query = em.createQuery("SELECT r FROM Room r");
        return query.getResultList();
    }
    
}
