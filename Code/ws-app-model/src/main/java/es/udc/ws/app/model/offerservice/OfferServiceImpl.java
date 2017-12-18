package es.udc.ws.app.model.offerservice;

import static es.udc.ws.app.model.util.ModelConstants.BASE_URL;
import static es.udc.ws.app.model.util.ModelConstants.OFFER_DATA_SOURCE;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import es.udc.ws.app.exceptions.DuplicatedReserveException;
import es.udc.ws.app.exceptions.FullOfferException;
import es.udc.ws.app.exceptions.NotRemovableOfferException;
import es.udc.ws.app.exceptions.NotUpdatableOfferException;
import es.udc.ws.app.exceptions.OfferExpirationException;
import es.udc.ws.app.exceptions.ReserveExpirationException;
import es.udc.ws.app.model.offer.Offer;
import es.udc.ws.app.model.offer.Offer.OfferState;
import es.udc.ws.app.model.reserve.Reserve.ReserveState;
import es.udc.ws.app.model.offer.SqlOfferDao;
import es.udc.ws.app.model.offer.SqlOfferDaoFactory;
import es.udc.ws.app.model.reserve.Reserve;
import es.udc.ws.app.model.reserve.SqlReserveDao;
import es.udc.ws.app.model.reserve.SqlReserveDaoFactory;
import es.udc.ws.app.validation.PropertyValidator;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;

public class OfferServiceImpl implements OfferService {
    /*
     * IMPORTANT: Some JDBC drivers require "setTransactionIsolation" to
     * be called before "setAutoCommit".
     */

    private DataSource dataSource;
    private SqlOfferDao offerDao = null;
    private SqlReserveDao reserveDao = null;

