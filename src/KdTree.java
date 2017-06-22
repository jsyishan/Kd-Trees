import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;


/**
 * Created by eugen on 6/15/17.
 */

public class KdTree {

    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;
    private static final RectHV RECT = new RectHV (0, 0, 1, 1);

    private int size;
    private Node root;

    private static class Node {
        private Point2D p;
        //private RectHV rect;
        private Node lb;
        private Node rt;
        private boolean orientation;

        public Node(Point2D p, boolean orientation) {
            this.p = p;
            this.orientation = orientation;
            lb = null;
            rt = null;
        }
    }


    public KdTree () {
        root = null;
        size = 0;
    }

    public boolean isEmpty () { return size == 0; }

    public int size () { return size; }

    private Node insert (Node h, Point2D p, boolean orientation) {

        if (h == null) {
            size++;
            return new Node (p, orientation);
        }

        if (h.p.equals (p)) { return h; }

        if (orientation == VERTICAL) {
            int cmp = Double.compare (p.x (), h.p.x ());
            if (cmp < 0) { h.lb = insert (h.lb, p, HORIZONTAL); }
            else { h.rt = insert (h.rt, p, HORIZONTAL); }
        } else {
            int cmp = Double.compare (p.y (), h.p.y ());
            if (cmp < 0) { h.lb = insert (h.lb, p, VERTICAL); }
            else { h.rt = insert (h.rt, p, VERTICAL); }
        }
        return h;

    }

    public void insert (Point2D p) {
        if (p == null) { throw new NullPointerException (); }

        root = insert (root, p, VERTICAL);
    }

    private boolean contains (Node x, Point2D p, boolean orientation) {
        if (x == null) { return false; }
        if (x.p.equals (p)) { return true; }

        if (orientation == VERTICAL) {
            int cmp = Double.compare (p.x (), x.p.x ());
            if (cmp < 0) { return contains (x.lb, p, HORIZONTAL); }
            else { return contains (x.rt, p, HORIZONTAL); }
        } else {
            int cmp = Double.compare (p.y (), x.p.y ());
            if (cmp < 0) { return contains (x.lb, p, VERTICAL); }
            else { return contains (x.rt, p, VERTICAL); }
        }
    }

    public boolean contains (Point2D p) {
        if (p == null) { throw new NullPointerException (); }

        return contains (root, p, VERTICAL);
    }

    private RectHV lbRect (final Node node, final RectHV rect) {
        if (node.orientation == VERTICAL) {
            return new RectHV (rect.xmin (), rect.ymin (), node.p.x (), rect.ymax ());
        } else {
            return new RectHV (rect.xmin (), rect.ymin (), rect.xmax (), node.p.y ());
        }
    }

    private RectHV rtRect (final Node node, final RectHV rect) {
        if (node.orientation == VERTICAL) {
            return new RectHV (node.p.x (), rect.ymin (), rect.xmax (), rect.ymax ());
        } else {
            return new RectHV (rect.xmin (), node.p.y (), rect.xmax (), rect.ymax ());
        }
    }

    private void draw (Node x, RectHV rect) {
        if (x == null) { return; }

        StdDraw.setPenColor (StdDraw.BLACK);
        StdDraw.setPenRadius (0.01);
        x.p.draw ();

        StdDraw.setPenRadius ();
        if (x.orientation == VERTICAL) {
            StdDraw.setPenColor (StdDraw.RED);
            new Point2D (x.p.x (), rect.ymin ()).drawTo (new Point2D (x.p.x (), rect.ymax ()));
        } else {
            StdDraw.setPenColor (StdDraw.BLUE);
            new Point2D (rect.xmin (), x.p.y ()).drawTo (new Point2D (rect.xmax (), x.p.y ()));
        }

        draw (x.lb, lbRect (x, rect));
        draw (x.rt, rtRect (x, rect));
    }

    public void draw () {
        StdDraw.setPenColor (StdDraw.BLACK);
        StdDraw.setPenRadius ();
        RECT.draw ();

        draw (root, RECT);
    }

    private void range (Node x, RectHV tarRect, RectHV curRect, Queue<Point2D> points) {
        if (x == null) { return; }

        if (tarRect.contains (x.p)) { points.enqueue (x.p); }
        if (tarRect.intersects (lbRect (x, curRect))) {
            range (x.lb, tarRect, lbRect (x, curRect), points);
        }
        if (tarRect.intersects (rtRect (x, curRect))) {
            range (x.rt, tarRect, rtRect (x, curRect), points);
        }
    }

    public Iterable<Point2D> range (RectHV rect) {
        if (rect == null) { throw new NullPointerException (); }

        Queue<Point2D> points = new Queue<> ();
        range (root, rect, RECT, points);
        return points;
    }


    private Point2D nearest (Node x, Point2D tarPoint, Point2D curPoint, RectHV rect) {
        if (x == null) { return curPoint; }

        double nearestCur = Double.MAX_VALUE;
        if (curPoint != null) {
            nearestCur = tarPoint.distanceSquaredTo (curPoint);
            if (nearestCur < rect.distanceSquaredTo (tarPoint)) {
                return curPoint;
            }
        }

        double nearestNext = tarPoint.distanceSquaredTo (x.p);
        Point2D nearest = (nearestNext < nearestCur) ? x.p : curPoint;

        RectHV lb = lbRect (x, rect);
        RectHV rt = rtRect (x, rect);

        if ((x.orientation == VERTICAL && tarPoint.x () < x.p.x ()) ||
                (x.orientation == HORIZONTAL && tarPoint.y () < x.p.y ())) {
            nearest = nearest (x.lb, tarPoint, nearest, lb);
            nearest = nearest (x.rt, tarPoint, nearest, rt);
        } else {
            nearest = nearest (x.rt, tarPoint, nearest, rt);
            nearest = nearest (x.lb, tarPoint, nearest, lb);
        }

        return nearest;

    }


    public Point2D nearest (Point2D p) {
        if (p == null) { throw new NullPointerException (); }

        return nearest (root, p, null, RECT);
    }

    public static void main (String[] args) {

//        KdTree kdTree = new KdTree ();
//        kdTree.insert (new Point2D (0.7, 0.2));
//        kdTree.insert (new Point2D (0.5,0.4));
//        kdTree.insert (new Point2D (0.2,0.3));
//        kdTree.insert (new Point2D (0.4,0.7));
//        kdTree.insert (new Point2D (0.9,0.6));
//        kdTree.draw ();
//        StdDraw.setPenColor (StdDraw.YELLOW);
//        StdDraw.setPenRadius (0.007);
//        RectHV rect = new RectHV (0.1, 0.2, 0.6, 0.9);
//        rect.draw ();
//        StdOut.println (kdTree.range (rect));
////        kdTree.insert (new Point2D (0.9,0.6));
////        kdTree.insert (new Point2D (0.4,0.7));
////        StdOut.println (kdTree.contains (new Point2D (0.9,0.6)));
////        StdOut.println (kdTree.contains (new Point2D (0.9, 0.5)));
//        StdOut.println (kdTree.nearest (new Point2D (0.7, 0.1)));
//        //kdTree.draw ();
//        kdTree.insert (new Point2D (0.7,0.7));

    }
}
