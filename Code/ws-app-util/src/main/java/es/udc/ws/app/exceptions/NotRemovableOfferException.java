package es.udc.ws.app.exceptions;

@SuppressWarnings("serial")
public class NotRemovableOfferException extends Exception {

    private Object instanceId;
    private String instanceType;
    private String reason;
    private String message;

    public NotRemovableOfferException(Object instanceId, String instanceType,
    		String reason) {

        super("Can't remove (identifier = '" + instanceId + "' - type = '"
                + instanceType + "'). Reason: "+reason);
        this.instanceId = instanceId;
        this.instanceType = instanceType;
        this.reason = reason;

    }
    
    public NotRemovableOfferException(String message) {
    	super(message);
    	this.message = message;
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
