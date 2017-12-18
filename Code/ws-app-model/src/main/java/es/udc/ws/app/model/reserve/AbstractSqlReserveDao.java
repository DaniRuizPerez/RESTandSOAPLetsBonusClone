package es.udc.ws.app.model.reserve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import es.udc.ws.app.format.FormatUtils;
import es.udc.ws.app.model.offer.Offer;
import es.udc.ws.app.model.reserve.Reserve;
import es.udc.ws.app.model.reserve.Reserve.ReserveState;
import es.udc.ws.util.exceptions.InstanceNotFoundException;


public abstract class AbstractSqlReserveDao implements SqlReserveDao {


    protected AbstractSqlReserveDao() {
    }
    

    @Override
    public List<Reserve> find(Connection connection, Long offerId, 
    		ReserveState reserveStatus)throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT reserve_id, reserve_offer_id, " +
                "reserve_user_mail, reserve_user_creditcard, "+
                "reserve_state, reserve_date FROM reserve";

        if (offerId != null || reserveStatus != null){
        	queryString += " WHERE";
        				
        	if (offerId != null){
        		queryString += " reserve_offer_id = ?";
        	}
        	
        	if (reserveStatus != null){
        		if (offerId != null){
        			queryString += " AND";
        		}
        		queryString += " reserve_state = ?";
        	}		
        }
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            
        	if (offerId != null){
            	preparedStatement.setLong(i++, offerId.longValue());
            }
        	if (reserveStatus != null){
        		preparedStatement.setString(i++,reserveStatus.toString());
            }
            
            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Reserve> reserves = new ArrayList<Reserve>();
            /* Get results. */
            while(resultSet.next()) {
	            i = 1;
	            long reserve_id = resultSet.getLong(i++);
	            long reserve_offer_id = resultSet.getLong(i++);
	            String reserve_user_mail = resultSet.getString(i++);
	            String reserve_user_creditcard = resultSet.getString(i++);
	            ReserveState status = ReserveState.valueOf(resultSet.getString(i++));
	            Calendar reserveDate = Calendar.getInstance();
	            reserveDate.setTime(resultSet.getTimestamp(i++));
	
	            /* Return reserve. */
	            reserves.add(new Reserve(reserve_id, reserve_offer_id, reserve_user_mail,
	            		reserve_user_creditcard,status,reserveDate));
            }
            return reserves;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    
  
    @Override
    public Reserve findByReserveId(Connection connection, Long reserveId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT reserve_id, reserve_offer_id, " +
                "reserve_user_mail, reserve_user_creditcard, "+
                "reserve_state, reserve_date FROM reserve WHERE reserve_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, reserveId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new InstanceNotFoundException(reserveId,
                        Reserve.class.getName());
            }
            
            /* Get result */
            i = 1;
            long reserve_id = resultSet.getLong(i++);
            long reserve_offer_id = resultSet.getLong(i++);
            String reserve_user_mail = resultSet.getString(i++);
            String reserve_user_creditcard = resultSet.getString(i++);
            ReserveState status = ReserveState.valueOf(resultSet.getString(i++));
            Calendar reserveDate = Calendar.getInstance();
            reserveDate.setTime(resultSet.getTimestamp(i++));
         
            /* Return reserve. */
            return (new Reserve(reserve_id, reserve_offer_id, reserve_user_mail,
            		reserve_user_creditcard, status, reserveDate));
        
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    
    @Override
    public Reserve findByOfferIdAndUser(Connection connection, Long offerId,
    		String userMail)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT reserve_id, reserve_offer_id, " +
                "reserve_user_mail, reserve_user_creditcard, "+
                "reserve_state, reserve_date FROM reserve WHERE reserve_offer_id = ? " +
                "AND reserve_user_mail = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, offerId.longValue());
            preparedStatement.setString(i++, userMail);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new InstanceNotFoundException(null,
                        Reserve.class.getName()); //No me importa el ID en este caso
            }
            
            /* Get result */
            i = 1;
            long reserve_id = resultSet.getLong(i++);
            long reserve_offer_id = resultSet.getLong(i++);
            String reserve_user_mail = resultSet.getString(i++);
            String reserve_user_creditcard = resultSet.getString(i++);
            ReserveState status = ReserveState.valueOf(resultSet.getString(i++));
            Calendar reserveDate = Calendar.getInstance();
            reserveDate.setTime(resultSet.getTimestamp(i++));
         
            /* Return reserve. */
            return (new Reserve(reserve_id, reserve_offer_id, reserve_user_mail,
            		reserve_user_creditcard, status, reserveDate));
        
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    
    
    @Override
    public void update(Connection connection, Reserve reserve)
            throws InstanceNotFoundException {
    	
    	
    	
        /* Create "queryString". */
        String queryString = "UPDATE reserve "
                + "SET reserve_offer_id = ?,"
                + "reserve_user_mail = ?, reserve_user_creditcard = ?," +
                "reserve_state = ?, reserve_date = ? WHERE reserve_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, reserve.getOfferId());
            preparedStatement.setString(i++, reserve.getUserMail());
            preparedStatement.setString(i++, reserve.getUserCreditCard());
            preparedStatement.setString(i++, reserve.getStatus().toString());
            preparedStatement.setString(i++,
            		FormatUtils.calendarToStringSql(reserve.getReserveDate()));
            preparedStatement.setLong(i++, reserve.getReserveId());
      
            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows == 0) {
                throw new InstanceNotFoundException(reserve.getReserveId(),
                        Reserve.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    

    @Override
    public void remove(Connection connection, Long reserveId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM reserve WHERE" + " reserve_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, reserveId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(reserveId,
                        Reserve.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    
    
    
    
    
    
    
    
    
	
	
}
