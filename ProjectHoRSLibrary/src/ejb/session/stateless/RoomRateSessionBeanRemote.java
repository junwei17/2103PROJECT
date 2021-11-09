/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RoomRateExistException;
import util.exception.RoomRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRoomRateException;

/**
 *
 * @author wongj
 */
@Remote
public interface RoomRateSessionBeanRemote {
    public Long createRoomRate(RoomRate newRoomRate) throws RoomRateExistException, UnknownPersistenceException;

    public RoomRate viewRoomRateDetails(Long roomRateId) throws RoomRateNotFoundException;

    public void updateRoomRate(RoomRate roomRate) throws RoomRateNotFoundException, UpdateRoomRateException;

    public List<RoomRate> viewAllRoomRates();
}
