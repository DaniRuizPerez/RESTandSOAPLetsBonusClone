package es.udc.ws.app.soapservice;

public class SoapDuplicatedReserveExceptionInfo {

    private Object offerId;
	private String userMail;

    public SoapDuplicatedReserveExceptionInfo() {
    }

    public SoapDuplicatedReserveExceptionInfo(Object offerId, String userMail) {
        this.offerId = offerId;
        this.userMail = userMail;
    }

    public Object getOfferId() {
        return offerId;
    }
    
    public String getUserMail() {
    	return userMail;
    }
    
    public void setUserMail(String userMail) {
    	this.userMail = userMail;
    }
  
    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }    
    
}
