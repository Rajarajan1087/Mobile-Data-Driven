package com.Stubs;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class StaticHolidays extends Holidays {
	
	private Set<Holiday> staticHolidays = new HashSet<Holiday>();

	public StaticHolidays() {
        staticHolidays.add(new Holiday(1, 1));
        staticHolidays.add(new Holiday(4, 18));
        staticHolidays.add(new Holiday(4, 21));
        staticHolidays.add(new Holiday(5, 5));
        staticHolidays.add(new Holiday(5, 26));
        staticHolidays.add(new Holiday(8, 25));
        staticHolidays.add(new Holiday(12, 25));
        staticHolidays.add(new Holiday(12, 26));
    }

    @Override
    public boolean contains(Date date) {
        Holiday staticHoliday = new Holiday(date);
        return staticHolidays.contains(staticHoliday);
    }

}
