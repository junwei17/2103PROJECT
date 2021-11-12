/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomRateExistException;
import util.exception.RoomRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRoomRateException;

/**
 *
 * @author wongj
 */
@Local
public interface RoomRateSessionBeanLocal {

    public Long createRoomRate(RoomRate newRoomRate, Long roomTypeId) throws RoomRateExistException, UnknownPersistenceException;

    public RoomRate viewRoomRateDetails(Long roomRateId) throws RoomRateNotFoundException;

    public void updateRoomRate(RoomRate roomRate) throws RoomRateNotFoundException, UpdateRoomRateException;

    public List<RoomRate> viewAllRoomRates();

    public void deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException;

    public BigDecimal getRoomRate(Long roomTypeId, Date date);

    public BigDecimal getFee(Long roomTypeId, Date start, Date end);

    
}
