package model;

import java.util.Date;

public class Interval {

    private Date begin;
    private Date end;

    public Interval(Date begin, Date end) {
        this.begin = begin;
        this.end = end;
    }

    public boolean overlapCenterOf(Interval interval) {
        return this.begin.after(interval.begin) && this.end.before(interval.end);
    }

    public boolean overlapLeftSideOf(Interval interval) {
        return this.begin.before(interval.begin) && (between(this.end, interval) || this.end.equals(interval.begin));
    }

    public boolean overlapRightSideOf(Interval interval) {
        return this.end.after(interval.end) && (between(this.begin, interval) || this.begin.equals(interval.end));
    }

    private boolean between(Date date, Interval interval) {
        return date.after(interval.begin) && date.before(interval.end);
    }

    public boolean outOf(Interval interval) {
        return (this.end.before(interval.begin) || this.end.equals(interval.begin))
                || (this.begin.after(interval.end) || this.begin.equals(interval.end));
    }
}
