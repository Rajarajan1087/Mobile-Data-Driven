package com.Stubs;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

	private final static Calendar calendar = Calendar.getInstance();
	private final static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	private final static Holidays staticHolidays = new StaticHolidays();


	public static int getBusinessDaysCount(String date1, String date2) {
		Date d1,d2;
		try {
			d1 = formatter.parse(date1);
			d2 = formatter.parse(date2);
		} catch (ParseException e) {
			throw new DateHelperRuntimeException(e);
		}

		if (!isWorkingDay(d1) || !isWorkingDay(d2)) {
			throw new DateHelperRuntimeException("Initial dates should both be working days");
		}

		int businessDaysCount = 0;

		if (d1.equals(d2)) {
			return businessDaysCount;
		}

		Date min = d1.before(d2) ? d1 : d2;
		Date max = min.equals(d2) ? d1 : d2;

		calendar.setTime(min);
		do {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			if (isWorkingDay(calendar.getTime())) {
				businessDaysCount++;
			}
		} while (calendar.getTime().before(max));

		return businessDaysCount;
	}
	
	public static String getNthBusinessDay(String date1, int numofDays) {
		Date d1;
		try {
			d1 = formatter.parse(date1);
		} catch (ParseException e) {
			throw new DateHelperRuntimeException(e);
		}

		/*if (!isWorkingDay(d1) || !isWorkingDay(d2)) {
			throw new DateHelperRuntimeException("Initial dates should both be working days");
		}*/

		int businessDaysCount = 0;

		calendar.setTime(d1);
		do {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			if (isWorkingDay(calendar.getTime())) {
				businessDaysCount++;
			}
		} while (businessDaysCount < numofDays);

		return formatter.format(calendar.getTime());
	}

	public static boolean isWorkingDay(Date date) {
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
			return false;
		} else if (staticHolidays.contains(date)) {
			return false;
		}
		return true;
	}

	public static class DateHelperRuntimeException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public DateHelperRuntimeException(String message) {
			super(message);
		}

		public DateHelperRuntimeException(Throwable e) {
			super(e);
		}

	}
}
