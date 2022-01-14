package api;


public class geo implements GeoLocation {
    private double x;
    private double  y;
    private  double z;
    public geo(double x, double y, double z)
    {
     this.x=x;
     this.y=y;
     this.z=z;
    }

    public geo(String g) {
        String[] geos = g.split(",");
        this.x = Double.parseDouble(geos[0]);
        this.y = Double.parseDouble(geos[1]);
        this.z = Double.parseDouble(geos[2]);
    }

    public geo(GeoLocation g) {
        if (!(g instanceof geo))
            throw new ClassCastException();
        this.x = ((geo) g).x;
        this.y = ((geo) g).y;
        this.z = ((geo) g).z;
    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public double distance(GeoLocation g) {
        if(g instanceof geo) {
            geo G = (geo) g;
            return Math.sqrt(Math.pow(this.x - G.x, 2) + Math.pow(this.y - G.y, 2) + Math.pow(this.z - G.z, 2));
        }
        else  return -1;

    }
    public String toString() {
        return "My_GoeLocation{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