    public OfferServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(OFFER_DATA_SOURCE);
        offerDao = SqlOfferDaoFactory.getDao();
        reserveDao = SqlReserveDaoFactory.getDao();
    }

    
    private void validateOffer(Offer offer) throws InputValidationException {
    	/*Validate Strings*/
    	PropertyValidator.validateMandatoryString("name", offer.getName());
    	PropertyValidator.validateMandatoryString("description", offer.getDescription());
    	
    	/*Validate >=0*/
    	PropertyValidator.validateNotNegativeInt("claimedReserves", 
    			offer.getClaimedReserves());
    	PropertyValidator.validateNotNegativeInt("numberOfReserves",
    			offer.getNumberOfReserves());
    	
    	/*Validar > 0 or null*/
    	PropertyValidator.validateInteger("maxPeople",offer.getMaxPeople());
    	
    	/*Real price -> >0
    	 *Discounted price -> >=0 and lower than realPrice*/
    	PropertyValidator.validatePositiveFloat("realPrice", offer.getRealPrice());
    	PropertyValidator.validateNotNegativeFloat("discountedPrice", 
    			offer.getDiscountedPrice(),
    			offer.getRealPrice());
    	
    	/*Validate not null dates*/
    	PropertyValidator.validateCalendarNotNull("startDate",offer.getStartDate());
    	PropertyValidator.validateCalendarNotNull("endDate", offer.getEndDate());
    	PropertyValidator.validateCalendarNotNull("enjoyDate", offer.getEnjoyDate());
    	
    	/*StartDate must be lower than endDate and enjoyDate*/
    	PropertyValidator.validateDateLower("startDate", offer.getStartDate(),
    			"endDate", offer.getEndDate());
    	PropertyValidator.validateDateLower("startDate", offer.getStartDate(),
    			"enjoyDate", offer.getEnjoyDate());
    	
    	/*EnjoyDate must be equal or greater than endDate
    	 * Commented: problematic restriction*/
    	/*PropertyValidator.validateDateLower("endDate", offer.getEndDate(),
    			"enjoyDate", offer.getEnjoyDate());*/

    	
    	/*Created: claimed and reserved = 0
    	 *Commited: claimed < reserved != 0
    	 *Released: claimed == reserved = 0*/
    	PropertyValidator.validateStatus("status",offer.getStatus().toString(), 
    			offer.getClaimedReserves(), offer.getNumberOfReserves());
    
    }
    
    private void validateReserveParam(Long offerId, String userMail, String creditCard) 
    		throws InputValidationException {
    	PropertyValidator.validateMandatoryString("userMail", userMail);
    	PropertyValidator.validateCreditCard(creditCard);
    	PropertyValidator.validateNotNegativeOrNullLong("offerId", offerId);
    	
    }

    @Override
    public Offer addOffer(Offer offer) throws InputValidationException {

        validateOffer(offer);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection
                        .setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Offer createdOffer = offerDao.create(connection, offer);

                /* Commit. */
                connection.commit();

                return createdOffer;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateOffer(Offer offer) throws InputValidationException,
            InstanceNotFoundException, NotUpdatableOfferException {

        validateOffer(offer);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection
                        .setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
            	
            	/*Check if offer exits*/
            	try {
            		Offer oldOffer = offerDao.find(connection, offer.getOfferId());
            		offer.setStatus(oldOffer.getStatus());
            	} catch (InstanceNotFoundException e) {
            		throw new InstanceNotFoundException(offer.getOfferId(), 
            				Offer.class.getName());
            	}
            	
            	/*Check if offer is updatable*/
            	if (!offer.getStatus().equals(OfferState.CREATED))
            		throw new NotUpdatableOfferException(offer.getOfferId(), 
            				Offer.class.getName(), "Offer state is not created");
            	
                /* Do work. */
                offerDao.update(connection, offer);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            } catch (NotUpdatableOfferException e) {
            	throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void removeOffer(Long offerId) throws InstanceNotFoundException, 
    		NotRemovableOfferException{

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection
                        .setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                
                Offer offer = offerDao.find(connection, offerId);
                /* Do work. */
            	if (offer.getStatus() == OfferState.COMMITTED)
            		throw new NotRemovableOfferException(offer.getOfferId(), Offer.class.getName(),
            				"Commited offers cannot be removed");
            	
                offerDao.remove(connection, offerId);
               
                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            } catch (NotRemovableOfferException e) {
				connection.rollback();
				throw e;
			}

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Offer findOffer(Long offerId) throws InstanceNotFoundException {

        try (Connection connection = dataSource.getConnection()) {
            return offerDao.find(connection, offerId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Offer> findOffers(String keywords, OfferState status, Calendar date) {

        try (Connection connection = dataSource.getConnection()) {
            return offerDao.findByKeys(connection, keywords, status, date);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getOfferUrl(Long offerId) {
        return BASE_URL + offerId + "/" + UUID.randomUUID().toString();
    }
    
    @Override
    public Long reserveOffer(Long offerId, String userMail,
    		String userCreditCard) throws InstanceNotFoundException, 
    		InputValidationException, OfferExpirationException, FullOfferException, 
    		DuplicatedReserveException{
    	//Creamos una reserva de oferta, obtenemos su id y devolvemos el codigo de la reserva
    	validateReserveParam(offerId, userMail, userCreditCard);
    	Calendar today = Calendar.getInstance();
    	Reserve reserve = new Reserve(offerId, userMail, userCreditCard, today);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection
                        .setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                
                /*Check errors*/
                Offer offer = offerDao.find(connection, offerId);
                if (today.after(offer.getEndDate()))
                	throw new OfferExpirationException(offer.getOfferId());
                if (offer.getMaxPeople() != null)
	                if (offer.getMaxPeople() == offer.getNumberOfReserves())
	                	throw new FullOfferException(offer.getOfferId());
                try {
                	reserveDao.findByOfferIdAndUser(connection, offerId, userMail);
                	throw new DuplicatedReserveException(offerId, userMail);
                } catch (InstanceNotFoundException e) {
                	//If we catch it, all is OK
                }
                
                /* Do work. */
                Reserve createdReserve = reserveDao.create(connection, 
                		reserve);
                int reserves = offer.getNumberOfReserves()+1;
                offer.setNumberOfReserves(reserves);
                offer.setStatus(OfferState.COMMITTED); //State now is commited
                offerDao.update(connection, offer);
                /* Commit. */
                connection.commit();

                return createdReserve.getReserveId();

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            } catch (OfferExpirationException e) {
                connection.rollback();
                throw e;
            } catch (FullOfferException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public List<Reserve> findReserves(Long offerId,
    		ReserveState status) throws InstanceNotFoundException{
    	try (Connection connection = dataSource.getConnection()) {
    		return reserveDao.find(connection, offerId, status);
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Reserve findReserve(Long reserveId) throws InstanceNotFoundException{
    	try (Connection connection = dataSource.getConnection()) {
            return reserveDao.findByReserveId(connection, reserveId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void claimOffer(Long reserveId) throws InstanceNotFoundException, 
    ReserveExpirationException{
    	//Obtain the Offer we've reserved

    	try (Connection connection = dataSource.getConnection()) {
    		try{
    			 /* Prepare connection. */
                connection
                        .setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                
	            Reserve reserve = reserveDao.findByReserveId(connection, reserveId);
	            Long offerId = reserve.getOfferId();
	            Offer offer = offerDao.find(connection, offerId);
	            Calendar today = (Calendar) Calendar.getInstance();
	            if (today.after(offer.getEnjoyDate()))
	            	throw new ReserveExpirationException(reserve.getReserveId());
	            reserve.setStatus(ReserveState.CLOSED);
	            int claimedReserves = offer.getClaimedReserves()+1;
	            offer.setClaimedReserves(claimedReserves);
	            if (offer.getClaimedReserves() == offer.getNumberOfReserves())
	            	offer.setStatus(OfferState.RELEASED);
	            reserveDao.update(connection, reserve);
	            connection.commit();
	            offerDao.update(connection, offer);
	            
	            /* Commit. */
                connection.commit();
        
	        } catch (RuntimeException | Error e) {
	            connection.rollback();
	            throw e;
	        } catch (ReserveExpirationException e) {
	        	connection.rollback();
	            throw e;
			}
    	} catch (SQLException e) {
            throw new RuntimeException(e);
    	}

    }
}
