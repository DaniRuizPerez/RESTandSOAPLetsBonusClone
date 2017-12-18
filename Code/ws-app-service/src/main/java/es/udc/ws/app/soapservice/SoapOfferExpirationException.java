package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapOfferExpirationException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapOfferExpirationException extends Exception {

    private SoapOfferExpirationExceptionInfo faultInfo;

    protected SoapOfferExpirationException(
            SoapOfferExpirationExceptionInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public SoapOfferExpirationExceptionInfo getFaultInfo() {
        return faultInfo;
    }
}
