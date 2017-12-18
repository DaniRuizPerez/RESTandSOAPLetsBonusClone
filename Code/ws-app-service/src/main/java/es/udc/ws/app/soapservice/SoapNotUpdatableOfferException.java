package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapNotUpdatableOfferException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapNotUpdatableOfferException extends Exception {  
    
    public SoapNotUpdatableOfferException(String message) {
        super(message);
    }
    
    public String getFaultInfo() {
        return getMessage();
    }    
}