/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author wongj
 */
@Stateless
public class DataInitSessionBean implements DataInitSessionBeanRemote, DataInitSessionBeanLocal {

    @PersistenceContext(unitName = "ProjectHoRS-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct(){
        if(em.find(Employee.class, 1l) == null) {
            em.persist(new Employee("Bob"));
            em.persist(new Employee("Alice"));
            em.persist(new Employee("Claire"));
        }
    }

}
