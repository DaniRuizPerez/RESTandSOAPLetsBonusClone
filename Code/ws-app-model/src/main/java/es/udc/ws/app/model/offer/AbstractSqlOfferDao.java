package es.udc.ws.app.model.offer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import es.udc.ws.app.format.FormatUtils;
import es.udc.ws.app.model.offer.Offer.OfferState;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

/**
 * A partial implementation of
 * <code>SQLofferDAO</code> that leaves
 * <code>create(Connection, offer)</code> as abstract.
 */
public abstract class AbstractSqlOfferDao implements SqlOfferDao {

    protected AbstractSqlOfferDao() {
    }
    

    @Override
    public Offer find(Connection connection, Long offerId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT offer_id, offer_name, "
                + " offer_description, offer_price, offer_reduced_price," +
                "offer_reserved, offer_claimed, offer_max_reserves," +
                "offer_ini_date_reserve, offer_end_date_reserve," +
                "offer_end_date_claim, offer_state " +
                "FROM offer WHERE offer_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, offerId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(offerId,
                        Offer.class.getName());
            }

            /* Get results. */
            i = 1;
            long offer_id = resultSet.getLong(i++);
            String offer_name = resultSet.getString(i++);
            String offer_description = resultSet.getString(i++);
            float offer_price = resultSet.getFloat(i++);
            float offer_reduced_price = resultSet.getFloat(i++);
            int offer_reserved = resultSet.getInt(i++);
            int offer_claimed = resultSet.getInt(i++);
            Integer offer_max_reserves = resultSet.getInt(i++);
            if (offer_max_reserves == 0)
            	offer_max_reserves = null;
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(resultSet.getTimestamp(i++));
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(resultSet.getTimestamp(i++));
            Calendar enjoyDate = Calendar.getInstance();
            enjoyDate.setTime(resultSet.getTimestamp(i++));
            String offer_state = resultSet.getString(i++); 
            
            OfferState status = OfferState.valueOf(offer_state);

            /* Return offer. */
            return new Offer(offer_id, offer_name, offer_description, offer_price, 
            		offer_reduced_price, offer_reserved, offer_claimed, offer_max_reserves,
            		startDate, endDate, enjoyDate, status);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Offer> findByKeys(Connection connection, String keywords, 
    		OfferState status, Calendar date) {
    	
        /* Create "queryString". */
        String[] words = keywords != null ? keywords.split(" ") : null;
        String queryString = "SELECT offer_id, offer_name, "
                + " offer_description, offer_price, offer_reduced_price," +
                "offer_reserved, offer_claimed, offer_max_reserves," +
                "offer_ini_date_reserve, offer_end_date_reserve," +
                "offer_end_date_claim, offer_state FROM offer";
        
        if (keywords!=null || status != null || date !=null)
        	queryString += " WHERE";
        
        if (words != null && words.length > 0) {
            for (int i = 0; i < words.length; i++) {
                if (i > 0) {
                    queryString += " AND";
                }
                queryString += " (LOWER(offer_name) LIKE LOWER(?)" +
                		" OR LOWER(offer_description) LIKE LOWER(?))";
            }
        }
        
        if (status != null) {
        	if (keywords != null)
        		queryString += " AND";
        	queryString+="(offer_state = '"+status.toString()+"')";
        }
        
        String sDate = null;
        if (date != null) {
        	if (status != null || keywords != null)
        		queryString += " AND";
    		sDate = FormatUtils.calendarToStringSql(date);
    		queryString+= " ((?) >= offer_ini_date_reserve AND " +
    				"offer_end_date_reserve >= (?))";
        }
        	
        queryString += " ORDER BY offer_name";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
        	int j = 0;
            if (words != null) {
                /* Fill "preparedStatement". */
            	
            	int i = 1;
                for (String word:words) {
                    preparedStatement.setString(i++, "%" + word + "%");
                    preparedStatement.setString(i++, "%" + word + "%");
                    j+=2;
                }
            }
            
            if (date != null) {
            	preparedStatement.setString(j+1, sDate);
            	preparedStatement.setString(j+2, sDate);
            }
            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Read offers. */
            List<Offer> offers = new ArrayList<Offer>();

            while (resultSet.next()) {
            	
                int i = 1;
                long offer_id = resultSet.getLong(i++);
                String offer_name = resultSet.getString(i++);
                String offer_description = resultSet.getString(i++);
                float offer_price = resultSet.getFloat(i++);
                float offer_reduced_price = resultSet.getFloat(i++);
                int offer_reserved = resultSet.getInt(i++);
                int offer_claimed = resultSet.getInt(i++);
                Integer offer_max_reserves = resultSet.getInt(i++);
                if (offer_max_reserves == 0)
                	offer_max_reserves = null;
                Calendar startDate = Calendar.getInstance();
                startDate.setTime(resultSet.getTimestamp(i++));
                Calendar endDate = Calendar.getInstance();
                endDate.setTime(resultSet.getTimestamp(i++));
                Calendar enjoyDate = Calendar.getInstance();
                enjoyDate.setTime(resultSet.getTimestamp(i++));
                String offer_state = resultSet.getString(i++); 
                
                OfferState offer_status = OfferState.valueOf(offer_state);

                offers.add(new Offer(offer_id, offer_name, offer_description, offer_price, 
                		offer_reduced_price, offer_reserved, offer_claimed, offer_max_reserves,
                		startDate, endDate, enjoyDate, offer_status));

            }

            /* Return offers. */
            return offers;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Connection connection, Offer offer)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE offer"
                + " SET offer_name = ?, "
                + " offer_description = ?, offer_price = ?, offer_reduced_price = ?," +
                "offer_max_reserves = ?, offer_reserved = ?, offer_claimed = ?,"  +
                "offer_ini_date_reserve = ?, offer_end_date_reserve = ?,"+
        		"offer_end_date_claim = ?, offer_state = ? WHERE offer_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
        	
            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, offer.getName());
            preparedStatement.setString(i++, offer.getDescription());
            preparedStatement.setFloat(i++, offer.getRealPrice());
            preparedStatement.setFloat(i++, offer.getDiscountedPrice());
            if(offer.getMaxPeople()==null)
            	preparedStatement.setNull(i++, Types.BIGINT);
            else
            	preparedStatement.setInt(i++, offer.getMaxPeople());
            preparedStatement.setInt(i++, offer.getNumberOfReserves());
            preparedStatement.setInt(i++, offer.getClaimedReserves());
            preparedStatement.setString(i++, 
            		FormatUtils.calendarToStringSql(offer.getStartDate()));
            preparedStatement.setString(i++, 
            		FormatUtils.calendarToStringSql(offer.getEndDate()));
            preparedStatement.setString(i++, 
            		FormatUtils.calendarToStringSql(offer.getEnjoyDate()));
            preparedStatement.setString(i++, offer.getStatus().toString());
            preparedStatement.setLong(i++, offer.getOfferId());
            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(offer.getOfferId(),
                        Offer.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void remove(Connection connection, Long offerId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM offer WHERE" + " offer_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, offerId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(offerId,
                        Offer.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
