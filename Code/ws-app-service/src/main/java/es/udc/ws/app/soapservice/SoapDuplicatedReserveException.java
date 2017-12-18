package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapDuplicatedReserveException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapDuplicatedReserveException extends Exception {

    private SoapDuplicatedReserveExceptionInfo faultInfo;

    protected SoapDuplicatedReserveException(
    		SoapDuplicatedReserveExceptionInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public SoapDuplicatedReserveExceptionInfo getFaultInfo() {
        return faultInfo;
    }
}
