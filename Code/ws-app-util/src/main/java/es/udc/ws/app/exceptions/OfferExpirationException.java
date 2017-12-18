package es.udc.ws.app.exceptions;

@SuppressWarnings("serial")
public class OfferExpirationException extends Exception {

    private Object instanceId;

    public OfferExpirationException(Object instanceId) {

        super("Offer" + instanceId + "has expired");
        this.instanceId = instanceId;

    }

    public Object getInstanceId() {
        return instanceId;
    }

}

