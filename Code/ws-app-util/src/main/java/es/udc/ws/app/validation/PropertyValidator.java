package es.udc.ws.app.validation;


import java.math.BigInteger;
import java.util.Calendar;

import es.udc.ws.util.exceptions.InputValidationException;

public final class PropertyValidator {

    private PropertyValidator() {}

    public static void validateInteger(String propertyName,
            Integer value)
            throws InputValidationException {
    	if (value != null)
	        if (value < 1) {
	            throw new InputValidationException("Invalid " + propertyName +
	                    " value (it must be greater greater than zero or null): " + value);
	        }

    }

    public static void validateNotNegativeInt(String propertyName,
            int intValue) throws InputValidationException {

        if (intValue < 0) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be greater than 0): " +	intValue);
        }

    }
    
    public static void validateNotNegativeOrNullLong(String propertyName,
            Long longValue) throws InputValidationException {

        if (longValue == null) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be greater than 0 and not null): " +	longValue);
        } else if (longValue < 0) 
        	throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be greater than 0 and not null): " +	longValue);
    }
    
    public static void validateNotNegativeLong(String propertyName,
            long longValue) throws InputValidationException {

        if (longValue < 0) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be greater than 0): " +	longValue);
        }

    }
    
    public static void validateNotNegativeFloat(String propertyName,
            float floatValue, float floatUpperLimit) throws InputValidationException {

        if ((floatValue < 0) ||
        		floatValue >= floatUpperLimit){
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be greater than 0 and lower than "+floatUpperLimit+"): " +	floatValue);
        }

    }

    public static void validatePositiveFloat(String propertyName,
            float floatValue) throws InputValidationException {

        if (floatValue <= 0) {
            throw new InputValidationException("Invalid " + propertyName +
                    "value (it must be greater than 0):" + floatValue);
        }

    }

    public static void validateMandatoryString(String propertyName,
            String stringValue) throws InputValidationException {

        if ( (stringValue == null) || (stringValue.length() == 0) ) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it cannot be null neither empty): " +
                    stringValue);
        }

    }

    public static void validatePastDate(String propertyName,
            Calendar propertyValue) throws InputValidationException {

        Calendar now = Calendar.getInstance();
        if ( (propertyValue == null) || (propertyValue.after(now)) ) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (it must be a past date): " +
                    propertyValue);
        }

    }

	public static void validateCalendarNotNull(String propertyName, 
			Calendar calendar) throws InputValidationException {
		if (calendar == null) {
            throw new InputValidationException("Invalid " + propertyName +
                    " value (expected not null param): " +
                    calendar);
		
	}

}

	public static void validateDateLower(String firstDate, Calendar cFirstDate,
			String secondDate, Calendar cSecondDate) throws InputValidationException {
		if (cFirstDate.after(cSecondDate)) {
            throw new InputValidationException("Invalid dates:" +firstDate  +
                    "should be lower than "+secondDate);
		
		}
	}

	public static void validateStatus(String propertyName, String status,
			int claimedReserves, int numberOfReserves) throws InputValidationException {
		/*Created: claimed and reserved = 0
    	 *Commited: claimed < reserved != 0
    	 *Released: claimed == reserved = 0*/
		if (propertyName.equals("CREATED")) {
			if (claimedReserves != 0) {
				throw new InputValidationException("Invalid status " +status  +
	                    ":claimedReserves must be 0");
			}
			else if (numberOfReserves != 0) {
				throw new InputValidationException("Invalid status " +status  +
	                    ":numberOfReserves must be 0");
			}
		} 
		else if (propertyName.equals("COMMITED")) {
			if (claimedReserves == 0) {
				throw new InputValidationException("Invalid status " +status  +
	                    ":claimedReserves must be greater than 0");
			}
			else if (numberOfReserves == 0) {
				throw new InputValidationException("Invalid status " +status  +
	                    ":numberOfReserves must be greater 0");
			}
			else if (claimedReserves >= numberOfReserves) {
				throw new InputValidationException("Invalid status " +status  +
	                    ":claimedReserves must be lower than numberOfReserves");
			}
		}
		else if (propertyName.equals("RELEASED")) {
			if (claimedReserves == 0) {
				throw new InputValidationException("Invalid status " +status  +
	                    ":claimedReserves must be greater than 0");
			}
			else if (numberOfReserves == 0) {
				throw new InputValidationException("Invalid status " +status  +
	                    ":numberOfReserves must be greater 0");
			}
			else if (claimedReserves != numberOfReserves) {
				throw new InputValidationException("Invalid status " +status  +
	                    ":claimedReserves must be equal than numberOfReserves");
			}
		} 
	}

    public static void validateCreditCard(String propertyValue)
            throws InputValidationException {

        boolean validCreditCard = true;
        if ( (propertyValue != null) && (propertyValue.length() == 16) ) {
            try {
                new BigInteger(propertyValue);
            } catch (NumberFormatException e) {
                validCreditCard = false;
            }
        } else {
            validCreditCard = false;
        }
        if (!validCreditCard) {
            throw new InputValidationException("Invalid credit card number" +
                    " (it should be a sequence of 16 numeric digits): " +
                    propertyValue);
        }

    }

}