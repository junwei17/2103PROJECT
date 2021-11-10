/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author harmo
 */
@Entity
public class Guest extends Visitor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long GuestId;
    @Column(nullable = false, unique = true, length = 64)
    @NotNull
    @Size(min = 6, max = 64)
    private String username;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 6, max = 64)
    private String password;
    
    @OneToMany(mappedBy="guest")
    private List<Reservation> reservations;

    public Guest() {
    }

    public Guest(String firstName, String lastName, String email, String address, String username, String password) {
        super(firstName, lastName, email, address);
        this.username = username;
        this.password = password;
    }
    
    

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

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the reservations
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * @param reservations the reservations to set
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    
}
