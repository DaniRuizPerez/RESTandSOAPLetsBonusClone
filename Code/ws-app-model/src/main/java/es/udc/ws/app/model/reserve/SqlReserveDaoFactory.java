package es.udc.ws.app.model.reserve;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlReserveDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlReserveDaoFactory.className";
    private static SqlReserveDao dao = null;

    private SqlReserveDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlReserveDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlReserveDao) daoClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlReserveDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}