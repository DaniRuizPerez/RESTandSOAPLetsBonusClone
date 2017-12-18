package es.udc.ws.app.model.offerservice;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class OfferServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "OfferServiceFactory.className";
    private static OfferService service = null;

    private OfferServiceFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static OfferService getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (OfferService) serviceClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static OfferService getService() {

        if (service == null) {
            service = getInstance();
        }
        return service;

    }
}
