/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 *
 * @author harmo
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ReservationId;
    /*@Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull*/
    private Date startDate;
    /*@Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull*/
    private Date endDate;
    /*@Column(nullable = false, precision = 11, scale = 2)
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)*/
    private BigDecimal fee;
    /*@Column(nullable = false)
    @NotNull*/
    //private Integer numberOfRooms;
    
    
    @OneToMany(mappedBy = "reservation")
    private List<ReservationRoom> reservationRooms;
    
    @ManyToOne
    private Visitor visitor;
    
    @ManyToOne
    private Guest guest;
    
    @ManyToOne
    private Partner partner;
    
    public Reservation() {
        this.reservationRooms = new ArrayList<>();
    }

    public Reservation(Date startDate, Date endDate, BigDecimal fee) {
        this();
        this.startDate = startDate;
        this.endDate = endDate;
        this.fee = fee;
       // this.rooms = rooms;
    }
   
    

    public Long getReservationId() {
        return ReservationId;
    }

    public void setReservationId(Long ReservationId) {
        this.ReservationId = ReservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ReservationId != null ? ReservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the ReservationId fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.ReservationId == null && other.ReservationId != null) || (this.ReservationId != null && !this.ReservationId.equals(other.ReservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + ReservationId + " ]";
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the fee
     */
    public BigDecimal getFee() {
        return fee;
    }

   
    /**
     * @param fee the fee to set
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }


    /**
     * @return the reservationRooms
     */
    public List<ReservationRoom> getReservationRooms() {
        return reservationRooms;
    }

    /**
     * @param reservationRooms the reservationRooms to set
     */
    public void setReservationRooms(List<ReservationRoom> reservationRooms) {
        this.reservationRooms = reservationRooms;
    }

    /**
     * @return the guest
     */
    public Visitor getVisitor() {
        return visitor;
    }

    /**
     * @param guest the guest to set
     */
    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    /**
     * @return the partner
     */
    public Partner getPartner() {
        return partner;
    }

    /**
     * @param partner the partner to set
     */
    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    /**
     * @return the guest
     */
    public Guest getGuest() {
        return guest;
    }

    /**
     * @param guest the guest to set
     */
    public void setGuest(Guest guest) {
        this.guest = guest;
    }

  

    /**
     * @return the rooms
     */
    /*public List<Room> getRooms() {
        return rooms;
    }

    /**
     * @param rooms the rooms to set
     */
    /*public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }*/
    
}
