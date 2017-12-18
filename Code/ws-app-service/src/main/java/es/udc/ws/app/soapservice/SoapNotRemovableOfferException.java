package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapNotRemovableOfferException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapNotRemovableOfferException extends Exception {  
    
    public SoapNotRemovableOfferException(String message) {
        super(message);
    }
    
    public String getFaultInfo() {
        return getMessage();
    }    
}