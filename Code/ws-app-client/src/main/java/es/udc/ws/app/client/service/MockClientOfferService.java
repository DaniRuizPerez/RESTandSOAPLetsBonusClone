package es.udc.ws.app.client.service;


import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.client.service.soap.wsdl.SoapInstanceNotFoundException;
import es.udc.ws.app.client.service.soap.wsdl.SoapNotRemovableOfferException;
import es.udc.ws.app.dto.OfferDto;
import es.udc.ws.app.dto.ReserveDto;
import es.udc.ws.app.dto.ReserveDto.ReserveDtoState;
import es.udc.ws.app.exceptions.FullOfferException;
import es.udc.ws.app.exceptions.NotRemovableOfferException;
import es.udc.ws.app.exceptions.NotUpdatableOfferException;
import es.udc.ws.app.exceptions.OfferExpirationException;
import es.udc.ws.app.exceptions.ReserveExpirationException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class MockClientOfferService implements ClientOfferService {

	@Override
	public Long addOffer(OfferDto offer) throws InputValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateOffer(OfferDto offer) throws InputValidationException,
			InstanceNotFoundException, NotUpdatableOfferException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeOffer(Long offerId) throws InstanceNotFoundException,
			NotRemovableOfferException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<OfferDto> findOffers(String keywords) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OfferDto findOffer(long offerId) throws InstanceNotFoundException,
			InstanceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReserveDto findReserve(long reserveId)
			throws InstanceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long reserveOffer(long offerId, String userMail,
			String userCreditCard) throws InstanceNotFoundException,
			FullOfferException, InputValidationException,
			OfferExpirationException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void claimOffer(long reserveId) throws InstanceNotFoundException,
			ReserveExpirationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ReserveDto> findReserves(Long offerId, ReserveDtoState status)
			throws InstanceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
