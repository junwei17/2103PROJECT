/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author wongj
 */
@Entity
public class ReservationRoom implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationRoomId;
    private RoomType roomType;
    
    @JoinColumn(nullable = true)
    private Room room;

    public ReservationRoom() {
    }
    
    @ManyToOne
    private Reservation reservation;
    
    public Long getReservationRoomId() {
        return reservationRoomId;
    }

    public void setReservationRoomId(Long reservationRoomId) {
        this.reservationRoomId = reservationRoomId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationRoomId != null ? reservationRoomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationRoomId fields are not set
        if (!(object instanceof ReservationRoom)) {
            return false;
        }
        ReservationRoom other = (ReservationRoom) object;
        if ((this.reservationRoomId == null && other.reservationRoomId != null) || (this.reservationRoomId != null && !this.reservationRoomId.equals(other.reservationRoomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservationRoom[ id=" + reservationRoomId + " ]";
    }

    /**
     * @return the reservation
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * @param reservation the reservation to set
     */
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    /**
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @param room the room to set
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * @return the roomType
     */
    public RoomType getRoomType() {
        return roomType;
    }

    /**
     * @param roomType the roomType to set
     */
    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
    
}
