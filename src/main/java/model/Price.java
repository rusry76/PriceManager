package model;

import java.util.Date;

public class Price {

    private long id;
    private String code;
    private int number;
    private int depart;
    private Date begin;
    private Date end;
    private long value;

    private Interval interval;

    public Price(String code, int number, int depart, Date begin, Date end, long value) {
        this.id = (long) (1 + Math.random() * Integer.MAX_VALUE);
        this.code = code;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;

        this.interval = new Interval(begin, end);
    }

    public Price(Price price, Date begin, Date end) {
        this(price.code, price.number, price.depart, begin, end, price.value);
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDepart() {
        return depart;
    }

    public void setDepart(int depart) {
        this.depart = depart;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public Interval getInterval() {
        return interval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Price price = (Price) o;

        if (number != price.number) return false;
        if (depart != price.depart) return false;
        if (value != price.value) return false;
        if (!code.equals(price.code)) return false;
        if (!begin.equals(price.begin)) return false;
        return end.equals(price.end);
    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + number;
        result = 31 * result + depart;
        result = 31 * result + begin.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + (int) (value ^ (value >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", number=" + number +
                ", depart=" + depart +
                ", begin=" + begin +
                ", end=" + end +
                ", value=" + value +
                "}\n";
    }
}
