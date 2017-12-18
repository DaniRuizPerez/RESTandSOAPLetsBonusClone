package es.udc.ws.app.client.service.soap;

import es.udc.ws.app.dto.OfferDto;
import es.udc.ws.app.format.FormatUtils;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;


public class OfferDtoToSoapOfferDtoConversor {
    
    public static es.udc.ws.app.client.service.soap.wsdl.OfferDto 
            toSoapOfferDto(OfferDto offer) throws DatatypeConfigurationException {
    	
        es.udc.ws.app.client.service.soap.wsdl.OfferDto soapOfferDto = 
                new es.udc.ws.app.client.service.soap.wsdl.OfferDto();
        soapOfferDto.setDescription(offer.getDescription());
        soapOfferDto.setDiscountedPrice(offer.getDiscountedPrice());
        soapOfferDto.setEndDate(FormatUtils.calendarToXMLGregorianCalendar(
        		offer.getEndDate()));
        soapOfferDto.setEnjoyDate(FormatUtils.calendarToXMLGregorianCalendar(
        		offer.getEnjoyDate()));
        soapOfferDto.setMaxPeople(offer.getMaxPeople());
        soapOfferDto.setName(offer.getName());
        soapOfferDto.setOfferId(offer.getOfferId());
        soapOfferDto.setRealPrice(offer.getRealPrice());
        soapOfferDto.setStartDate(FormatUtils.calendarToXMLGregorianCalendar(
        		offer.getStartDate()));
        return soapOfferDto;
    }    
    
    public static OfferDto toOfferDto(
            es.udc.ws.app.client.service.soap.wsdl.OfferDto offer) {
        return new OfferDto(offer.getOfferId(), offer.getName(), offer.getDescription(),
    			offer.getRealPrice(), offer.getDiscountedPrice(), offer.getMaxPeople(), 
    			FormatUtils.XMLGregorianCalendarToCalendar(offer.getStartDate()),
    			FormatUtils.XMLGregorianCalendarToCalendar(offer.getEndDate()), 
    			FormatUtils.XMLGregorianCalendarToCalendar(offer.getEnjoyDate()));
    }     
    
    public static List<OfferDto> toOfferDtos(
            List<es.udc.ws.app.client.service.soap.wsdl.OfferDto> offers) {
        List<OfferDto> offerDtos = new ArrayList<>(offers.size());
        for (int i = 0; i < offers.size(); i++) {
            es.udc.ws.app.client.service.soap.wsdl.OfferDto offer = 
                    offers.get(i);
            offerDtos.add(toOfferDto(offer));
            
        }
        return offerDtos;
    }    
    
}
