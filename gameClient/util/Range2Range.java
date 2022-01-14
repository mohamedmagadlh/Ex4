package gameClient.util;

import api.GeoLocation;


public class Range2Range {
    private Range2D _world, _frame;

    public Range2Range(Range2D w, Range2D f) {
        _world = new Range2D(w);
        _frame = new Range2D(f);
    }
    public GeoLocation world2frame(GeoLocation p) {
        point d = _world.getPortion(p);
        point ans = _frame.fromPortion(d);
        return ans;
    }
    public GeoLocation frame2world(GeoLocation p) {
        point d = _frame.getPortion(p);
        point ans = _world.fromPortion(d);
        return ans;
    }
    public Range2D getWorld() {
        return _world;
    }
    public Range2D getFrame() {
        return _frame;
    }
}
