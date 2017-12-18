package es.udc.ws.app.xml;

import es.udc.ws.app.dto.OfferDto;
import es.udc.ws.app.format.FormatUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

public class XmlOfferDtoConversor {

    public final static Namespace XML_NS =
            Namespace.getNamespace("http://ws.udc.es/offers/xml");

    public static Document toXml(OfferDto offer)
            throws IOException {

        Element offerElement = toJDOMElement(offer);

        return new Document(offerElement);
    }

    public static Document toXml(List<OfferDto> offer)
            throws IOException {

        Element offersElement = new Element("offers", XML_NS);
        for (int i = 0; i < offer.size(); i++) {
            OfferDto xmlOfferDto = offer.get(i);
            Element offerElement = toJDOMElement(xmlOfferDto);
            offersElement.addContent(offerElement);
        }

        return new Document(offersElement);
    }

    public static OfferDto toOffer(InputStream offerXml)
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(offerXml);
            Element rootElement = document.getRootElement();

            return toOffer(rootElement);
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<OfferDto> toOffers(InputStream offerXml)
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(offerXml);
            Element rootElement = document.getRootElement();

            if(!"offers".equalsIgnoreCase(rootElement.getName())) {
                throw new ParsingException("Unrecognized element '"
                    + rootElement.getName() + "' ('offers' expected)");
            }
            @SuppressWarnings("unchecked")
			List<Element> children = rootElement.getChildren();
            List<OfferDto> offerDtos = new ArrayList<>(children.size());
            for (int i = 0; i < children.size(); i++) {
                Element element = children.get(i);
                offerDtos.add(toOffer(element));
            }

            return offerDtos;
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static Element toJDOMElement(OfferDto offer) {

    	Element offerElement = new Element("offer", XML_NS);

        if (offer.getOfferId() != null) {
            Element identifierElement = new Element("offer_id", XML_NS);
            identifierElement.setText(offer.getOfferId().toString());
            offerElement.addContent(identifierElement);
        } 

        if (offer.getMaxPeople() != null) {
        	Element maxPeopleElement = new Element("max_people", XML_NS);
        	maxPeopleElement.setText(Integer.toString(offer.getMaxPeople()));
            offerElement.addContent(maxPeopleElement);
        }
        

        Element realPriceElement = new Element("real_price", XML_NS);
        realPriceElement.setText(Double.toString(offer.getRealPrice()));
        offerElement.addContent(realPriceElement);
        
        Element priceElement = new Element("discounted_price", XML_NS);
        priceElement.setText(Double.toString(offer.getDiscountedPrice()));
        offerElement.addContent(priceElement);

        Element nameElement = new Element("name", XML_NS);
        nameElement.setText(offer.getName());
        offerElement.addContent(nameElement);

        Element descriptionElement = new Element("description", XML_NS);
        descriptionElement.setText(offer.getDescription());
        offerElement.addContent(descriptionElement);
        
        Element startDateElement = new Element("start_date", XML_NS);
        startDateElement.setText(FormatUtils.calendarToStringXML(offer.getStartDate(),
        		"/"));
        offerElement.addContent(startDateElement);
        
        Element endDateElement = new Element("end_date", XML_NS);
        endDateElement.setText(FormatUtils.calendarToStringXML(offer.getEndDate(),"/"));
        offerElement.addContent(endDateElement);
        
        Element enjoyDateElement = new Element("enjoy_date", XML_NS);
        enjoyDateElement.setText(FormatUtils.calendarToStringXML(offer.getEnjoyDate(),
        		"/"));
        offerElement.addContent(enjoyDateElement);

        return offerElement;
    }

    private static OfferDto toOffer(Element offerElement)
            throws ParsingException, DataConversionException {
        if (!"offer".equals(
                offerElement.getName())) {
            throw new ParsingException("Unrecognized element '"
                    + offerElement.getName() + "' ('offer' expected)");
        }
        Element identifierElement = offerElement.getChild("offer_id", XML_NS);
        Long identifier = null;

        if (identifierElement != null) {
            identifier = Long.valueOf(identifierElement.getTextTrim());
        }

        Element maxPeopleElement = offerElement.getChild("max_people", XML_NS);
        Integer maxPeople = null;

        if (maxPeopleElement != null) {
            maxPeople = Integer.valueOf(maxPeopleElement.getTextTrim());
        }

        String name = offerElement
                .getChildTextNormalize("name", XML_NS);
        String description = offerElement
                .getChildTextNormalize("description", XML_NS);

        float realPrice = Float.valueOf(
                offerElement.getChildTextTrim("real_price", XML_NS));
        
        float discountedPrice = Float.valueOf(
                offerElement.getChildTextTrim("discounted_price", XML_NS));
        Calendar startDate = null;
        Calendar endDate = null;
        Calendar enjoyDate = null;
		try {
			startDate = FormatUtils.parseCalendar
					(offerElement.getChildTextNormalize("start_date", XML_NS));
			endDate = FormatUtils.parseCalendar
	        		(offerElement.getChildTextNormalize("end_date", XML_NS));
	        enjoyDate = FormatUtils.parseCalendar
	        		(offerElement.getChildTextNormalize("enjoy_date", XML_NS));
		} catch (ParseException e) {
			/*This parseException can be thrown if an internal error occurs on the called
			 * method. This should never happen in a common java distribution*/
			System.out.println(e.getMessage());
		}

        return new OfferDto(identifier, name, description, realPrice, discountedPrice, 
        		maxPeople, startDate, endDate, enjoyDate);
    }

}
