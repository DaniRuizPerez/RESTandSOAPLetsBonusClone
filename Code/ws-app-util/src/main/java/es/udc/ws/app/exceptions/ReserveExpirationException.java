package es.udc.ws.app.exceptions;

@SuppressWarnings("serial")
public class ReserveExpirationException extends Exception {

    private Object instanceId;

    public ReserveExpirationException(Object instanceId) {

        super("Reserve" + instanceId + "has expired");
        this.instanceId = instanceId;

    }

    public Object getInstanceId() {
        return instanceId;
    }

}

