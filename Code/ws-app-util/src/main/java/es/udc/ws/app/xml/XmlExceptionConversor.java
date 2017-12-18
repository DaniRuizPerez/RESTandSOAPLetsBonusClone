package es.udc.ws.app.xml;

import es.udc.ws.app.exceptions.DuplicatedReserveException;
import es.udc.ws.app.exceptions.FullOfferException;
import es.udc.ws.app.exceptions.NotRemovableOfferException;
import es.udc.ws.app.exceptions.NotUpdatableOfferException;
import es.udc.ws.app.exceptions.OfferExpirationException;
import es.udc.ws.app.exceptions.ReserveExpirationException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

public class XmlExceptionConversor {

	public final static String CONVERSION_PATTERN =
			"EEE, d MMM yyyy HH:mm:ss Z";

	public final static Namespace XML_NS = XmlOfferDtoConversor.XML_NS;

	public static String getExceptionName(Document document) {
		Element rootElement = document.getRootElement();
		String name = rootElement.getName();
		return name;
		
	}
	
	public static Document getExceptionDocument (InputStream ex)
			throws ParsingException {
			
		SAXBuilder builder = new SAXBuilder();
		try {
			return builder.build(ex);
			
		} catch (JDOMException | IOException e) {
			throw new ParsingException(e);
		}
	}

	/*From XML to JavaException*/

