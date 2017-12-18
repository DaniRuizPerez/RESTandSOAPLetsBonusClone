package es.udc.ws.app.soapservice;

public class SoapFullOfferExceptionInfo {

    private Long offerId;

    public SoapFullOfferExceptionInfo() {
    }

    public SoapFullOfferExceptionInfo(Long offerId) {
        this.offerId = offerId;
    }

    public Long getOfferId() {
        return offerId;
    }
    
    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }    
    
}