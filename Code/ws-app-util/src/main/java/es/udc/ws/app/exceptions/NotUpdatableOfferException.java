package es.udc.ws.app.exceptions;

@SuppressWarnings("serial")
public class NotUpdatableOfferException extends Exception {

    private Object instanceId;
    private String instanceType;
	private String reason;
    
    public NotUpdatableOfferException (String message) {
    	super(message);
    }

    public NotUpdatableOfferException(Object instanceId, String instanceType,
    		String reason) {

        super("Can't update (identifier = '" + instanceId + "' - type = '"
                + instanceType + "'). Reason: "+reason);
        this.instanceId = instanceId;
        this.instanceType = instanceType;
        this.reason = reason;

    }

    public Object getInstanceId() {
        return instanceId;
    }

    public String getInstanceType() {
        return instanceType;
    }
    
    public String getReason() {
    	return reason;
    }
}

