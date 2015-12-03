package root.ephem;


public class EphemObject implements Ephem {
    private int hour;
    private int minute;
    private int second;
    private double a;
    private double h;

    public EphemObject(int hour, int minute, int second, double a, double h) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.a = a;
        this.h = h;
    }

    public double getA(){
        return this.a;
    }

    public double getH(){
        return this.h;
    }

    public int getHour(){
        return this.hour;
    }

    public int getMinute(){
        return this.minute;
    }

    public int getSecond(){
        return this.second;
    }

    @Override
    public int hashCode(){
        int result = 17;
        result = 37*result+(hour - (hour >>> 32));
        result = 37*result+(second - (second >>> 32));
        result = 37*result+(minute - (minute >>> 32));
        return result;
    }

    public String getKey(){
        String h = fixStringForHash(hour);
        String m = fixStringForHash(minute);
        String s = fixStringForHash(second);
        return h + " " + m + " " + s;
    }

    private String fixStringForHash(int in){
        String r = String.valueOf(in);
        return r.length() > 1 ? r : (" " + r);
    }

    @Override
    public String toString(){
        return hour+" "+minute+" "+second+"   "+a+"   "+h;
    }
}
