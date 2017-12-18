package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapReserveExpirationException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapReserveExpirationException extends Exception {

    private SoapReserveExpirationExceptionInfo faultInfo;

    protected SoapReserveExpirationException(
            SoapReserveExpirationExceptionInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public SoapReserveExpirationExceptionInfo getFaultInfo() {
        return faultInfo;
    }
}
