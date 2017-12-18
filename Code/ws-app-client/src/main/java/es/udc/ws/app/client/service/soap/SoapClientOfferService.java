package es.udc.ws.app.client.service.soap;

import es.udc.ws.app.client.service.ClientOfferService;
import es.udc.ws.app.client.service.soap.wsdl.*;
import es.udc.ws.app.dto.OfferDto;
import es.udc.ws.app.dto.ReserveDto;
import es.udc.ws.app.dto.ReserveDto.ReserveDtoState;
import es.udc.ws.app.exceptions.DuplicatedReserveException;
import es.udc.ws.app.exceptions.FullOfferException;
import es.udc.ws.app.exceptions.NotRemovableOfferException;
import es.udc.ws.app.exceptions.NotUpdatableOfferException;
import es.udc.ws.app.exceptions.OfferExpirationException;
import es.udc.ws.app.exceptions.ReserveExpirationException;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.util.List;
import javax.xml.ws.BindingProvider;

public class SoapClientOfferService implements ClientOfferService {

    private final static String ENDPOINT_ADDRESS_PARAMETER =
        "SoapClientOfferService.endpointAddress";
    private String endpointAddress;
    private OffersProvider offersProvider;
    
    public SoapClientOfferService() {
        init(getEndpointAddress());
    }

    private void init(String offersProviderURL) {
        OffersProviderService stockQuoteProviderService =
                new OffersProviderService();
        offersProvider = stockQuoteProviderService
                .getOffersProviderPort();
        ((BindingProvider) offersProvider).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                offersProviderURL);
    }

    @Override
    public Long addOffer(OfferDto offer)
            throws InputValidationException {
        try {
            return offersProvider.addOffer(OfferDtoToSoapOfferDtoConversor
                    .toSoapOfferDto(offer));
        } catch (SoapInputValidationException ex) {
            throw new InputValidationException(ex.getMessage());
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateOffer(OfferDto offer)
            throws InputValidationException, InstanceNotFoundException,
            NotUpdatableOfferException {
        try {
            offersProvider.updateOffer(OfferDtoToSoapOfferDtoConversor
                    .toSoapOfferDto(offer));
        } catch (SoapInputValidationException ex) {
            throw new InputValidationException(ex.getMessage());
        } catch (SoapInstanceNotFoundException ex) {
            throw new InstanceNotFoundException(
                    ex.getFaultInfo().getInstanceId(),
                    ex.getFaultInfo().getInstanceType());
        }catch (SoapNotUpdatableOfferException ex){
        	throw new NotUpdatableOfferException(ex.getMessage());
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void removeOffer(Long offerId)
            throws InstanceNotFoundException, NotRemovableOfferException {
        try {
			offersProvider.removeOffer(offerId);
		} catch (SoapInstanceNotFoundException ex) {
			throw new InstanceNotFoundException(
                    ex.getFaultInfo().getInstanceId(),
                    ex.getFaultInfo().getInstanceType());
		} catch (SoapNotRemovableOfferException e) {
			throw new NotRemovableOfferException(e.getMessage());
		}
    }

    @Override
    public OfferDto findOffer(long offerId) throws InstanceNotFoundException {
        try {
			return OfferDtoToSoapOfferDtoConversor.toOfferDto(
			            offersProvider.findOffer(offerId));
		} catch (SoapInstanceNotFoundException ex) {
            throw new InstanceNotFoundException(
                    ex.getFaultInfo().getInstanceId(),
                    ex.getFaultInfo().getInstanceType());
        }
    }
    

	@Override
	public long reserveOffer(long offerId, String userMail, String userCreditCard) 
			throws InstanceNotFoundException, FullOfferException, InputValidationException, 
			OfferExpirationException, DuplicatedReserveException {
        try {
			return offersProvider.reserveOffer(offerId, userMail, userCreditCard);
		} catch (SoapInstanceNotFoundException ex) {
            throw new InstanceNotFoundException(
                    ex.getFaultInfo().getInstanceId(),
                    ex.getFaultInfo().getInstanceType());
        } catch (SoapFullOfferException e) {
			throw new FullOfferException(e.getFaultInfo().getOfferId());
		} catch (SoapInputValidationException e) {
			throw new InputValidationException(e.getMessage());
		} catch (SoapOfferExpirationException e) {
			throw new OfferExpirationException(e.getFaultInfo().getOfferId());
		} catch (SoapDuplicatedReserveException e) {
			throw new DuplicatedReserveException(e.getFaultInfo().getUserMail(),
					e.getFaultInfo().getUserMail());
		}
	}

	@Override
	public List<OfferDto> findOffers(String keywords) {
		return OfferDtoToSoapOfferDtoConversor.toOfferDtos(offersProvider.
				findOffers(keywords));
	}
	
	@Override
	public void claimOffer(long reserveId) throws InstanceNotFoundException,
			ReserveExpirationException {
		try {
			offersProvider.claimOffer(reserveId);
		} catch (SoapInstanceNotFoundException e) {
			throw new InstanceNotFoundException(e.getFaultInfo().getInstanceId(), 
					e.getFaultInfo().getInstanceType());
		} catch (SoapReserveExpirationException e) {
			throw new ReserveExpirationException(e.getFaultInfo().getReserveId());
		}
		
	}
	
	@Override
	public List<ReserveDto> findReserves(Long offerId, ReserveDtoState status) 
			throws InstanceNotFoundException {
		try {
			return ReserveDtoToSoapReserveDtoConversor.
					toReserveDtos(offersProvider.findReserves(offerId, 
							ReserveDtoToSoapReserveDtoConversor.
							ReserveStateToSoapReserveState(status)));
		} catch (SoapInstanceNotFoundException e) {
			throw new InstanceNotFoundException(e.getFaultInfo().getInstanceId(), 
					e.getFaultInfo().getInstanceType());
		}
	}

	@Override
	public ReserveDto findReserve(long reserveId) throws InstanceNotFoundException {
		try {
			return ReserveDtoToSoapReserveDtoConversor.
					toReserveDto(offersProvider.findReserve(reserveId));
		} catch (SoapInstanceNotFoundException e) {
			throw new InstanceNotFoundException(e.getFaultInfo().getInstanceId(), 
					e.getFaultInfo().getInstanceType());
		}
	}
	
	private String getEndpointAddress() {

        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager.getParameter(
                ENDPOINT_ADDRESS_PARAMETER);
        }

        return endpointAddress;
    }



}
