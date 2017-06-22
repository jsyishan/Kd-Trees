import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;


/**
 * Created by eugen on 6/15/17.
 */

public class PointSET {

    private SET<Point2D> points;

    public PointSET () { points = new SET<>(); }

    public boolean isEmpty () { return points.isEmpty (); }

    public int size () { return points.size(); }

    public void insert (Point2D p) {
        if (p == null) { throw new NullPointerException (); }
        points.add (p);
    }

    public boolean contains (Point2D p) {
        if (p == null) { throw new NullPointerException (); }
        return points.contains (p);
    }

    public void draw () {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : points) { p.draw(); }
    }

    public Iterable<Point2D> range (RectHV rect) {
        if (rect == null) { throw new NullPointerException (); }

        SET<Point2D> rangePoints = new SET<> ();
        for (Point2D p : points) {
            if (rect.contains (p)) {
                rangePoints.add (p);
            }
        }
        return rangePoints;
    }

    public Point2D nearest (Point2D p) {
        if (p == null) { throw new NullPointerException (); }

        double nearest = Double.MAX_VALUE;
        Point2D nearestPoint = null;
        for (Point2D _p : points) {
            if (_p.distanceSquaredTo (p) < nearest) {
                nearest = _p.distanceSquaredTo (p);
                nearestPoint = _p;
            }
        }
        return nearestPoint;
    }

    public static void main (String[] args) {
//        PointSET pointSET = new PointSET ();
//        pointSET.insert (new Point2D (0.1, 0.1));
//        pointSET.insert (new Point2D (0.5, 0.3));
//        pointSET.insert (new Point2D (0.2, 0.3));
//        pointSET.insert (new Point2D (0.4, 0.7));
//        pointSET.draw ();

    }
}
