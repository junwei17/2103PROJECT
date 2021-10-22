/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author harmo
 */
@Entity
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private Integer roomNo;
    private Integer sequenceNo; 
    private boolean status;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Reservation reservation;

    @OneToOne
    private RoomType roomType;
    
    
    public Room() {
    }

    public Room(RoomType roomType, Integer roomNo, Integer sequenceNo, Reservation reservation, boolean status) {
        this.roomType = roomType;
        this.roomNo = roomNo;
        this.sequenceNo = sequenceNo;
        this.reservation = reservation;
        this.status = status;
    }
    
    
    
    

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomId fields are not set
        if (!(object instanceof Room)) {
            return false;
        }
        Room other = (Room) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Room[ id=" + roomId + " ]";
    }

    /**
     * @return the roomNo
     */
    public Integer getRoomNo() {
        return roomNo;
    }

    /**
     * @param roomNo the roomNo to set
     */
    public void setRoomNo(Integer roomNo) {
        this.roomNo = roomNo;
    }

    /**
     * @return the sequenceNo
     */
    public Integer getSequenceNo() {
        return sequenceNo;
    }

    /**
     * @param sequenceNo the sequenceNo to set
     */
    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    /**
     * @return the status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }
    
}
