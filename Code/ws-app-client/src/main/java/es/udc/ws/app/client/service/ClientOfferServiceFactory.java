package es.udc.ws.app.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class ClientOfferServiceFactory {

    private final static String CLASS_NAME_PARAMETER = 
            "ClientOfferServiceFactory.className";
    private static Class<ClientOfferService> serviceClass = null;

    private ClientOfferServiceFactory() {
    }

    @SuppressWarnings("unchecked")
    private synchronized static Class<ClientOfferService> getServiceClass() {

        if (serviceClass == null) {
            try {
                String serviceClassName = ConfigurationParametersManager
                        .getParameter(CLASS_NAME_PARAMETER);
                serviceClass = (Class<ClientOfferService>) 
                        Class.forName(serviceClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceClass;

    }

    public static ClientOfferService getService() {

        try {
            return (ClientOfferService) getServiceClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
