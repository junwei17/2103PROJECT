/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author harmo
 */
@Stateless
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {

    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager entityManager;
    

    public RoomSessionBean() {
    }
    
    @PostConstruct
    public void postConstruct() 
    {
    }
    
    @PreDestroy
    public void preDestroy()
    {
    }
    
    
    @Override
    public Long createRoom(Room room) {
        entityManager.persist(room);
        entityManager.flush();
        
        return room.getRoomId();
    }
    
    @Override
    public Long updateRoomStatus(Room room, boolean status)
    {
        room.setStatus(status);
        
        return room.getRoomId();
    }
    
    @Override
    public List<Room> getRooms() 
    {
        Query query = entityManager.createQuery("SELECT r FROM Room r");
        
        return query.getResultList();
    }
        
    
   
}
