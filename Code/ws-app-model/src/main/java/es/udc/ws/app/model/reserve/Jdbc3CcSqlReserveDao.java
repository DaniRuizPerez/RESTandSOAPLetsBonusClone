package es.udc.ws.app.model.reserve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import es.udc.ws.app.format.FormatUtils;


public class Jdbc3CcSqlReserveDao extends AbstractSqlReserveDao {

	 @Override
	    public Reserve create(Connection connection, Reserve reserve) {

	        /* Create "queryString". */
		 
	        String queryString = "INSERT INTO reserve"
	                + " (reserve_offer_id,reserve_user_mail, reserve_user_creditcard," +
	                "reserve_date) " +
	                "VALUES (?, ?, ?, ?)";

	        try (PreparedStatement preparedStatement = connection.prepareStatement(
	                        queryString, Statement.RETURN_GENERATED_KEYS)) {

	            /* Fill "preparedStatement". */
	            int i = 1;
	            preparedStatement.setLong(i++, reserve.getOfferId());
	            preparedStatement.setString(i++, reserve.getUserMail());
	            preparedStatement.setString(i++, reserve.getUserCreditCard());
	            preparedStatement.setString(i++,
	            		FormatUtils.calendarToStringSql(reserve.getReserveDate()));
	            
	            /* Execute query. */
	            preparedStatement.executeUpdate();

	            /* Get generated identifier. */
	            ResultSet resultSet = preparedStatement.getGeneratedKeys();

	            if (!resultSet.next()) {
	                throw new SQLException(
	                        "JDBC driver did not return generated key.");
	            }
	            
	            Long reserveId = resultSet.getLong(1);

	            /* Return offer. */
	            return new Reserve(reserveId, reserve.getOfferId(), reserve.getUserMail(),
	            		reserve.getUserCreditCard(), reserve.getStatus(), 
	            		reserve.getReserveDate());

	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        }

	    }	

	
}
