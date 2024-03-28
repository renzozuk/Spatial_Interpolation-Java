package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;

public class MomentIterator implements Iterator<Calendar> {
    private Calendar current;
    private Calendar end;

    public MomentIterator(String year) {
        this("01/01/" + year, "31/12/" + year);
    }

    public MomentIterator(String start, String end) {
        TimeZone timeZone = TimeZone.getTimeZone("Etc/GMT");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        this.current = Calendar.getInstance();
        this.current.setTimeZone(timeZone);
        try {
            this.current.setTime(simpleDateFormat.parse(start));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.current.set(Calendar.HOUR_OF_DAY, 0);
        this.current.set(Calendar.MINUTE, 0);
        this.current.set(Calendar.SECOND, 0);
        this.current.set(Calendar.MILLISECOND, 0);

        this.end = Calendar.getInstance();
        this.end.setTimeZone(timeZone);
        try {
            this.end.setTime(simpleDateFormat.parse(end));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.end.set(Calendar.HOUR_OF_DAY, 23);
        this.end.set(Calendar.MINUTE, 59);
        this.end.set(Calendar.SECOND, 59);
        this.end.set(Calendar.MILLISECOND, 59);
    }

    @Override
    public boolean hasNext(){
        return current.before(end);
    }

    @Override
    public Calendar next(){
        Calendar result = (Calendar) current.clone();
        current.add(Calendar.HOUR_OF_DAY, 1);
        return result;
    }
}
