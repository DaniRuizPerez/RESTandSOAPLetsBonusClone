package es.udc.ws.app.soapservice;

import es.udc.ws.app.dto.OfferDto;
import es.udc.ws.app.dto.ReserveDto;
import es.udc.ws.app.dto.ReserveDto.ReserveDtoState;
import es.udc.ws.app.exceptions.DuplicatedReserveException;
import es.udc.ws.app.exceptions.FullOfferException;
import es.udc.ws.app.exceptions.NotRemovableOfferException;
import es.udc.ws.app.exceptions.NotUpdatableOfferException;
import es.udc.ws.app.exceptions.OfferExpirationException;
import es.udc.ws.app.exceptions.ReserveExpirationException;
import es.udc.ws.app.model.offer.Offer;
import es.udc.ws.app.model.offerservice.OfferServiceFactory;
import es.udc.ws.app.model.reserve.Reserve;
import es.udc.ws.app.util.OfferToOfferDtoConversor;
import es.udc.ws.app.util.ReserveToReserveDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.Calendar;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(
    name="OffersProvider",
    serviceName="OffersProviderService",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapOfferService {
	
    @WebMethod(
        operationName="addOffer"
    )
    public Long addOffer(@WebParam(name="offerDto") OfferDto offerDto)
            throws SoapInputValidationException {
        Offer offer = OfferToOfferDtoConversor.toOffer(offerDto);
        try {
            return OfferServiceFactory.getService().addOffer(offer).getOfferId();
        } catch (InputValidationException ex) {
            throw new SoapInputValidationException(ex.getMessage());
        }
    }
    
    @WebMethod(
        operationName="updateOffer"
    )
    public void updateOffer(@WebParam(name="offerDto") OfferDto offerDto)
            throws SoapInputValidationException, SoapInstanceNotFoundException, 
            SoapNotUpdatableOfferException {
        Offer offer = OfferToOfferDtoConversor.toOffer(offerDto);
        try {
            OfferServiceFactory.getService().updateOffer(offer);
        } catch (InputValidationException ex) {
            throw new SoapInputValidationException(ex.getMessage());
        } catch (NotUpdatableOfferException ex) {
        	throw new SoapNotUpdatableOfferException(ex.getMessage());
        } catch (InstanceNotFoundException ex) {
            throw new SoapInstanceNotFoundException(
                    new SoapInstanceNotFoundExceptionInfo(ex.getInstanceId(),
                        ex.getInstanceType()));
        }
        
    }

    @WebMethod(
        operationName="removeOffer"
    )
    public void removeOffer(@WebParam(name="offerId") Long offerId)
            throws SoapInstanceNotFoundException, SoapNotRemovableOfferException {
        try {
            OfferServiceFactory.getService().removeOffer(offerId);
        } catch (InstanceNotFoundException ex) {
            throw new SoapInstanceNotFoundException(
                    new SoapInstanceNotFoundExceptionInfo(
                    ex.getInstanceId(), ex.getInstanceType()));
        } catch (NotRemovableOfferException ex) {
        	throw new SoapNotRemovableOfferException(ex.getMessage());
		}
    }

    @WebMethod(
        operationName="findOffer"
    )
    public OfferDto findOffer(
            @WebParam(name="offerId") long offerId) throws SoapInstanceNotFoundException {
        Offer offer;
		try {
			offer = OfferServiceFactory.getService().findOffer(offerId);
			return OfferToOfferDtoConversor.toOfferDto(offer);
		} catch (InstanceNotFoundException ex) {
			throw new SoapInstanceNotFoundException(
                    new SoapInstanceNotFoundExceptionInfo(
                    ex.getInstanceId(), ex.getInstanceType()));
		}
        
    }

    @WebMethod(
            operationName="findOffers"
        )
        public List<OfferDto> findOffers(
                @WebParam(name="keywords") String keywords) {
            List<Offer> offers;
    		offers = OfferServiceFactory.getService().findOffers(keywords, null, 
					Calendar.getInstance());
			return OfferToOfferDtoConversor.toOfferDtos(offers);   
        }
    
    @WebMethod(
            operationName="findReserves"
        )
        public List<ReserveDto> findReserves(
                @WebParam(name="offerId") Long offerId,
                @WebParam(name="status") ReserveDtoState status) throws 
                SoapInstanceNotFoundException {
            List<Reserve> reserves;
    		try {
				reserves = OfferServiceFactory.getService().findReserves(offerId, 
						Reserve.ReserveState.valueOf(status.toString()));
				return ReserveToReserveDtoConversor.toReserveDtos(reserves);   
			} catch (InstanceNotFoundException ex) {
				throw new SoapInstanceNotFoundException(
	                    new SoapInstanceNotFoundExceptionInfo(
	                    ex.getInstanceId(), ex.getInstanceType()));
			}
			
        }
    
    @WebMethod(
            operationName="reserveOffer"
        )
        public long reserveOffer(
        		@WebParam(name="offerId")  Long offerId,
                @WebParam(name="userMail")   String userMail,
                @WebParam(name="userCreditCard") String userCreditCard) throws 
                SoapInstanceNotFoundException, SoapInputValidationException, 
                SoapOfferExpirationException, SoapFullOfferException,
                SoapDuplicatedReserveException{
            try {
    			return OfferServiceFactory.getService().reserveOffer(offerId, userMail, 
    					userCreditCard);
    			
    		} catch (InstanceNotFoundException ex) {
    			throw new SoapInstanceNotFoundException(
                        new SoapInstanceNotFoundExceptionInfo(
                        ex.getInstanceId(), ex.getInstanceType()));
    		 } catch (InputValidationException ex) {
    	         throw new SoapInputValidationException(ex.getMessage());
    	     } catch (OfferExpirationException ex) {
    	         throw new SoapOfferExpirationException(new SoapOfferExpirationExceptionInfo(
    	        		 (Long) ex.getInstanceId()));
    	     } catch (FullOfferException e) {
				throw new SoapFullOfferException(new SoapFullOfferExceptionInfo(
						(Long)e.getInstanceId()));
			} catch (DuplicatedReserveException e) {
				throw new SoapDuplicatedReserveException(
						new SoapDuplicatedReserveExceptionInfo (e.getInstanceId(), 
								e.getUserMail()));
			}
            
        }
    
    @WebMethod(
            operationName="findReserve"
        )
        public ReserveDto findReserve(
                @WebParam(name="reserveId") long reserveId) throws 
                SoapInstanceNotFoundException {
            Reserve reserve;
    		try {
    			reserve = OfferServiceFactory.getService().findReserve(reserveId);
    			return ReserveToReserveDtoConversor.toReserveDto(reserve);
    		} catch (InstanceNotFoundException ex) {
    			throw new SoapInstanceNotFoundException
    				(new SoapInstanceNotFoundExceptionInfo(ex.getInstanceId(), 
    						ex.getInstanceType()));
    		}
            
       	}
   
    @WebMethod(
            operationName="claimOffer"
        )
        public void claimOffer(
                @WebParam(name="reserveId") long reserveId) throws 
                SoapInstanceNotFoundException, SoapReserveExpirationException {
    		try {
    			OfferServiceFactory.getService().claimOffer(reserveId);
    		} catch (InstanceNotFoundException ex) {
    			throw new SoapInstanceNotFoundException(
                        new SoapInstanceNotFoundExceptionInfo(
                        ex.getInstanceId(), ex.getInstanceType()));
    		} catch (ReserveExpirationException e) {
				throw new SoapReserveExpirationException(
						new SoapReserveExpirationExceptionInfo((Long)e.getInstanceId()));
			}
            
       }  

}
