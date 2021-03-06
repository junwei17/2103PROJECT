/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import util.enumeration.AmenitiesEnum;

/**
 *
 * @author harmo
 */
@Entity
public class RoomType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;
    private String name;
    private String bed;
    private String description;
    private Integer size;
    private Integer capacity;
    private AmenitiesEnum amenitiesEnum;
    private Long nextHigherRoomType;
    
    @OneToMany(mappedBy ="roomType")
    private List<Room> rooms;
    
    @OneToMany(mappedBy ="roomType", fetch = FetchType.EAGER)
    private List<RoomRate> roomRates;
    
    
    //@OneToMany(mappedBy ="RoomType")
    //private List<Reservation> reservations;

    public RoomType(String name) {
        this();
        this.name = name;
    }

    public RoomType(String name, Long nextHigherRoomType) {
        this();
        this.name = name;
        this.nextHigherRoomType = nextHigherRoomType;
    }

    public RoomType(String name, String description, String bed, Integer size, Integer capacity, Long nextHigherRoomType) {
        this();
        this.name = name;
        this.bed = bed;
        this.description = description;
        this.size = size;
        this.capacity = capacity;
        this.nextHigherRoomType = nextHigherRoomType;
    }
        public RoomType(String name, String description, String bed, Integer size, Integer capacity) {
        this();
        this.name = name;
        this.bed = bed;
        this.description = description;
        this.size = size;
        this.capacity = capacity;
    }
    
    
    

    public RoomType() {
        this.rooms = new ArrayList<>();
        this.roomRates = new ArrayList<>();
        
    }
    
    

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeId != null ? roomTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeId fields are not set
        if (!(object instanceof RoomType)) {
            return false;
        }
        RoomType other = (RoomType) object;
        if ((this.roomTypeId == null && other.roomTypeId != null) || (this.roomTypeId != null && !this.roomTypeId.equals(other.roomTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomType[ id=" + roomTypeId + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * @return the capacity
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }


    /**
     * @return the rooms
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * @param rooms the rooms to set
     */
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    /**
     * @return the bed
     */
    public String getBed() {
        return bed;
    }

    /**
     * @param bed the bed to set
     */
    public void setBed(String bed) {
        this.bed = bed;
    }

    /**
     * @return the amenitiesEnum
     */
    public AmenitiesEnum getAmenitiesEnum() {
        return amenitiesEnum;
    }

   
    public void setAmenitiesEnum(AmenitiesEnum amenitiesEnum) {
        this.amenitiesEnum = amenitiesEnum;
    }
    /**
     * @return the roomRates
     */
    public List<RoomRate> getRoomRates() {
        return roomRates;
    }

    /**
     * @param roomRates the roomRates to set
     */
    public void setRoomRates(List<RoomRate> roomRates) {
        this.roomRates = roomRates;
    }

    /**
     * @return the nextHigherRoomType
     */
    public Long getNextHigherRoomType() {
        return nextHigherRoomType;
    }

    /**
     * @param nextHigherRoomType the nextHigherRoomType to set
     */
    public void setNextHigherRoomType(Long nextHigherRoomType) {
        this.nextHigherRoomType = nextHigherRoomType;
    }

    
}
