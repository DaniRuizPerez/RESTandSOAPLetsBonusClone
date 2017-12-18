package es.udc.ws.app.model.offer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import es.udc.ws.app.format.FormatUtils;

public class Jdbc3CcSqlOfferDao extends AbstractSqlOfferDao {

    @Override
    public Offer create(Connection connection, Offer offer) {

        /* Create "queryString". */
    	
        String queryString = "INSERT INTO offer"
                + " (offer_name,offer_description, offer_price, " +
                "offer_reduced_price,offer_max_reserves,offer_ini_date_reserve, " +
		        "offer_end_date_reserve, offer_end_date_claim) " +
		        "VALUES (?, ?, ?, ?, ?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

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
            preparedStatement.setString(i++, FormatUtils.calendarToStringSql(offer.getStartDate()));
            preparedStatement.setString(i++, FormatUtils.calendarToStringSql(offer.getEndDate()));
            preparedStatement.setString(i++, FormatUtils.calendarToStringSql(offer.getEnjoyDate()));

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long offerId = resultSet.getLong(1);

            /* Return offer. */
            return new Offer(offerId,offer.getName(), offer.getDescription(), offer.getRealPrice(), 
            		offer.getDiscountedPrice(), offer.getNumberOfReserves(), offer.getClaimedReserves(),
            		offer.getMaxPeople(),offer.getStartDate(), offer.getEndDate(), offer.getEnjoyDate(),
            		offer.getStatus());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
