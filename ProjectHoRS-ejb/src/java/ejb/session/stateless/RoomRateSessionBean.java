/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.RoomExistException;
import util.exception.RoomRateExistException;
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
    public Long createRoomRate(RoomRate newRoomRate, Long roomTypeId) throws RoomRateExistException, UnknownPersistenceException {
        try {
            RoomType roomType = em.find(RoomType.class, roomTypeId);
            newRoomRate.setRoomType(roomType);
            roomType.getRoomRates().add(newRoomRate);
            em.persist(newRoomRate);
            em.flush();
        
            return newRoomRate.getRoomRateId();
        } catch (PersistenceException ex) {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new RoomRateExistException("here");
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
    public RoomRate viewRoomRateDetails(Long roomRateId) throws RoomRateNotFoundException {
        Query query = em.createQuery("SELECT rr FROM RoomRate rr WHERE rr.roomRateId = :inRoomRateId");
        query.setParameter("inRoomRateId", roomRateId);
        
        try {
            return(RoomRate)query.getSingleResult();
        }catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomRateNotFoundException("The Room Type for " + roomRateId + " does not exists!");
        }
        
    }

    @Override
    public void updateRoomRate(RoomRate roomRate) throws RoomRateNotFoundException, UpdateRoomRateException {
        if(roomRate != null && roomRate.getRoomRateId() != null) {
            RoomRate toBeUpdated = viewRoomRateDetails(roomRate.getRoomRateId());
            if(toBeUpdated.getName().equals(roomRate.getName())) {
                toBeUpdated.setRoomType(roomRate.getRoomType());
                toBeUpdated.setRateType(roomRate.getRateType());
                toBeUpdated.setValidityStartDate(roomRate.getValidityStartDate());
                toBeUpdated.setValidityEndDate(roomRate.getValidityEndDate());
                toBeUpdated.setRatePerNight(roomRate.getRatePerNight());
                toBeUpdated.setDisabled(roomRate.isDisabled());
            } else {
                throw new UpdateRoomRateException("Room Rate name of Room Rate record to be updated does not match the existing record");
            }
        } else {
            throw new RoomRateNotFoundException("Room Rate not found!");
        }
    }
    
    /*@Override
    public void deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException, DeleteRoomRateException {
        RoomRate toBeRemoved = viewRoomRateDetails(roomRateId);
        if(!toBeRemoved.getStatus()) {
            em.remove(toBeRemoved);
        } else {
            throw new DeleteRoomException("Room ID " + roomId + " is currently in used, thus it cannot be deleted!");
        }
    }*/

    
    @Override
    public List<RoomRate> viewAllRoomRates() {
        Query query = em.createQuery("SELECT rr FROM RoomRate rr");
        return query.getResultList();
    }
    
    @Override
    public void deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException{
        RoomRate toBeRemoved = viewRoomRateDetails(roomRateId);
        em.remove(toBeRemoved);
    }

    
}
