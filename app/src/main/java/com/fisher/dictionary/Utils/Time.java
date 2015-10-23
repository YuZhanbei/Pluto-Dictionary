package com.fisher.dictionary.Utils;

import java.util.Calendar;

public class Time {

	private Calendar cTime;
	private long lTime;
	private String sTime;//Length==23

	public Time () {
		this.cTime = Calendar.getInstance();
		this.fUpdateFromC();
	}
	public Time ( long lTime ) {
		this.lTime = lTime;
		this.cTime = Calendar.getInstance();
		cTime.setTimeInMillis( lTime );
		this.fUpdateFromC();
	}
	public Time ( Calendar c ) {
		this.cTime = c;
		this.fUpdateFromC();
	}
	public Time ( String s ) throws Exception {
		try {
			this.cTime = Calendar.getInstance();
			switch ( s.length() ) {
				case 23:
					cTime.set( Calendar.MILLISECOND, Integer.parseInt( s.substring( 20, 23 ) ) );
				case 19:
					cTime.set( Calendar.SECOND, Integer.parseInt( s.substring( 17, 19 ) ) );
				case 16:
					cTime.set( Calendar.MINUTE, Integer.parseInt( s.substring( 14, 16 ) ) );
					cTime.set( Calendar.HOUR_OF_DAY, Integer.parseInt( s.substring( 11, 13 ) ) );
				case 10:
					cTime.set( Calendar.DATE, Integer.parseInt( s.substring( 8, 10 ) ) );
				case 7:
					cTime.set( Calendar.MONTH, Integer.parseInt( s.substring( 5, 7 ) ) - 1 );
					cTime.set( Calendar.YEAR, Integer.parseInt( s.substring( 0, 4 ) ) );
					break;
			}
			this.fUpdateFromC();
		} catch ( Exception e ) {
			throw e;
		}
	}

	public long fGetLong () {
		return lTime;
	}
	public Calendar fGetCalendar () {
		return cTime;
	}
	public String fGetTime () {
		return sTime.substring( 0, 19 );
	}
	public String fGetYearMonth () {
		return sTime.substring( 0, 7 );
	}
	public String fGetHourMS () {
		return sTime.substring( 11, 19 );
	}
	public String fGetHourM () {
		return sTime.substring( 11, 16 );
	}
	public String fGetYearMD () {
		return sTime.substring( 0, 10 );
	}
	public String fGetFullTime () {
		return sTime;
	}
	public Time fToCurrentTime () {
		cTime = Calendar.getInstance();
		this.fUpdateFromC();
		return this;
	}
	public Time fToNextDay () {
		cTime.add( Calendar.DATE, 1 );
		this.fUpdateFromC();
		return this;
	}
	public Time fToPreviousDay () {
		cTime.add( Calendar.DATE, -1 );
		this.fUpdateFromC();
		return this;
	}
	public String fGetIntervalTime () {
		long l = lTime - System.currentTimeMillis();
		long ll = l >= 0 ? l : -l;
		return ( l > 0 ? "" : "-" ) + ( ll / ( 24 * 60 * 60 * 1000 ) % 30 > 0 ? ll / ( 24 * 60 * 60 * 1000 ) % 30 + " Day " : "" ) + ( ll / ( 60 * 60 * 1000 ) % 24 > 0 ? ll / ( 60 * 60 * 1000 ) % 24 + " h " : "" ) + ( ll / ( 60 * 1000 ) % 60 > 0 ? ll / ( 60 * 1000 ) % 60 + " mins " : "" ) + ll / 1000 % 60 + " s";
	}
	public String fGetIntervalTime ( boolean bType ) {
		long l = lTime - System.currentTimeMillis();
		long ll = l >= 0 ? l : -l;
		String sTemp = "";
		if ( bType )
			sTemp = ( l > 0 ? "" : "-" );
		return sTemp + ( ll / ( 24 * 60 * 60 * 1000 ) % 30 > 0 ? ll / ( 24 * 60 * 60 * 1000 ) % 30 + " Day " : "" ) + ( ll / ( 60 * 60 * 1000 ) % 24 > 0 ? ll / ( 60 * 60 * 1000 ) % 24 + " h " : "" ) + ( ll / ( 60 * 1000 ) % 60 > 0 ? ll / ( 60 * 1000 ) % 60 + " mins " : "" ) + ll / 1000 % 60 + " s";
	}
	public boolean fIsPassed () {
		if ( ( lTime - System.currentTimeMillis() ) < 0 ) {
			return true;
		}
		return false;
	}
	public long fGetInterval () {
		return lTime - System.currentTimeMillis();
	}
	public String fGetWeekString () {
		switch ( cTime.get( Calendar.DAY_OF_WEEK ) ) {
			case 1:
				return "Sunday";
			case 2:
				return "Monday";
			case 3:
				return "Thesday";
			case 4:
				return "Wednesday";
			case 5:
				return "Tursday";
			case 6:
				return "Friday";
			case 7:
				return "Saturday";
			default:
				return "DeadDay";
		}
	}


	private void fUpdateFromC () {
		this.lTime = cTime.getTimeInMillis();
		this.sTime = this.fGetTimeString();
	}

	//2015-04-04 12:25:47:345;
	private String fGetTimeString () {
		String sMonth = String.valueOf( cTime.get( Calendar.MONTH ) + 1 );
		String sDay = String.valueOf( cTime.get( Calendar.DATE ) );
		String sHour = String.valueOf( cTime.get( Calendar.HOUR_OF_DAY ) );
		String sMin = String.valueOf( cTime.get( Calendar.MINUTE ) );
		String sSec = String.valueOf( cTime.get( Calendar.SECOND ) );
		String sMSec = String.valueOf( cTime.get( Calendar.MILLISECOND ) );
		sMSec = sMSec.length() == 3 ? sMSec : "0" + sMSec;
		return cTime.get( Calendar.YEAR )
				+ "-" + ( sMonth.length() > 1 ? sMonth : ( "0" + sMonth ) )
				+ "-" + ( sDay.length() > 1 ? sDay : ( "0" + sDay ) )
				+ " " + ( sHour.length() > 1 ? sHour : ( "0" + sHour ) )
				+ ":" + ( sMin.length() > 1 ? sMin : ( "0" + sMin ) )
				+ ":" + ( sSec.length() > 1 ? sSec : ( "0" + sSec ) )
				+ ":" + ( sMSec.length() == 3 ? sMSec : ( "0" + sMSec ) );
	}
}