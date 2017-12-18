package es.udc.ws.app.model.offerservice;

import es.udc.ws.app.model.offer.Offer;
import es.udc.ws.app.model.offer.Offer.OfferState;
import es.udc.ws.app.model.reserve.Reserve;
import es.udc.ws.app.model.reserve.Reserve.ReserveState;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.exceptions.DuplicatedReserveException;
import es.udc.ws.app.exceptions.FullOfferException;
import es.udc.ws.app.exceptions.NotRemovableOfferException;
import es.udc.ws.app.exceptions.NotUpdatableOfferException;
import es.udc.ws.app.exceptions.OfferExpirationException;
import es.udc.ws.app.exceptions.ReserveExpirationException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface OfferService {

    public Offer addOffer(Offer Offer) throws InputValidationException;

    public void updateOffer(Offer Offer) throws InputValidationException,
            InstanceNotFoundException, NotUpdatableOfferException;

    public void removeOffer(Long OfferId) throws InstanceNotFoundException, 
    		NotRemovableOfferException;

    public Offer findOffer(Long OfferId) throws InstanceNotFoundException;

    List<Offer> findOffers(String keywords, OfferState status, Calendar date);
    
    public Long reserveOffer(Long offerId, String userMail, 
    		String userCreditCard) throws InstanceNotFoundException, 
    		InputValidationException, 
    		OfferExpirationException, FullOfferException, DuplicatedReserveException;

    public List<Reserve> findReserves(Long offerId, 
    		ReserveState status) throws InstanceNotFoundException;
    
    public Reserve findReserve(Long reserveId) throws InstanceNotFoundException;;

    public void claimOffer(Long reserveId) throws InstanceNotFoundException, 
    		ReserveExpirationException;

	

}
