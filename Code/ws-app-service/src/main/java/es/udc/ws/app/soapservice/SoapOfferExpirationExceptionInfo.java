package es.udc.ws.app.soapservice;

import java.util.Calendar;

public class SoapOfferExpirationExceptionInfo {

    private Long offerId;

    public SoapOfferExpirationExceptionInfo() {
    }

    public SoapOfferExpirationExceptionInfo(Long offerId) {
        this.offerId = offerId;
    }

    public Long getOfferId() {
        return offerId;
    }
    
    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }    
    
}
