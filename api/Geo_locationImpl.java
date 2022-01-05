package api;

public class Geo_locationImpl implements geo_location {

    double _x, _y, _z;

    /**
     * Constructor with params
     *
     * @param x
     * @param y
     * @param z
     */
    public Geo_locationImpl(double x, double y, double z) {
        _x = x;
        _y = y;
        _z = z;
    }

    /**
     * Copy constructor
     *
     * @param location another geo_location
     */
    public Geo_locationImpl(geo_location location) {
        this(location.x(), location.y(), location.z());
    }

    /**
     * This method calculates the distance between two points in 3D space
     * uses the extended Pythagorean theorem
     *
     * @param g another geo_location point
     * @return distance between this to g
     */
    @Override
    public double distance(geo_location g) {
        double tempDis = Math.pow((_x - g.x()), 2) + Math.pow((_y - g.y()), 2) + Math.pow((_z - g.z()), 2);
        return Math.sqrt(tempDis);
    }

    /**
     * Equal method
     * returns true iff to points are equal,
     * i.e. each coordinate is equal to the corresponding coordinate in o
     *
     * @param o Object
     * @return true iff o is equal to this
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Geo_locationImpl that = (Geo_locationImpl) o;
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
