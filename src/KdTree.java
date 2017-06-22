import edu.princeton.cs.algs4.*;

import java.awt.*;

/**
 * Created by eugen on 6/15/17.
 */

public class KdTree {

    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;

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

    private int size;
    private Node root;

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

    private void draw (Node x) {
        if (x == null) { return; }


    }

    public void draw () {
        StdDraw.setPenColor (StdDraw.BLACK);
        StdDraw.setPenRadius (0.01);
        root.p.draw ();
        StdDraw.setPenColor (StdDraw.RED);
        StdDraw.setPenRadius ();
        StdDraw.line (root.p.x (), 1.0, root.p.x (), 0.0);
    }

    public Iterable<Point2D> range (RectHV rect) {
        SET<Point2D> points = new SET<> ();


        return points;
    }

    private Node nearest (Node x, Point2D p, boolean orientation, Double nearest) {
        if (x == null) { return null; }
        if (x.p.equals (p)) { return x; }

//        if (orientation == VERTICAL) {
//            if (nearest >= x.p.distanceSquaredTo (p)) {
//                int cmp = Double.compare (x.lb.p.distanceSquaredTo (p), x.rt.p.distanceSquaredTo (p));
//                if (cmp < 0) {
//                    x = nearest (x.lb, p, HORIZONTAL, x.lb.p.distanceSquaredTo (p));
//                } else {
//                    x = nearest (x.rt, p, HORIZONTAL, x.rt.p.distanceSquaredTo (p));
//                }
//            }
//        } else {
//            if (nearest >= x.p.distanceSquaredTo (p)) {
//                int cmp = Double.compare (x.lb.p.distanceSquaredTo (p), x.rt.p.distanceSquaredTo (p));
//                if (cmp < 0) {
//                    x = nearest (x.lb, p, VERTICAL, x.lb.p.distanceSquaredTo (p));
//                } else {
//                    x = nearest (x.rt, p, VERTICAL, x.rt.p.distanceSquaredTo (p));
//                }
//            }
//        }
//        return x;

    }

    public Point2D nearest (Point2D p) {
        if (p == null) { throw new NullPointerException (); }

        return nearest (root, p, VERTICAL, Double.MAX_VALUE).p;

    }

    public static void main (String[] args) {

        KdTree kdTree = new KdTree ();
        kdTree.insert (new Point2D (0.7, 0.2));
        kdTree.insert (new Point2D (0.5,0.4));
        kdTree.insert (new Point2D (0.2,0.3));
        kdTree.insert (new Point2D (0.4,0.7));
        kdTree.insert (new Point2D (0.9,0.6));
//        kdTree.insert (new Point2D (0.9,0.6));
//        kdTree.insert (new Point2D (0.4,0.7));
//        StdOut.println (kdTree.contains (new Point2D (0.9,0.6)));
//        StdOut.println (kdTree.contains (new Point2D (0.9, 0.5)));
        StdOut.println (kdTree.nearest (new Point2D (0.7, 0.1)));
        //kdTree.draw ();
        kdTree.insert (new Point2D (0.7,0.7));

    }
}
