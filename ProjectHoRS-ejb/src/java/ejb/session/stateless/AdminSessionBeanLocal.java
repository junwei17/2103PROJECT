/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Exceptions;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author harmo
 */
@Local
public interface AdminSessionBeanLocal {

    public void allocateRoom(Date startDate);

    public List<Object> searchRooms(Date startDate, Date endDate, Long roomTypeId);

    public List<Object[]> reservationsToFulfill(Date startDate);

    public void setRoom(Long reservationRoomId, Long roomId);
    
}
