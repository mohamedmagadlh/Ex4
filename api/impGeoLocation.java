package api;

public class  impGeoLocation implements GeoLocation {

    double _x, _y, _z;
    public  impGeoLocation(double x, double y, double z) {
        _x = x;
        _y = y;
        _z = z;
    }

    public  impGeoLocation(GeoLocation location) {
        this(location.x(), location.y(), location.z());
    }


    @Override
    public double distance(GeoLocation g) {
        double tempDis = Math.pow((_x - g.x()), 2) + Math.pow((_y - g.y()), 2) + Math.pow((_z - g.z()), 2);
        return Math.sqrt(tempDis);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        impGeoLocation that = ( impGeoLocation) o;
        return Double.compare(that._x, _x) == 0 &&
                Double.compare(that._y, _y) == 0 &&
                Double.compare(that._z, _z) == 0;
    }

    @Override
    public String toString() {
        return "Geo_locationImpl{" + _x + "," + _y + "," + _z + '}';
    }

    // Getters:

    @Override
    public double x() {
        return _x;
    }

    @Override
    public double y() {
        return _y;
    }

    @Override
    public double z() {
        return _z;
    }
}
