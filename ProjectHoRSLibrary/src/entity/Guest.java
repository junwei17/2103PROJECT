/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
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
    @Column(nullable = false, unique = true, length = 64)
    private String username;
    @Column(nullable = false, length = 64)
    private String password;
    
    @OneToMany(mappedBy="guest")
    private List<Reservation> reservations;

    public Guest() {
        super();
        //this.reservations = new ArrayList<>();
    }

    public Guest(String firstName, String lastName, String email, String address, String username, String password) {
        super(firstName, lastName, email, address);
        //this.reservations = new ArrayList<>();
        this.username = username;
        this.password = password;
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

    
    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    
}
