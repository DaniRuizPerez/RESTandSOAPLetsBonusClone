package es.udc.ws.app.soapservice;

public class SoapReserveExpirationExceptionInfo {

    private Long reserveId;

	public SoapReserveExpirationExceptionInfo() {
    }

    public SoapReserveExpirationExceptionInfo(Long reserveId) {
        this.reserveId = reserveId;
    }

	public Long getReserveId() {
		return reserveId;
	}

	public void setReserveId(Long reserveId) {
		this.reserveId = reserveId;
	}    

}