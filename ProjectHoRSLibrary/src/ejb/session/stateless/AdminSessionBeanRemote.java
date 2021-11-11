/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationRoom;
import entity.Room;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author harmo
 */
@Remote
public interface AdminSessionBeanRemote {
    public void allocateRoom(Date startDate);

    public Long searchRooms(Date startDate, Date endDate, Long roomTypeId);

    public List<Object[]> reservationsToFulfill(Date startDate);

    public void setRoom(ReservationRoom reservationRoom, Room room);
    
}
