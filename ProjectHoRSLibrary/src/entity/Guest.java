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

/**
 *
 * @author harmo
 */
@Entity
public class Guest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long GuestId;

    public Long getGuestId() {
        return GuestId;
    }

    public void setGuestId(Long GuestId) {
        this.GuestId = GuestId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (GuestId != null ? GuestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the GuestId fields are not set
        if (!(object instanceof Guest)) {
            return false;
        }
        Guest other = (Guest) object;
        if ((this.GuestId == null && other.GuestId != null) || (this.GuestId != null && !this.GuestId.equals(other.GuestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Guest[ id=" + GuestId + " ]";
    }
    
}