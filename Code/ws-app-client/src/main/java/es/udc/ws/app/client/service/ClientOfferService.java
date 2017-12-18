package es.udc.ws.app.client.service;
import java.util.List;

import es.udc.ws.app.dto.OfferDto;
import es.udc.ws.app.dto.ReserveDto;
import es.udc.ws.app.dto.ReserveDto.ReserveDtoState;
import es.udc.ws.app.exceptions.DuplicatedReserveException;
import es.udc.ws.app.exceptions.FullOfferException;
import es.udc.ws.app.exceptions.NotRemovableOfferException;
import es.udc.ws.app.exceptions.NotUpdatableOfferException;
import es.udc.ws.app.exceptions.OfferExpirationException;
import es.udc.ws.app.exceptions.ReserveExpirationException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface ClientOfferService {

    public Long addOffer(OfferDto offer)
            throws InputValidationException;

    public void updateOffer(OfferDto offer)
            throws InputValidationException, InstanceNotFoundException, NotUpdatableOfferException;

    public void removeOffer(Long offerId) throws InstanceNotFoundException, 
    		NotRemovableOfferException;

    public List<OfferDto> findOffers(String keywords);

	public OfferDto findOffer(long offerId) throws InstanceNotFoundException, 
			InstanceNotFoundException;
	
	public List<ReserveDto> findReserves(Long offerId, ReserveDtoState status) 
			throws InstanceNotFoundException;

	public ReserveDto findReserve(long reserveId) throws InstanceNotFoundException;

	public long reserveOffer(long offerId, String userMail, String userCreditCard) 
			throws InstanceNotFoundException, FullOfferException,
			InputValidationException, OfferExpirationException, DuplicatedReserveException;
	
	public void claimOffer(long reserveId) throws InstanceNotFoundException, 
			ReserveExpirationException;

    /*public String getOfferUrl(Long saleId)
            throws InstanceNotFoundException, SaleExpirationException;*/

}
