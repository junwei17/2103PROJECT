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
import util.enumeration.ExceptionTypeEnum;

/**
 *
 * @author wongj
 */
@Entity
public class Exceptions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exceptionId;
    private ExceptionTypeEnum exceptionTypeEnum;

    private Long reservationId;
    private Long reservationRoomId;
    private Long reservationRoomTypeId;

    public Exceptions( Long reservationId, Long reservationRoomId, Long reservationRoomTypeId, ExceptionTypeEnum exceptionTypeEnum) {
        this.exceptionTypeEnum = exceptionTypeEnum;
        this.reservationId = reservationId;
        this.reservationRoomId = reservationRoomId;
        this.reservationRoomTypeId = reservationRoomTypeId;
    }
    
    public Long getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(Long exceptionId) {
        this.exceptionId = exceptionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (exceptionId != null ? exceptionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the exceptionId fields are not set
        if (!(object instanceof Exceptions)) {
            return false;
        }
        Exceptions other = (Exceptions) object;
        if ((this.exceptionId == null && other.exceptionId != null) || (this.exceptionId != null && !this.exceptionId.equals(other.exceptionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Exceptions[ id=" + exceptionId + " ]";
    }

    /**
     * @return the exceptionTypeEnum
     */
    public ExceptionTypeEnum getExceptionTypeEnum() {
        return exceptionTypeEnum;
    }

    /**
     * @param exceptionTypeEnum the exceptionTypeEnum to set
     */
    public void setExceptionTypeEnum(ExceptionTypeEnum exceptionTypeEnum) {
        this.exceptionTypeEnum = exceptionTypeEnum;
    }
    
}
