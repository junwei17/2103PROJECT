/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import java.util.List;
import javax.ejb.Local;
import util.exception.GuestNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author harmo
 */
@Local
public interface ReservationSessionBeanLocal {

    public List<Reservation> viewAllReservations(Long guestId) throws GuestNotFoundException, ReservationNotFoundException;

    public Reservation viewReservationDetails(Long reservationId) throws ReservationNotFoundException;
    
}
