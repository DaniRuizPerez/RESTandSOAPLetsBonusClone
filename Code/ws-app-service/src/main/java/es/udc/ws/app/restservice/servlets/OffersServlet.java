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
import es.udc.ws.app.exceptions.NotRemovableOfferException;
import es.udc.ws.app.exceptions.NotUpdatableOfferException;
import es.udc.ws.app.model.offer.Offer;
import es.udc.ws.app.model.offerservice.OfferServiceFactory;
import es.udc.ws.app.serviceutil.OfferToOfferDtoConversor;
import es.udc.ws.app.xml.ParsingException;
import es.udc.ws.app.xml.XmlExceptionConversor;
import es.udc.ws.app.xml.XmlOfferDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class OffersServlet extends HttpServlet{
	
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
    	
        OfferDto xmloffer;
        try {
            xmloffer = XmlOfferDtoConversor
                    .toOffer(req.getInputStream());
        } catch (ParsingException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
                    XmlExceptionConversor.toInputValidationExceptionXml(
                        new InputValidationException(ex.getMessage())),
                    null);

            return;

        }
        Offer offer = OfferToOfferDtoConversor.toOffer(xmloffer);
        try {
            offer = OfferServiceFactory.getService().addOffer(offer);
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
                    XmlExceptionConversor.toInputValidationExceptionXml(
                    new InputValidationException(ex.getMessage())),
                    null);
            return;
        }
        OfferDto offerDto = OfferToOfferDtoConversor.toOfferDto(offer);

        String offerURL = req.getRequestURL().append("/").append(
                offer.getOfferId()).toString();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", offerURL);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                XmlOfferDtoConversor.toXml(offerDto), headers);
    }	 

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        String requestURI = req.getRequestURI();
        int idx = requestURI.lastIndexOf('/');
        if (idx <= 0) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlExceptionConversor.toInputValidationExceptionXml(
                    new InputValidationException("Invalid Request: " + 
                        "unable to find offer id")), null);
            return;
        }
        Long offerId;
        String offerIdAsString = requestURI.substring(idx + 1);
        try {
            offerId = Long.valueOf(offerIdAsString);
        } catch (NumberFormatException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlExceptionConversor.toInputValidationExceptionXml(
                    new InputValidationException("Invalid Request: " + 
                        "unable to parse offer id '" + 
                    offerIdAsString + "'")), null);
            return;
        }        
        
        OfferDto offerDto;
        try {
            offerDto = XmlOfferDtoConversor
                    .toOffer(req.getInputStream());
        } catch (ParsingException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
                    XmlExceptionConversor.toInputValidationExceptionXml(
                    new InputValidationException(ex.getMessage())), 
                    null);
            return;
            
        }
        Offer offer = OfferToOfferDtoConversor.toOffer(offerDto);
        offer.setOfferId(offerId);
        try {
            OfferServiceFactory.getService().updateOffer(offer);
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
                    XmlExceptionConversor.toInputValidationExceptionXml(
                    new InputValidationException(ex.getMessage())), 
                    null);
            return;
        } catch (InstanceNotFoundException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND, 
                    XmlExceptionConversor.toInstanceNotFoundException(
                new InstanceNotFoundException(
                    ex.getInstanceId().toString(), ex.getInstanceType())),
                    null);       
            return;
        } catch (NotUpdatableOfferException ex) {
        	ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, 
                    XmlExceptionConversor.toNotUpdatableOfferException(
                new NotUpdatableOfferException(
                    ex.getInstanceId().toString(), ex.getInstanceType(), ex.getReason())),
                    null);       
            return;
		}
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, 
                null, null);        
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        int idx = requestURI.lastIndexOf('/');
        if (idx <= 0) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlExceptionConversor.toInputValidationExceptionXml(
                    new InputValidationException("Invalid Request: " + 
                        "unable to find offer id")), null);
            return;
        }
        Long offerId;
        String offerIdAsString = requestURI.substring(idx + 1);
        try {
            offerId = Long.valueOf(offerIdAsString);
        } catch (NumberFormatException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlExceptionConversor.toInputValidationExceptionXml(
                    new InputValidationException("Invalid Request: " + 
                        "unable to parse offer id '" + offerIdAsString + "'")),
                    null);
            
            return;
        }
        try {
            OfferServiceFactory.getService().removeOffer(offerId);
        } catch (InstanceNotFoundException ex) {
           ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                   XmlExceptionConversor.toInstanceNotFoundException(
                    new InstanceNotFoundException(
                            ex.getInstanceId().toString(),
                    ex.getInstanceType())), null);
            return;
        } catch (NotRemovableOfferException ex) {
        	ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND, 
                    XmlExceptionConversor.toNotRemovableOfferException(
                new NotRemovableOfferException(
                    ex.getInstanceId().toString(), ex.getInstanceType(), ex.getReason())),
                    null);       
            return;
		}
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, 
                null, null);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if(path == null || path.length() == 0 || "/".equals(path)) {
            String keyWords = req.getParameter("keywords").equals("")?null:
            	req.getParameter("keywords");
            List<Offer> offers = OfferServiceFactory.getService()
                    .findOffers(keyWords, null, Calendar.getInstance());
            List<OfferDto> offerDtos = OfferToOfferDtoConversor
                    .toOfferDtos(offers);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    XmlOfferDtoConversor.toXml(offerDtos), null);
        } else {
            String offerIdAsString = path.endsWith("/") && path.length() > 2 ? 
                    path.substring(1, path.length() - 1) : path.substring(1);
            Long offerId;
            try {
                offerId = Long.valueOf(offerIdAsString);
            } catch (NumberFormatException ex) {
                ServletUtils.writeServiceResponse(resp, 
                		HttpServletResponse.SC_BAD_REQUEST,
                        XmlExceptionConversor.toInputValidationExceptionXml(
                        new InputValidationException("Invalid Request: " +
                            "parameter 'offerId' is invalid '" + 
                            offerIdAsString + "'")),
                        null);

                return;
            }
             Offer offer;
            try {
                offer = OfferServiceFactory.getService().findOffer(offerId);
            } catch (InstanceNotFoundException ex) {
                ServletUtils.writeServiceResponse(resp, 
                		HttpServletResponse.SC_NOT_FOUND, 
                        XmlExceptionConversor.toInstanceNotFoundException(
                new InstanceNotFoundException(
                    ex.getInstanceId().toString(), ex.getInstanceType())),
                    null);
                return;
            }
            OfferDto offerDto = OfferToOfferDtoConversor.toOfferDto(offer);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                XmlOfferDtoConversor.toXml(offerDto), null);
        }
    }    
    
}
