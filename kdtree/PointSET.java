import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.TreeSet;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Stack;

public class PointSET {
	
	private int N = 0;
	private TreeSet<Point2D> points;
	
    public         PointSET() {                             // construct an empty set of points 
		points = new TreeSet<Point2D>();
	}
    public           boolean isEmpty() { return points.size() == 0; }                      // is the set empty? 
    public               int size()    { return points.size(); }                        // number of points in the set 
    public              void insert(Point2D p)              // add the point to the set (if it is not already in the set)
	{
		if (p == null) throw new java.lang.NullPointerException();
		if (!points.contains(p))
			points.add(p);
	}
    public           boolean contains(Point2D p)            // does the set contain point p?
	{
		if (p == null) throw new java.lang.NullPointerException();
		return points.contains(p);
	}
    public              void draw()                         // draw all points to standard draw 
	{
		for (Point2D point : points)
			point.draw();
	}
    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle 	
		if (rect == null) throw new java.lang.NullPointerException();
		Stack<Point2D> stack = new Stack<Point2D>();
		for (Point2D point : points) {
			if (rect.contains(point))
				stack.push(point);
		}
		return stack;
	}
	
    public           Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 
	{
		if (p == null) throw new java.lang.NullPointerException();
		if (points.isEmpty()) return null;
		Point2D nearPoint = points.last();
		for (Point2D point : points) {
			if (point.distanceTo(p) < nearPoint.distanceTo(p))
				nearPoint = point;
		}
		return nearPoint;
	}

    public static void main(String[] args)  { }                // unit testing of the methods (optional) 
}
















