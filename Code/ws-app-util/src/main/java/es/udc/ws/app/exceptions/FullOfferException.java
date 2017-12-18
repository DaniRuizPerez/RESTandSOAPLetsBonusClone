package es.udc.ws.app.exceptions;

@SuppressWarnings("serial")
public class FullOfferException extends Exception {

    private Object instanceId;

    public FullOfferException(Object instanceId) {

        super("Offer" + instanceId + "is full");
        this.instanceId = instanceId;

    }

    public Object getInstanceId() {
        return instanceId;
    }

}
