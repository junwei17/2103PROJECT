/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Local;
import util.exception.PartnerExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wongj
 */
@Local
public interface PartnerSessionBeanLocal {
    public Long createNewPartner(Partner newPartner) throws UnknownPersistenceException, PartnerExistException;

    public List<Partner> retrieveAllPartners();
}
