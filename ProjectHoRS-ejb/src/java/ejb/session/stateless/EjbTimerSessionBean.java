/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author harmo
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager em;
    
    

    @Schedule(hour = "2", minute = "0", second = "0", info = "allocateRoomTimer")    
    // For testing purpose, we are allowing the timer to trigger every 5 seconds instead of every 5 minutes
    // To trigger the timer once every 5 minutes instead, use the following the @Schedule annotation
    // @Schedule(hour = "*", minute = "*/5", info = "productEntityReorderQuantityCheckTimer")
    public void allocateRoomTimer()
    {
        Date current = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        Query query = em.createQuery("SELECT r FROM Reservation r");
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("********** EjbTimerSessionBean.productEntityReorderQuantityCheckTimer(): Timeout at " + timeStamp);
        
    }
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
