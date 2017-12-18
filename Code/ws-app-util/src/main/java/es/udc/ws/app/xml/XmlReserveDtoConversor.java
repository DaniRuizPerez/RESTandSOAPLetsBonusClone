package es.udc.ws.app.xml;

import es.udc.ws.app.dto.ReserveDto;
import es.udc.ws.app.dto.ReserveDto.ReserveDtoState;
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

public class XmlReserveDtoConversor {

    public final static Namespace XML_NS = Namespace
            .getNamespace("http://ws.udc.es/reserves/xml");

    public static ReserveDto toReserve(InputStream reserveXml)
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(reserveXml);
            Element rootElement = document.getRootElement();

            return toReserve(rootElement);
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static Document toXml(ReserveDto reserve) throws IOException {

    	Element reserveElement = toJDOMElement(reserve);

        return new Document(reserveElement);
    }
    
    public static Document toXml(List<ReserveDto> reserve)
            throws IOException {

        Element reservesElement = new Element("reserves", XML_NS);
        for (int i = 0; i < reserve.size(); i++) {
            ReserveDto xmlReserveDto = reserve.get(i);
            Element offerElement = toJDOMElement(xmlReserveDto);
            reservesElement.addContent(offerElement);
        }

        return new Document(reservesElement);
    }
    
    public static List<ReserveDto> toReserves (InputStream offerXml)
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(offerXml);
            Element rootElement = document.getRootElement();

            if(!"reserves".equalsIgnoreCase(rootElement.getName())) {
                throw new ParsingException("Unrecognized element '"
                    + rootElement.getName() + "' ('reserves' expected)");
            }
            @SuppressWarnings("unchecked")
			List<Element> children = rootElement.getChildren();
            List<ReserveDto> reserveDtos = new ArrayList<>(children.size());
            for (int i = 0; i < children.size(); i++) {
                Element element = children.get(i);
                reserveDtos.add(toReserve(element));
            }

            return reserveDtos;
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static Element toJDOMElement(ReserveDto reserve) {

    	Element reserveElement = new Element("reserve", XML_NS);

        if (reserve.getReserveId() != null) {
            Element reserveIdElement = new Element("reserve_id", XML_NS);
            reserveIdElement.setText(reserve.getReserveId().toString());
            reserveElement.addContent(reserveIdElement);
        }

        if (reserve.getOfferId() != null) {
            Element offerIdElement = new Element("offer_id", XML_NS);
            offerIdElement.setText(reserve.getOfferId().toString());
            reserveElement.addContent(offerIdElement);
        }
        
        Element userCreditCardElement = new Element("userCreditCard", XML_NS);
        userCreditCardElement.setText(reserve.getUserCreditCard());
        reserveElement.addContent(userCreditCardElement);
        
        Element userMailElement = new Element("userMail", XML_NS);
        userMailElement.setText(reserve.getUserMail());
        reserveElement.addContent(userMailElement);
        
        Element statusElement = new Element("status", XML_NS);
        statusElement.setText(reserve.getStatus().toString());
        reserveElement.addContent(statusElement);
        
        Element reserveDateElement = new Element("reserve_date", XML_NS);
        reserveDateElement.setText(FormatUtils.calendarToStringXML(reserve.
        		getReserveDate(), "/"));
        reserveElement.addContent(reserveDateElement);

        return reserveElement;
    }

    private static ReserveDto toReserve(Element reserveElement)
            throws ParsingException, DataConversionException,
            NumberFormatException {
        if (!"reserve".equals(reserveElement.getName())) {
            throw new ParsingException("Unrecognized element '"
                    + reserveElement.getName() + "' ('reserve' expected)");
        }
        Element reserveIdElement = reserveElement.getChild("reserve_id", XML_NS);
        Long reserveId = null;
        if (reserveIdElement != null) {
            reserveId = Long.valueOf(reserveIdElement.getTextTrim());
        }

        Element offerIdElement = reserveElement.getChild("offer_id", XML_NS);
        Long offerId = null;
        if (offerIdElement != null) {
            offerId = Long.valueOf(offerIdElement.getTextTrim());
        }

        String userMail = reserveElement
                .getChildTextNormalize("userMail", XML_NS);
        String userCreditCard = reserveElement
                .getChildTextNormalize("userCreditCard", XML_NS);
        ReserveDtoState status = ReserveDtoState.valueOf(reserveElement
                .getChildTextNormalize("status", XML_NS));
        Calendar reserveDate = null;
        try {
			reserveDate = FormatUtils.parseCalendar
					(reserveElement.getChildTextNormalize("reserve_date", XML_NS));

		} catch (ParseException e) {
			/*This parseException can be thrown if an internal error occurs on the called
			 * method. This should never happen in a common java distribution*/
		}
        
        
        return new ReserveDto(reserveId, offerId, userMail, userCreditCard, status, 
        		reserveDate);
    }

}
