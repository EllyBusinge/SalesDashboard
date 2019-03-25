package org.chai.client.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class DateUtil {
	private static final String D_M_YYYY = "d-M-yyyy";
	private static final String YYYY_M_D = "yyyy-M-d";
	private static final String DATE_SEPARATOR = "-";

	public static Date getDate( Integer dd, Integer mm, Integer yyyy )
	{
		if ( dd == null || mm == null || yyyy == null )
			return null;
		Date retVal = null;
		try
		{
			retVal = DateTimeFormat.getFormat( D_M_YYYY ).parse( dd + DATE_SEPARATOR + mm + DATE_SEPARATOR + yyyy );
		}
		catch ( Exception e )
		{
			retVal = null;
		}

		return retVal;
	}

	public static String getDateStr (Date date) {
		Integer dt = Integer.parseInt(DateTimeFormat.getFormat( D_M_YYYY ).format( date ).split( DATE_SEPARATOR )[0]);
		Integer month = Integer.parseInt(DateTimeFormat.getFormat( D_M_YYYY ).format( date ).split( DATE_SEPARATOR )[1]);
		
		String fmDate = "";
		String fmMonth = "";
		
		if (dt < 10) {
			fmDate = "0" + dt;
		} else {
			fmDate = dt.toString();
		}
		
		if (month < 10) {
			fmMonth = "0" + month;
		} else {
			fmMonth = dt.toString();
		}
		return DateTimeFormat.getFormat( D_M_YYYY ).format( date ).split( DATE_SEPARATOR )[2] + "-" + fmMonth + "-" + fmDate;
	}
	
	public static String getDateStr2 (Date date) {
		return DateTimeFormat.getFormat( YYYY_M_D ).format( date ).split( DATE_SEPARATOR )[2] + "-" 
		+ DateTimeFormat.getFormat( YYYY_M_D ).format( date ).split( DATE_SEPARATOR )[1] + "-" 
		+ DateTimeFormat.getFormat( YYYY_M_D ).format( date ).split( DATE_SEPARATOR )[0];
	}

	public static String getDayAsString( Date date )
	{
		return ( date == null ) ? null : DateTimeFormat.getFormat( D_M_YYYY ).format( date ).split( DATE_SEPARATOR )[0];
	}

	public static String getMonthAsString( Date date )
	{
		return ( date == null ) ? null : DateTimeFormat.getFormat( D_M_YYYY ).format( date ).split( DATE_SEPARATOR )[1];
	}

	public static String getYearAsString( Date date ) {
		return ( date == null ) ? null : DateTimeFormat.getFormat( D_M_YYYY ).format( date ).split( DATE_SEPARATOR )[2];
	}

	public static boolean isValidDate( Integer dd, Integer mm, Integer yyyy ) {
		boolean isvalidDate = true;

		try {
			String transformedInput = DateTimeFormat.getFormat( D_M_YYYY ).format( getDate( dd, mm, yyyy ) );
			String originalInput = dd + DATE_SEPARATOR + mm + DATE_SEPARATOR + yyyy;

			isvalidDate = transformedInput.equals( originalInput );
		}
		catch ( Exception e ) {
			isvalidDate = false;
		}

		return isvalidDate;
	}
	
	public static String getFormattedDate(Date date) {
		return DateTimeFormat.getFormat("YYYY-MM-DD h:m:s a").format(date);
	}
}
