package es.udc.ws.app.model.offer;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

/**
 * A factory to get
 * <code>SqlOfferDao</code> objects. <p> Required configuration parameters: <ul>
 * <li><code>SqlOfferDaoFactory.className</code>: it must specify the full class
 * name of the class implementing
 * <code>SqlOfferDao</code>.</li> </ul>
 */
public class SqlOfferDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlOfferDaoFactory.className";
    private static SqlOfferDao dao = null;

    private SqlOfferDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlOfferDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlOfferDao) daoClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlOfferDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}