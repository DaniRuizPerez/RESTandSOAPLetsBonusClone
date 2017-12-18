package es.udc.ws.app.exceptions;

@SuppressWarnings("serial")
public class DuplicatedReserveException extends Exception {

    private Object instanceId;
    private String userMail;

    public DuplicatedReserveException(Object instanceId, String userMail) {

        super("Offer" + instanceId + "was reserved previously by user "+userMail);
        this.instanceId = instanceId;
        this.userMail = userMail;

    }

    public Object getInstanceId() {
        return instanceId;
    }
    
    public String getUserMail() {
        return userMail;
    }

}
