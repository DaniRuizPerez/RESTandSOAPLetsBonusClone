package es.udc.ws.app.client.service.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

import es.udc.ws.app.client.service.ClientOfferService;
import es.udc.ws.app.dto.OfferDto;
import es.udc.ws.app.dto.ReserveDto;
import es.udc.ws.app.dto.ReserveDto.ReserveDtoState;
import es.udc.ws.app.exceptions.DuplicatedReserveException;
import es.udc.ws.app.exceptions.FullOfferException;
import es.udc.ws.app.exceptions.NotRemovableOfferException;
import es.udc.ws.app.exceptions.NotUpdatableOfferException;
import es.udc.ws.app.exceptions.OfferExpirationException;
import es.udc.ws.app.exceptions.ReserveExpirationException;
import es.udc.ws.app.xml.ParsingException;
import es.udc.ws.app.xml.XmlExceptionConversor;
import es.udc.ws.app.xml.XmlOfferDtoConversor;
import es.udc.ws.app.xml.XmlReserveDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class RestClientOfferService implements ClientOfferService {

    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "RestClientOfferService.endpointAddress";
    private String endpointAddress;

    @Override
    public Long addOffer(OfferDto offer) throws InputValidationException {

        PostMethod method =
                new PostMethod(getEndpointAddress() + "offers");
        try {

            ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
            Document document;
            try {
                document = XmlOfferDtoConversor.toXml(offer);
                XMLOutputter outputter = new XMLOutputter(
                        Format.getPrettyFormat());
                outputter.output(document, xmlOutputStream);
            } catch (IOException ex) {
                throw new InputValidationException(ex.getMessage());
            }
            ByteArrayInputStream xmlInputStream =
                    new ByteArrayInputStream(xmlOutputStream.toByteArray());
            InputStreamRequestEntity requestEntity =
                    new InputStreamRequestEntity(xmlInputStream,
                    "application/xml");
            HttpClient client = new HttpClient();
            method.setRequestEntity(requestEntity);

            int statusCode;
            try {
                statusCode = client.executeMethod(method);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                validateResponse(statusCode, HttpStatus.SC_CREATED, method);
            } catch (InputValidationException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            return getIdFromHeaders(method);

        } finally {
            method.releaseConnection();
        }
    }

    @Override
    public void updateOffer(OfferDto offer)
            throws InputValidationException, InstanceNotFoundException, 
            		NotUpdatableOfferException {
        PutMethod method =
                new PutMethod(getEndpointAddress() + "offers/"
                + offer.getOfferId());
        try {

            ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
            Document document;
            try {
                document = XmlOfferDtoConversor.toXml(offer);
                XMLOutputter outputter = new XMLOutputter(
                        Format.getPrettyFormat());
                outputter.output(document, xmlOutputStream);
            } catch (IOException ex) {
                throw new InputValidationException(ex.getMessage());
            }
            ByteArrayInputStream xmlInputStream =
                    new ByteArrayInputStream(xmlOutputStream.toByteArray());
            InputStreamRequestEntity requestEntity =
                    new InputStreamRequestEntity(xmlInputStream,
                    "application/xml");
            HttpClient client = new HttpClient();
            method.setRequestEntity(requestEntity);

            int statusCode;
            try {
                statusCode = client.executeMethod(method);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                validateResponse(statusCode, HttpStatus.SC_NO_CONTENT, method);
            } catch (InputValidationException | InstanceNotFoundException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            method.releaseConnection();
        }
    }

    @Override
    public void removeOffer(Long offerId) throws InstanceNotFoundException, 
    		NotRemovableOfferException {
        DeleteMethod method =
                new DeleteMethod(getEndpointAddress() + "offers/" + offerId);
        try {
            HttpClient client = new HttpClient();
            int statusCode = client.executeMethod(method);
            validateResponse(statusCode, HttpStatus.SC_NO_CONTENT, method);
        } catch (InstanceNotFoundException ex) {
            throw ex;
        } catch (NotRemovableOfferException ex) {
        	throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            method.releaseConnection();
        }
    }

    @Override
    public List<OfferDto> findOffers(String keywords) {
        GetMethod method = null;
        try {
            method = new GetMethod(getEndpointAddress() + "offers/?keywords="
                    + URLEncoder.encode(keywords == null?"":keywords, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        try {
            HttpClient client = new HttpClient();
            int statusCode;
            try {
                statusCode = client.executeMethod(method);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            try {
                validateResponse(statusCode, HttpStatus.SC_OK, method);

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            try {
                return XmlOfferDtoConversor.toOffers(
                        method.getResponseBodyAsStream());
            } catch (ParsingException | IOException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            method.releaseConnection();
        }
    }



	@Override
	public OfferDto findOffer(long offerId) throws InstanceNotFoundException,
			InstanceNotFoundException {
        GetMethod method = null;
        try {
            method = new GetMethod(getEndpointAddress() + "offers/"
                    + URLEncoder.encode(String.valueOf(offerId), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        try {
            HttpClient client = new HttpClient();
            int statusCode;
            try {
                statusCode = client.executeMethod(method);
                
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            try {
                validateResponse(statusCode, HttpStatus.SC_OK, method);
            }catch (InstanceNotFoundException ex){
            	throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            try {
                return XmlOfferDtoConversor.toOffer(
                        method.getResponseBodyAsStream());
            } catch (ParsingException | IOException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            method.releaseConnection();
        }
	}

	@Override
	public List<ReserveDto> findReserves(Long offerId, ReserveDtoState status)
			throws InstanceNotFoundException {
		GetMethod method = null;
		
        try {
            method = new GetMethod(getEndpointAddress() + "reserves/?offerId="
                    + URLEncoder.encode(String.valueOf(offerId), "UTF-8") + "&state="
                    + URLEncoder.encode(
                    		status == null?"null":status.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        try {
            HttpClient client = new HttpClient();
            int statusCode;
            try {
                statusCode = client.executeMethod(method);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            try {
                validateResponse(statusCode, HttpStatus.SC_OK, method);

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            try {
                return XmlReserveDtoConversor.toReserves(
                        method.getResponseBodyAsStream());
            } catch (ParsingException | IOException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            method.releaseConnection();
        }
	}

	@Override
	public ReserveDto findReserve(long reserveId)
			throws InstanceNotFoundException {
		GetMethod method = null;
		try {
			method = new GetMethod(getEndpointAddress() + "reserves/"
					+ URLEncoder.encode(String.valueOf(reserveId), "UTF-8"));
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
		try {
			HttpClient client = new HttpClient();
			int statusCode;
			try {
				statusCode = client.executeMethod(method);

			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			try {
				validateResponse(statusCode, HttpStatus.SC_OK, method);
			}catch (InstanceNotFoundException ex){
				throw ex;
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			try {
				return XmlReserveDtoConversor.toReserve(
						method.getResponseBodyAsStream());
			} catch (ParsingException | IOException ex) {
				throw new RuntimeException(ex);
			}
		} finally {
			method.releaseConnection();
		}
	}

	@Override
	public long reserveOffer(long offerId, String userMail,
			String userCreditCard) throws InstanceNotFoundException,
			FullOfferException, InputValidationException,
			OfferExpirationException, DuplicatedReserveException {
		
		PostMethod method = new PostMethod(getEndpointAddress() + "reserves");
        try {
            method.addParameter("offerId", Long.toString(offerId));
            method.addParameter("userMail", userMail);
            method.addParameter("creditCardNumber", userCreditCard);

            HttpClient client = new HttpClient();

            int statusCode;
            try {
                statusCode = client.executeMethod(method);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                validateResponse(statusCode, HttpStatus.SC_CREATED, method);
            } catch (InputValidationException| InstanceNotFoundException 
            		| DuplicatedReserveException| OfferExpirationException
            		| FullOfferException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            return getIdFromHeaders(method);
        } finally {
            method.releaseConnection();
        }
	}

	@Override
	public void claimOffer(long reserveId) throws InstanceNotFoundException,
			ReserveExpirationException {
		PostMethod method = null;
		try {
			method = new PostMethod(getEndpointAddress() + "reserves/"+
	        		URLEncoder.encode(String.valueOf(reserveId), "UTF-8") 
	        		+ "/claim");
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
		try {
            HttpClient client = new HttpClient();
            int statusCode;
            try {
                statusCode = client.executeMethod(method);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            try {
                validateResponse(statusCode, HttpStatus.SC_NO_CONTENT, method);

            } catch (InstanceNotFoundException ex) {
                throw ex;
            } catch (ReserveExpirationException ex) {
            	throw ex;
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
        } finally {
            method.releaseConnection();
        }
       
	}
	

    private synchronized String getEndpointAddress() {

        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager.getParameter(
                    ENDPOINT_ADDRESS_PARAMETER);
        }

        return endpointAddress;
    }

    private void validateResponse(int statusCode,
                                  int expectedStatusCode,
                                  HttpMethod method)
            throws InstanceNotFoundException,
            ReserveExpirationException, InputValidationException,
            ParsingException, DuplicatedReserveException, 
            FullOfferException, OfferExpirationException,
            NotRemovableOfferException, NotUpdatableOfferException {

        InputStream in;
        try {
            in = method.getResponseBodyAsStream();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        String contentType = getResponseHeader(method, "Content-Type");
        boolean isXmlResponse = "application/xml"
                .equalsIgnoreCase(contentType);
        if (!isXmlResponse && statusCode >= 400) {
            throw new RuntimeException("HTTP error; status code = "
                    + statusCode);
        }
        String name = null;
        switch (statusCode) {
        	case HttpStatus.SC_FORBIDDEN:
            try {
                throw XmlExceptionConversor
                        .fromNotUpdatableOfferExceptionXml(in);
            } catch (ParsingException e) {
                throw new RuntimeException(e);
            }
            case HttpStatus.SC_NOT_FOUND:
                try {
                	Document exceptionDoc = XmlExceptionConversor
            				.getExceptionDocument(in);
            		name = XmlExceptionConversor.getExceptionName(exceptionDoc);
            		if (name.equals("InstanceNotFoundException"))
            			throw XmlExceptionConversor
                        .fromInstanceNotFoundExceptionXml(exceptionDoc);
            		if (name.equals("NotRemovableOfferException"))
            			throw XmlExceptionConversor
            			.fromNotRemovableOfferExceptionXml(exceptionDoc);
                } catch (ParsingException e) {
                    throw new RuntimeException(e);
                }
            case HttpStatus.SC_BAD_REQUEST:
                try {
                    throw XmlExceptionConversor
                            .fromInputValidationExceptionXml(in);
                } catch (ParsingException e) {
                    throw new RuntimeException(e);
                }
            case HttpStatus.SC_GONE:
            	try {
            		Document exceptionDoc = XmlExceptionConversor
            				.getExceptionDocument(in);
            		name = XmlExceptionConversor.getExceptionName(exceptionDoc);
            		if (name.equals("FullOfferException"))
            			throw XmlExceptionConversor
                        .fromFullOfferExceptionXml(exceptionDoc);
            		if (name.equals("ReserveExpirationException"))
            			throw XmlExceptionConversor.
            			fromReserveExpirationExceptionXml(exceptionDoc);
            		if (name.equals("OfferExpirationException"))
            			throw XmlExceptionConversor.
            			fromOfferExpirationExceptionXml(exceptionDoc);
            	} catch (ParsingException e) {
            		throw new RuntimeException(e);
            	}
              case HttpStatus.SC_CONFLICT:
                  try {
                     throw XmlExceptionConversor
                             .fromDuplicatedReserveExceptionXml(in);
                   } catch (ParsingException e) {
                       throw new RuntimeException(e);
                   } 
      
            default:
                if (statusCode != expectedStatusCode) {
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
                }
                break;
        }
    }

    private static Long getIdFromHeaders(HttpMethod method) {
        String location = getResponseHeader(method, "Location");
        if (location != null) {
            int idx = location.lastIndexOf('/');
            return Long.valueOf(location.substring(idx + 1));
        }
        return null;
    }

    private static String getResponseHeader(HttpMethod method,
            String headerName) {
        Header[] headers = method.getResponseHeaders();
        for (int i = 0; i < headers.length; i++) {
            Header header = headers[i];
            if (headerName.equalsIgnoreCase(header.getName())) {
                return header.getValue();
            }
        }
        return null;
    }
}
