package es.udc.ws.app.model.reserve;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.app.model.reserve.Reserve.ReserveState;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlReserveDao {

	
	public Reserve create(Connection connection, Reserve reserve);

    public Reserve findByReserveId(Connection connection, Long reserveId)
            throws InstanceNotFoundException;
    
    public Reserve findByOfferIdAndUser (Connection connection, Long offerId, 
    		String userMail) throws InstanceNotFoundException;

    public void update(Connection connection, Reserve reserve)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long reserveId)
            throws InstanceNotFoundException;

	List<Reserve> find(Connection connection, Long offerId,
			ReserveState reserveStatus) throws InstanceNotFoundException;

}
