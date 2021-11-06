/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import util.enumeration.RateTypeEnum;

/**
 *
 * @author harmo
 */
@Entity
public class RoomRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long RoomRateId;
    private String name;
    private RateTypeEnum rateType;
    private BigDecimal ratePerNight;
    private Date validityStartDate;
    private Date validityEndDate;
    
    //@ManyToOne
    //private RoomType roomType;

    public RoomRate(String name, RateTypeEnum rateType, BigDecimal ratePerNight, Date validityStartDate, Date validityEndDate) {
        this.name = name;
        this.rateType = rateType;
        this.ratePerNight = ratePerNight;
        this.validityStartDate = validityStartDate;
        this.validityEndDate = validityEndDate;
    }

    public RoomRate() {
    }
    

    public Long getRoomRateId() {
        return RoomRateId;
    }

    public void setRoomRateId(Long RoomRateId) {
        this.RoomRateId = RoomRateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getRoomRateId() != null ? getRoomRateId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the RoomRateId fields are not set
        if (!(object instanceof RoomRate)) {
            return false;
        }
        RoomRate other = (RoomRate) object;
        if ((this.getRoomRateId() == null && other.getRoomRateId() != null) || (this.getRoomRateId() != null && !this.RoomRateId.equals(other.RoomRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomType[ id=" + getRoomRateId() + " ]";
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
     * @return the rateType
     */
    public RateTypeEnum getRateType() {
        return rateType;
    }

    /**
     * @param rateType the rateType to set
     */
    public void setRateType(RateTypeEnum rateType) {
        this.rateType = rateType;
    }

    /**
     * @return the ratePerNight
     */
    public BigDecimal getRatePerNight() {
        return ratePerNight;
    }

    /**
     * @param ratePerNight the ratePerNight to set
     */
    public void setRatePerNight(BigDecimal ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    /**
     * @return the validityStartDate
     */
    public Date getValidityStartDate() {
        return validityStartDate;
    }

    /**
     * @param validityStartDate the validityStartDate to set
     */
    public void setValidityStartDate(Date validityStartDate) {
        this.validityStartDate = validityStartDate;
    }

    /**
     * @return the validityEndDate
     */
    public Date getValidityEndDate() {
        return validityEndDate;
    }

    /**
     * @param validityEndDate the validityEndDate to set
     */
    public void setValidityEndDate(Date validityEndDate) {
        this.validityEndDate = validityEndDate;
    }
    
}