	public static InputValidationException
	fromInputValidationExceptionXml(InputStream ex)
			throws ParsingException {
		try {

			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(ex);
			Element rootElement = document.getRootElement();

			Element message = rootElement.getChild("message", XML_NS);

			return new InputValidationException(message.getText());
		} catch (JDOMException | IOException e) {
			throw new ParsingException(e);
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	public static InputValidationException
	fromInputValidationExceptionXml(Document document)
			throws ParsingException, JDOMException {
		try {
			Element rootElement = document.getRootElement();

			Element message = rootElement.getChild("message", XML_NS);

			return new InputValidationException(message.getText());
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static InstanceNotFoundException
	fromInstanceNotFoundExceptionXml(InputStream ex)
			throws ParsingException {
		try {

			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(ex);
			Element rootElement = document.getRootElement();

			Element instanceId = rootElement.getChild("instanceId", XML_NS);
			Element instanceType =
					rootElement.getChild("instanceType", XML_NS);

			return new InstanceNotFoundException(instanceId.getText(),
					instanceType.getText());
		} catch (JDOMException | IOException e) {
			throw new ParsingException(e);
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	public static InstanceNotFoundException
	fromInstanceNotFoundExceptionXml(Document document)
			throws ParsingException {
		try {
			Element rootElement = document.getRootElement();

			Element instanceId = rootElement.getChild("instanceId", XML_NS);
			Element instanceType =
					rootElement.getChild("instanceType", XML_NS);

			return new InstanceNotFoundException(instanceId.getText(),
					instanceType.getText());
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static OfferExpirationException
	fromOfferExpirationExceptionXml(InputStream ex)
			throws ParsingException {
		try {

			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(ex);
			Element rootElement = document.getRootElement();

			Element instanceId = rootElement.getChild("offerId", XML_NS);

			return new OfferExpirationException(
					Long.parseLong(instanceId.getTextTrim()));
		} catch (JDOMException | IOException | NumberFormatException e) {
			throw new ParsingException(e);
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	public static OfferExpirationException
	fromOfferExpirationExceptionXml(Document document)
			throws ParsingException {
		try {
			Element rootElement = document.getRootElement();

			Element instanceId = rootElement.getChild("offerId", XML_NS);

			return new OfferExpirationException(
					Long.parseLong(instanceId.getTextTrim()));
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static ReserveExpirationException
	fromReserveExpirationExceptionXml(InputStream ex)
			throws ParsingException {
		try {

			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(ex);
			Element rootElement = document.getRootElement();

			Element instanceId = rootElement.getChild("reserveId", XML_NS);

			return new ReserveExpirationException(
					Long.parseLong(instanceId.getTextTrim()));
		} catch (JDOMException | IOException | NumberFormatException e) {
			throw new ParsingException(e);
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	public static ReserveExpirationException
	fromReserveExpirationExceptionXml(Document document)
			throws ParsingException {
		try {
			Element rootElement = document.getRootElement();

			Element instanceId = rootElement.getChild("reserveId", XML_NS);

			return new ReserveExpirationException(
					Long.parseLong(instanceId.getTextTrim()));
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static NotUpdatableOfferException
	fromNotUpdatableOfferExceptionXml(InputStream ex)
			throws ParsingException {
		try {

			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(ex);
			Element rootElement = document.getRootElement();

			Element instanceId = rootElement.getChild("offerId", XML_NS);
			Element instanceType = rootElement.getChild("instanceType", XML_NS);
			Element reason = rootElement.getChild("reason", XML_NS);

			return new NotUpdatableOfferException(
					Long.parseLong(instanceId.getTextTrim()), 
					instanceType.getTextTrim(), reason.getTextTrim());
		} catch (JDOMException | IOException | NumberFormatException e) {
			throw new ParsingException(e);
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	public static NotUpdatableOfferException
	fromNotUpdatableOfferExceptionXml(Document document)
			throws ParsingException {
		try {
			Element rootElement = document.getRootElement();

			Element instanceId = rootElement.getChild("offerId", XML_NS);
			Element instanceType = rootElement.getChild("instanceType", XML_NS);
			Element reason = rootElement.getChild("reason", XML_NS);

			return new NotUpdatableOfferException(
					Long.parseLong(instanceId.getTextTrim()), 
					instanceType.getTextTrim(), reason.getTextTrim());
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}


	public static NotRemovableOfferException
	fromNotRemovableOfferExceptionXml(InputStream ex)
			throws ParsingException {
		try {

			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(ex);
			Element rootElement = document.getRootElement();

			Element instanceId = rootElement.getChild("offerId", XML_NS);
			Element instanceType = rootElement.getChild("instanceType", XML_NS);
			Element reason = rootElement.getChild("reason", XML_NS);

			return new NotRemovableOfferException(
					Long.parseLong(instanceId.getTextTrim()), 
					instanceType.getTextTrim(), reason.getTextTrim());
		} catch (JDOMException | IOException | NumberFormatException e) {
			throw new ParsingException(e);
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	public static NotRemovableOfferException
	fromNotRemovableOfferExceptionXml(Document document)
			throws ParsingException {
		try {
			Element rootElement = document.getRootElement();

			Element instanceId = rootElement.getChild("offerId", XML_NS);
			Element instanceType = rootElement.getChild("instanceType", XML_NS);
			Element reason = rootElement.getChild("reason", XML_NS);
			
			return new NotRemovableOfferException(
					Long.parseLong(instanceId.getTextTrim()), 
					instanceType.getTextTrim(), reason.getTextTrim());
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static FullOfferException
	fromFullOfferExceptionXml(InputStream ex)
			throws ParsingException {
		try {

			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(ex);
			Element rootElement = document.getRootElement();

			Element instanceId = rootElement.getChild("offerId", XML_NS);

			return new FullOfferException(
					Long.parseLong(instanceId.getTextTrim()));
		} catch (JDOMException | IOException | NumberFormatException e) {
			throw new ParsingException(e);
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	public static FullOfferException
	fromFullOfferExceptionXml(Document document)
			throws ParsingException {
		try {
			Element rootElement = document.getRootElement();

			Element instanceId = rootElement.getChild("offerId", XML_NS);

			return new FullOfferException(
					Long.parseLong(instanceId.getTextTrim()));
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static DuplicatedReserveException
	fromDuplicatedReserveExceptionXml(InputStream ex)
			throws ParsingException {
		try {

			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(ex);
			Element rootElement = document.getRootElement();

			Element instanceId = rootElement.getChild("offerId", XML_NS);
			Element userMail = rootElement.getChild("userMail", XML_NS);

			return new DuplicatedReserveException(
					Long.parseLong(instanceId.getTextTrim()), userMail.getTextTrim());
		} catch (JDOMException | IOException | NumberFormatException e) {
			throw new ParsingException(e);
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	public static DuplicatedReserveException
	fromDuplicatedReserveExceptionXml(Document document)
			throws ParsingException {
		try {
			Element rootElement = document.getRootElement();

			Element instanceId = rootElement.getChild("offerId", XML_NS);
			Element userMail = rootElement.getChild("userMail", XML_NS);

			return new DuplicatedReserveException(
					Long.parseLong(instanceId.getTextTrim()), userMail.getTextTrim());
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	/*From JavaException to XML*/
	public static Document toInputValidationExceptionXml(
			InputValidationException ex)
					throws IOException {

		Element exceptionElement =
				new Element("InputValidationException", XML_NS);

		Element messageElement = new Element("message", XML_NS);
		messageElement.setText(ex.getMessage());
		exceptionElement.addContent(messageElement);

		return new Document(exceptionElement);
	}

	public static Document toInstanceNotFoundException (
			InstanceNotFoundException ex)
					throws IOException {

		Element exceptionElement =
				new Element("InstanceNotFoundException", XML_NS);

		if(ex.getInstanceId() != null) {
			Element instanceIdElement = new Element("instanceId", XML_NS);
			instanceIdElement.setText(ex.getInstanceId().toString());

			exceptionElement.addContent(instanceIdElement);
		}

		if(ex.getInstanceType() != null) {
			Element instanceTypeElement = new Element("instanceType", XML_NS);
			instanceTypeElement.setText(ex.getInstanceType());

			exceptionElement.addContent(instanceTypeElement);
		}
		return new Document(exceptionElement);
	}

	public static Document toOfferExpirationException (
			OfferExpirationException ex)
					throws IOException {

		Element exceptionElement =
				new Element("OfferExpirationException", XML_NS);

		if(ex.getInstanceId() != null) {
			Element offerIdElement = new Element("offerId", XML_NS);
			offerIdElement.setText(ex.getInstanceId().toString());
			exceptionElement.addContent(offerIdElement);
		}

		return new Document(exceptionElement);
	}

	public static Document toReserveExpirationException (
			ReserveExpirationException ex)
					throws IOException {

		Element exceptionElement =
				new Element("ReserveExpirationException", XML_NS);

		if(ex.getInstanceId() != null) {
			Element offerIdElement = new Element("reserveId", XML_NS);
			offerIdElement.setText(ex.getInstanceId().toString());
			exceptionElement.addContent(offerIdElement);
		}

		return new Document(exceptionElement);

	}

	public static Document toNotUpdatableOfferException (
			NotUpdatableOfferException ex)
					throws IOException {

		Element exceptionElement =
				new Element("NotUpdatableOfferException", XML_NS);

		if(ex.getInstanceId() != null) {
			Element offerIdElement = new Element("offerId", XML_NS);
			offerIdElement.setText(ex.getInstanceId().toString());
			exceptionElement.addContent(offerIdElement);
		}

		if(ex.getInstanceType() != null) {
			Element instanceTypeElement = new Element("instanceType", XML_NS);
			instanceTypeElement.setText(ex.getInstanceType().toString());
			exceptionElement.addContent(instanceTypeElement);
		}

		if(ex.getReason() != null) {
			Element reasonElement = new Element("reason", XML_NS);
			reasonElement.setText(ex.getReason().toString());
			exceptionElement.addContent(reasonElement);
		}


		return new Document(exceptionElement);

	}
	
	public static Document toNotRemovableOfferException (
			NotRemovableOfferException ex)
					throws IOException {

		Element exceptionElement =
				new Element("NotRemovableOfferException", XML_NS);

		if(ex.getInstanceId() != null) {
			Element offerIdElement = new Element("offerId", XML_NS);
			offerIdElement.setText(ex.getInstanceId().toString());
			exceptionElement.addContent(offerIdElement);
		}

		if(ex.getInstanceType() != null) {
			Element instanceTypeElement = new Element("instanceType", XML_NS);
			instanceTypeElement.setText(ex.getInstanceType().toString());
			exceptionElement.addContent(instanceTypeElement);
		}

		if(ex.getReason() != null) {
			Element reasonElement = new Element("reason", XML_NS);
			reasonElement.setText(ex.getReason().toString());
			exceptionElement.addContent(reasonElement);
		}


		return new Document(exceptionElement);

	}
	
	public static Document toFullOfferException (
			FullOfferException ex)
					throws IOException {

		Element exceptionElement =
				new Element("FullOfferException", XML_NS);

		if(ex.getInstanceId() != null) {
			Element offerIdElement = new Element("offerId", XML_NS);
			offerIdElement.setText(ex.getInstanceId().toString());
			exceptionElement.addContent(offerIdElement);
		}

		return new Document(exceptionElement);

	}
	
	public static Document toDuplicatedReserveException (
			DuplicatedReserveException ex)
					throws IOException {

		Element exceptionElement =
				new Element("DuplicatedReserveException", XML_NS);

		if(ex.getInstanceId() != null) {
			Element offerIdElement = new Element("offerId", XML_NS);
			offerIdElement.setText(ex.getInstanceId().toString());
			exceptionElement.addContent(offerIdElement);
		}

		if(ex.getUserMail() != null) {
			Element userMailElement = new Element("userMail", XML_NS);
			userMailElement.setText(ex.getUserMail().toString());
			exceptionElement.addContent(userMailElement);
		}

		return new Document(exceptionElement);

	}




}
