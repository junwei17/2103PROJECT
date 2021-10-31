/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DeleteRoomTypeException;
import util.exception.RoomExistException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRoomTypeException;

/**
 *
 * @author wongj
 */
@Remote
public interface RoomTypeSessionBeanRemote {
    public Long createRoomType(RoomType roomType) throws RoomExistException, UnknownPersistenceException;
    public RoomType viewRoomTypeDetails(Long roomName) throws RoomTypeNotFoundException;
    public void updateRoomType(RoomType roomType) throws RoomTypeNotFoundException, UpdateRoomTypeException;
    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException, DeleteRoomTypeException;

    public List<RoomType> viewAllRoomTypes();
}
