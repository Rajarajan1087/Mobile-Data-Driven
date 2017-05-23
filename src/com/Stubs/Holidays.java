package com.Stubs;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public abstract class Holidays {
	
	protected final static Calendar calendar = Calendar.getInstance();

    protected static class Holiday {

        private int month;
        private int day;

        protected Holiday(int month, int day) {
            this.month = month;
            this.day = day;
        }

        protected Holiday(Date date) {
            calendar.setTime(date);
            this.month = calendar.get(Calendar.MONTH) + 1;
            this.day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Holiday && obj.hashCode() == hashCode();
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(new Object[] { month, day });
        }

    }

    public abstract boolean contains(Date date);

}
