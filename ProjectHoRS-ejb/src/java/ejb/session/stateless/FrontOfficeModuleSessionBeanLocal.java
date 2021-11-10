/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.ReservationRoom;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.ReservationExistException;
import util.exception.ReservationRoomExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wongj
 */
@Local
public interface FrontOfficeModuleSessionBeanLocal {

    public List<Object[]> searchRooms(Date startDate, Date endDate);

    public Long createReservation(Reservation newReservation) throws ReservationExistException, UnknownPersistenceException;

    public Long reserveRoom(ReservationRoom newReservationRoom) throws ReservationRoomExistException, UnknownPersistenceException;
    
}
