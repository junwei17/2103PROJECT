/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.DeleteRoomTypeException;
import util.exception.RoomExistException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRoomTypeException;

/**
 *
 * @author wongj
 */
@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanRemote, RoomTypeSessionBeanLocal {

    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager em;

    @Override
    public Long createRoomType(RoomType roomType) throws RoomExistException, UnknownPersistenceException {
        try {
            em.persist(roomType);
            em.flush();
        
            return roomType.getRoomTypeId();
        } catch (PersistenceException ex) {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new RoomExistException();
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
    public RoomType retreieveRoomTypeByName(String name) throws RoomTypeNotFoundException
    {
        Query query = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = :inRoomTypeName");
        query.setParameter("inRoomTypeName", name);
        
        try {
            return(RoomType)query.getSingleResult();
        }catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomTypeNotFoundException("The Room Type for " + name + " does not exists!");
        }
        
    }
    
    @Override
    public RoomType viewRoomTypeDetails(Long roomTypeId) throws RoomTypeNotFoundException {
        Query query = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.roomTypeId = :inRoomTypeId");
        query.setParameter("inRoomTypeId", roomTypeId);
        
        try {
            return(RoomType)query.getSingleResult();
        }catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomTypeNotFoundException("The Room Type for " + roomTypeId + " does not exists!");
        }
        
    }

    @Override
    public void updateRoomType(RoomType roomType) throws RoomTypeNotFoundException, UpdateRoomTypeException {
        if(roomType != null && roomType.getRoomTypeId() != null) {
            RoomType toBeUpdated = viewRoomTypeDetails(roomType.getRoomTypeId());
            if(toBeUpdated.getName().equals(roomType.getName())) {
                toBeUpdated.setDescription(roomType.getDescription());
                toBeUpdated.setSize(roomType.getSize());
                toBeUpdated.setCapacity(roomType.getCapacity());
                toBeUpdated.setAmenitiesEnum(roomType.getAmenitiesEnum());
                
            } else {
                throw new UpdateRoomTypeException("Name of Room Type record to be updated does not match the existing record");
            }
        } else {
            throw new RoomTypeNotFoundException("Room Type not found or Room Type ID not provided!");
        }
    }
    
    @Override
    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException, DeleteRoomTypeException {
        RoomType toBeRemoved = viewRoomTypeDetails(roomTypeId);
        if(toBeRemoved.getRooms().isEmpty()) {
            em.remove(toBeRemoved);
        } else {
            throw new DeleteRoomTypeException("Room Type ID " + roomTypeId + " is associated with other room records and is currently in used, thus it cannot be deleted!");
        }
    }
    
    @Override
    public List<RoomType> viewAllRoomTypes() {
        Query query = em.createQuery("SELECT rt FROM RoomType rt");
        return query.getResultList();
    }
}
