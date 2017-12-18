package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import es.udc.ws.app.dto.OfferDto;
import es.udc.ws.app.dto.ReserveDto;
import es.udc.ws.app.exceptions.DuplicatedReserveException;
import es.udc.ws.app.exceptions.FullOfferException;
import es.udc.ws.app.exceptions.OfferExpirationException;
import es.udc.ws.app.exceptions.ReserveExpirationException;
import es.udc.ws.app.model.offer.Offer;
import es.udc.ws.app.model.offerservice.OfferServiceFactory;
import es.udc.ws.app.model.reserve.Reserve;
import es.udc.ws.app.model.reserve.Reserve.ReserveState;
import es.udc.ws.app.serviceutil.OfferToOfferDtoConversor;
import es.udc.ws.app.util.ReserveToReserveDtoConversor;
import es.udc.ws.app.xml.XmlExceptionConversor;
import es.udc.ws.app.xml.XmlOfferDtoConversor;
import es.udc.ws.app.xml.XmlReserveDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class ReservesServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	String path = req.getPathInfo();
    	if(path == null || path.length() == 0 || "/".equals(path)) {
	        String offerIdParameter = req.getParameter("offerId");
	        if (offerIdParameter == null) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlExceptionConversor.toInputValidationExceptionXml(
	                    new InputValidationException("Invalid Request: " +
	                        "parameter 'offerId' is mandatory")), null);
	            return;
	        }
	        Long offerId;
	        try {
	            offerId = Long.valueOf(offerIdParameter);
	        } catch (NumberFormatException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlExceptionConversor.toInputValidationExceptionXml(
	                    new InputValidationException("Invalid Request: " +
	                        "parameter 'offerId' is invalid '" +
	                        offerIdParameter + "'")),
	                    null);
	            return;
	        }
	        String creditCardNumber = req.getParameter("creditCardNumber");
	        if (creditCardNumber == null) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlExceptionConversor.toInputValidationExceptionXml(
	                    new InputValidationException("Invalid Request: "+
	                        "parameter 'creditCardNumber' is mandatory")), null);
	            return;
	        }
	        String userMail = req.getParameter("userMail");
	        if (userMail == null) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlExceptionConversor.toInputValidationExceptionXml(
	                    new InputValidationException("Invalid Request: "+
	                        "parameter 'userMail' is mandatory")), null);
	
	            return;
	        }
	       Long reserveId;
	        try {
	            reserveId = OfferServiceFactory.getService()
	                    .reserveOffer(offerId, userMail, creditCardNumber);
	        } catch (InstanceNotFoundException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
	                    XmlExceptionConversor.toInstanceNotFoundException(
	                    new InstanceNotFoundException(ex.getInstanceId()
	                        .toString(),ex.getInstanceType())), null);
	            return;
	        } catch (InputValidationException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlExceptionConversor.toInputValidationExceptionXml(
	                    new InputValidationException(ex.getMessage())), null);
	            return;
	        } catch (OfferExpirationException ex) {
	        	ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
	                    XmlExceptionConversor.toOfferExpirationException(
	                    new OfferExpirationException(ex.getInstanceId())), null);
	            return;
	        } catch (FullOfferException ex) {
	        	ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
	                    XmlExceptionConversor.toFullOfferException(
	                    new FullOfferException(ex.getInstanceId())), null);
	            return;
	        } catch (DuplicatedReserveException ex) {
	        	ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CONFLICT,
	                    XmlExceptionConversor.toDuplicatedReserveException(
	                    new DuplicatedReserveException(ex.getInstanceId(),
	                    		ex.getUserMail())), null);
	            return;
			}
	        Reserve reserve = null;
			try {
				reserve = OfferServiceFactory.getService().findReserve(reserveId);
			} catch (InstanceNotFoundException e) {
				//This should never happen
				System.out.println("This should never happen");
			}
	        ReserveDto reserveDto = ReserveToReserveDtoConversor.toReserveDto(reserve);
	
	        String reserveURL = req.getRequestURL().append("/").append(
	                reserve.getReserveId()).toString();
	
	        Map<String, String> headers = new HashMap<>(1);
	        headers.put("Location", reserveURL);
	
	        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
	                XmlReserveDtoConversor.toXml(reserveDto), headers);
    	} else {
    		String pathToParse  = path.endsWith("/") && path.length() > 2 ? 
                    path.substring(1, path.length() - 1) : path.substring(1);
            String[] params = pathToParse.split("/");
            String claim = params[1];
            Long reserveId = Long.parseLong(params[0]);
            if (!claim.equals("claim")) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlExceptionConversor.toInputValidationExceptionXml(
	                    new InputValidationException("Invalid Request: " +
	                        "post method should be do to claim a reserve")), null);
	            return;
            }
            try {
                OfferServiceFactory.getService().claimOffer(reserveId);
            } catch (InstanceNotFoundException ex) {
                ServletUtils.writeServiceResponse(resp, 
                		HttpServletResponse.SC_NOT_FOUND, 
                        XmlExceptionConversor.toInstanceNotFoundException(
                new InstanceNotFoundException(
                    ex.getInstanceId().toString(), ex.getInstanceType())),
                    null);
                return;
            } catch (ReserveExpirationException ex) {
            	ServletUtils.writeServiceResponse(resp, 
                		HttpServletResponse.SC_GONE, 
                        XmlExceptionConversor.toReserveExpirationException(
                new ReserveExpirationException(ex.getInstanceId())), null);
                return;
			}
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT,
                null, null);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	String path = req.getPathInfo();
        if(path == null || path.length() == 0 || "/".equals(path)) {
            Long offerId = req.getParameter("offerId").equals("null")?null:
            	Long.parseLong(req.getParameter("offerId"));
            ReserveState state = req.getParameter("state").equals("null")?null:
            	ReserveState.valueOf(req.getParameter("state"));
            List<Reserve> reserves = null;
			try {
				reserves = OfferServiceFactory.getService()
				        .findReserves(offerId,state);
			} catch (InstanceNotFoundException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
	                    XmlExceptionConversor.toInstanceNotFoundException(
	                    new InstanceNotFoundException(ex.getInstanceId()
	                        .toString(),ex.getInstanceType())), null);
	            return;
			}
            List<ReserveDto> reserveDtos = ReserveToReserveDtoConversor
                    .toReserveDtos(reserves);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    XmlReserveDtoConversor.toXml(reserveDtos), null);
        } else {
            String reserveIdAsString = path.endsWith("/") && path.length() > 2 ? 
                    path.substring(1, path.length() - 1) : path.substring(1);
            Long reserveId;
            try {
                reserveId = Long.valueOf(reserveIdAsString);
            } catch (NumberFormatException ex) {
                ServletUtils.writeServiceResponse(resp, 
                		HttpServletResponse.SC_BAD_REQUEST,
                        XmlExceptionConversor.toInputValidationExceptionXml(
                        new InputValidationException("Invalid Request: " +
                            "parameter 'reserveId' is invalid '" + 
                            reserveIdAsString + "'")),
                        null);

                return;
            }
            Reserve reserve;
            try {
                reserve = OfferServiceFactory.getService().findReserve(reserveId);
            } catch (InstanceNotFoundException ex) {
                ServletUtils.writeServiceResponse(resp, 
                		HttpServletResponse.SC_NOT_FOUND, 
                        XmlExceptionConversor.toInstanceNotFoundException(
                new InstanceNotFoundException(
                    ex.getInstanceId().toString(), ex.getInstanceType())),
                    null);
                return;
            }
            ReserveDto reserveDto = ReserveToReserveDtoConversor.toReserveDto(reserve);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                XmlReserveDtoConversor.toXml(reserveDto), null);
        }
    }
}
