/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteRoomException;
import util.exception.RoomExistException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRoomException;

/**
 *
 * @author harmo
 */
@Local
public interface RoomSessionBeanLocal {
    public Long createRoom(Room newRoom, Long roomTypeId) throws RoomExistException, UnknownPersistenceException;
            
    public List<Room> viewAllRooms();

    public void deleteRoom(Long roomId) throws RoomNotFoundException, DeleteRoomException;

    public void updateRoom(Room room) throws RoomNotFoundException, UpdateRoomException;

    public Room viewRoomDetails(Long roomId) throws RoomNotFoundException;

    public Room retreieveRoom(Long roomName) throws RoomNotFoundException;

}
