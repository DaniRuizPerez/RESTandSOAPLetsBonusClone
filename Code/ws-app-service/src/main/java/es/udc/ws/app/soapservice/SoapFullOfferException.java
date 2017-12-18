package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapFullOfferExpirationException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapFullOfferException extends Exception {

    private SoapFullOfferExceptionInfo faultInfo;

    protected SoapFullOfferException(
            SoapFullOfferExceptionInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public SoapFullOfferExceptionInfo getFaultInfo() {
        return faultInfo;
    }
}
