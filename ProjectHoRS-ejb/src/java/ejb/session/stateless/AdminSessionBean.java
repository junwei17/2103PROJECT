/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.ReservationRoom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author harmo
 */
@Stateless
public class AdminSessionBean implements AdminSessionBeanRemote, AdminSessionBeanLocal {

    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager em;

    public void allocateRoom()
    {
        Date current = new Date();
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
        }
    }

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
