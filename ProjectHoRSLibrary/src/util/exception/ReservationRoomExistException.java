/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author wongj
 */
public class ReservationRoomExistException extends Exception{

    public ReservationRoomExistException() {
    }

    public ReservationRoomExistException(String string) {
        super(string);
    }
    
}
