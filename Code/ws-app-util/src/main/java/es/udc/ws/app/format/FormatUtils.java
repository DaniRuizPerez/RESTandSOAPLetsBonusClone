package es.udc.ws.app.format;

import java.security.Timestamp;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class FormatUtils {

	public static String calendarToStringSql(Calendar date) {
		if (date ==null)
			return null;
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH)+1;
		int day = date.get(Calendar.DAY_OF_MONTH);
		int hour = date.get(Calendar.HOUR_OF_DAY);
		int min = date.get(Calendar.MINUTE);
		int sec = date.get(Calendar.SECOND);
		return year + "-" + month + "-" + day + " "+ hour + ":" + min + ":" + sec;
	}
	
	public static String calendarToStringXML (Calendar date) {
		if (date ==null)
			return null;
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH)+1;
		int day = date.get(Calendar.DAY_OF_MONTH);
		int hour = date.get(Calendar.HOUR_OF_DAY);
		int min = date.get(Calendar.MINUTE);
		return day + "-" + month + "-" + year + " "+ String.format("%02d",hour) + ":" + 
			String.format("%02d", min);
	}
	
	public static String calendarToStringXML (Calendar date, String s) {
		//s is the separator (year"s"month"s"day)
		if (date ==null)
			return null;
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH)+1;
		int day = date.get(Calendar.DAY_OF_MONTH);
		int hour = date.get(Calendar.HOUR_OF_DAY);
		int min = date.get(Calendar.MINUTE);
		return day + s + month + s + year + " "+ String.format("%02d",hour) + ":" + 
			String.format("%02d", min);
	}
	
	public static XMLGregorianCalendar calendarToXMLGregorianCalendar(Calendar c) 
			throws DatatypeConfigurationException {
		DatatypeFactory parser = DatatypeFactory.newInstance();
    	GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(c.getTime());
    	return parser.newXMLGregorianCalendar(gc);
	}
	
	public static Calendar XMLGregorianCalendarToCalendar (XMLGregorianCalendar c) {
		return c.toGregorianCalendar(TimeZone.getDefault(), Locale.getDefault(), c);
	}
	
	public static Calendar parseCalendar (String date) throws ParseException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		df.parse(date);
		return df.getCalendar();
	}
}
