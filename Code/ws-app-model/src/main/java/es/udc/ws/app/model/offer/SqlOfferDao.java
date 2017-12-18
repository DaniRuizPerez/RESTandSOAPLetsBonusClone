package es.udc.ws.app.model.offer;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.model.offer.Offer.OfferState;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlOfferDao {

    public Offer create(Connection connection, Offer offer);

    public Offer find(Connection connection, Long offerId)
            throws InstanceNotFoundException;

    public void update(Connection connection, Offer offer)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long offerId)
            throws InstanceNotFoundException;
    
	List<Offer> findByKeys(Connection connection, String keywords,
			OfferState status, Calendar date);
}
