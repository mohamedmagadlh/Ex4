package api;
/**
 * This interface represents a GeoLocation <x,y,z>, (aka Point3D data).
 *
 */
public interface GeoLocation {
    public double x();
    public double y();
    public double z();
    public double distance(GeoLocation g);
}