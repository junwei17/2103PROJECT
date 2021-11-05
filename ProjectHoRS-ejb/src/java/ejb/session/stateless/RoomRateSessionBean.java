/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.RoomExistException;
import util.exception.RoomRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRoomException;
import util.exception.UpdateRoomRateException;

/**
 *
 * @author wongj
 */
@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager em;

    public RoomRateSessionBean() {
    }
    
    @Override
    public Long createRoomRate(RoomRate newRoomRate) throws RoomRateExistException, UnknownPersistenceException {
        try {
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
    public void updateRoom(RoomRate roomRate) throws RoomRateNotFoundException, UpdateRoomRateException {
        if(roomRate != null && roomRate.getRoomId() != null) {
            RoomRate toBeUpdated = viewRoomDetails(roomRate.getRoomId());
            if(toBeUpdated.getRoomNo().equals(room.getRoomNo())) {
                toBeUpdated.setRoomType(room.getRoomType());
                toBeUpdated.setStatus(room.getStatus());      
            } else {
                throw new UpdateRateRoomException("Room Number of Room record to be updated does not match the existing record");
            }
        } else {
            throw new RoomRateNotFoundException("Room not found or Room ID not provided!");
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
