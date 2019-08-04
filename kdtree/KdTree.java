import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

/**
 *
    ASSESSMENT SUMMARY

    Compilation:  PASSED
    API:          PASSED

    Spotbugs:     PASSED
    PMD:          PASSED
    Checkstyle:   PASSED

    Correctness:  35/35 tests passed
    Memory:       16/16 tests passed
    Timing:       42/42 tests passed

    Aggregate score: 100.00%

 * Created by yixu on 2017/1/18.
 * Edited by yixu on 2019/8/1.
 */
public class KdTree {
    
    private Node root;
    private int n = 0;

    // construct an empty set of points
    public KdTree()    {  }

    private static class Node {
        Point2D p;      // the point
        RectHV rect;    // the axis-aligned rectangle corresponding to this node
        Node lb;        // the left/bottom subtree
        Node rt;        // the right/top subtree
        final boolean isReverse;   // is that vertical or reverse(horizontal)
          
        Node(Point2D point, boolean isReverse) {
            this.p = point;
            this.isReverse = isReverse;
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // number of points in the set
    public int size() {
        return n;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        root = insert(root, 0, 0, 1, 1, p, false);
    }

    private Node insert(Node x, double xmin, double ymin, double xmax, double ymax, Point2D p, boolean isReverse) {
        if (x == null) {
            Node node = new Node(p, isReverse);
            node.rect = new RectHV(xmin, ymin, xmax, ymax);
            n++;
            return node;
        }

//        if (x.p.x() == p.x() && x.p.y() == p.y())   // points set, no dup
        if (Double.compare(x.p.x(), p.x()) == 0 && Double.compare(x.p.y(), p.y()) == 0)
            return x;

        if (isReverse) {
            if (p.y() < x.p.y()) {
                x.lb = insert(x.lb, xmin, ymin, xmax, x.p.y(), p, false);
            } else {
                x.rt = insert(x.rt, xmin, x.p.y(), xmax, ymax, p, false);
            }
        } else {
            if (p.x() < x.p.x()) {
                x.lb = insert(x.lb, xmin, ymin, x.p.x(), ymax, p, true);
            } else {
                x.rt = insert(x.rt, x.p.x(), ymin, xmax, ymax, p, true);
            }
        }

        return x;
    }

    // does the set contain point p?    
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return contains(root, p);    
    }
    
    private boolean contains(Node x, Point2D p) {
        if (x == null) 
            return false;
//        if (p.x() == x.p.x() && p.y() == x.p.y())
        if (Double.compare(x.p.x(), p.x()) == 0 && Double.compare(x.p.y(), p.y()) == 0)
            return true;

        boolean lb = x.isReverse ? (p.y() < x.p.y()) : (p.x() < x.p.x());
        if (lb)
            return contains(x.lb, p);
        else 
            return contains(x.rt, p);
    }

    // draw all points to standard draw
    public void draw()     {
        StdDraw.setScale(0, 1);
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.BLACK);
        new RectHV(0, 0, 1, 1).draw();  // draw container
        draw(root);
    }
    private void draw(Node x) {
        if (x == null)
            return;

        // line
        StdDraw.setPenRadius();
        if (x.isReverse) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        } else {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymax(), x.p.x(), x.rect.ymin());
        }

        // point
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        x.p.draw();

        draw(x.lb);
        draw(x.rt);
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect)   {
        if (rect == null)
            throw new IllegalArgumentException();

        Queue<Point2D> queue = new Queue<>();
        range(root, queue, rect);
        return queue;
    }
    private void range(Node x, Queue<Point2D> queue, RectHV rect) {
        if (x == null)
            return;
        if (rect.contains(x.p))
            queue.enqueue(x.p);

        boolean lb = x.isReverse ? (rect.ymin() < x.p.y()) : (rect.xmin() < x.p.x());
        boolean rt = x.isReverse ? (rect.ymax() >= x.p.y()) : (rect.xmax() >= x.p.x());

        if (lb) range(x.lb, queue, rect);
        if (rt) range(x.rt, queue, rect);
    }


    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (root == null)
            return null;
        return nearest(root.p, root, p);
    }

    private Point2D nearest(Point2D near, Node x, Point2D p) {
        if (x == null) 
            return near;
        if (x.p.distanceSquaredTo(p) < near.distanceSquaredTo(p))
            near = x.p;

        double nearestDistance = near.distanceSquaredTo(p);
        double lbDistance = Double.POSITIVE_INFINITY, rtDistance = Double.POSITIVE_INFINITY;
        if (x.lb != null)
            lbDistance = x.lb.rect.distanceSquaredTo(p);
        if (x.rt != null)
            rtDistance = x.rt.rect.distanceSquaredTo(p);

        if (lbDistance < rtDistance) {  // start from nearer
            if (lbDistance < nearestDistance)
                near = nearest(near, x.lb, p);
            if (rtDistance < near.distanceSquaredTo(p)) // nearestPoint may has updated
                near = nearest(near, x.rt, p);
        } else {
            if (rtDistance < nearestDistance)
                near = nearest(near, x.rt, p);
            if (lbDistance < near.distanceSquaredTo(p))
                near = nearest(near, x.lb, p);
        }

        return near;
    }
    

    public static void main(String[] args) {
//        KdTree kd = new KdTree();
//        Point2D p1 = new Point2D(0.5, 0.5);
//
//        StdOut.println(kd.isEmpty());
//        kd.insert(new Point2D(0.1, 0.4));
//        kd.insert(new Point2D(0.6, 0.4));
//        kd.insert(p1);
//        StdOut.println(kd.size());
//        kd.insert(p1);
//        kd.insert(new Point2D(0.2, 0.2));
//        StdOut.println(kd.size());
//        if (kd.contains(p1))
//            StdOut.println("Absolutly right");
////        kd.draw();
//        StdOut.println(kd.nearest(new Point2D(0.1, 0.1)));
    }   
}