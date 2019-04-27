import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;


public class KdTree {
	
	private Node root;
	private int N = 0;
	private	Point2D nearPoint;
	private static final RectHV CONTAINER = new RectHV(0, 0, 1, 1); 
	
    public         KdTree()	{  }               // construct an empty set of points 
	private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
		private boolean revs;   // is that vertical or reverse(horizontal)   
  		
		public Node(Point2D point, boolean isReverse)
		{  p = point; revs = isReverse;  }
	}
	
    public boolean isEmpty() { return N == 0; }                            // is the set empty? 
    public int     size()    { return N;      }                            // number of points in the set
	
	public void insert(Point2D p)           // add the point to the set (if it is not already in the set)
	{   root = insert(root, p, false);  root.rect = new RectHV(0, 0, 1, 1);  }
	private Node insert(Node x, Point2D p, boolean isReverse) {
		if (x == null) {
			N++;
			return new Node(p, isReverse);
		}
		if (p.x() == x.p.x() && p.y() == x.p.y()) return x;
		double cmp = p.x() - x.p.x();
	    if (isReverse) cmp = p.y() - x.p.y();
		if (cmp < 0) {
		    if (x.lb == null) {
			    x.lb = insert(x.lb, p, !isReverse);
				if (isReverse) x.lb.rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.p.y());
				else x.lb.rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.p.x(), x.rect.ymax());
			}else x.lb = insert(x.lb, p, !isReverse);			
		}else {
			if (x.rt == null) {
			    x.rt = insert(x.rt, p, !isReverse);
				if (isReverse) x.rt.rect = new RectHV(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.rect.ymax());
				else x.rt.rect = new RectHV( x.p.x(),x.rect.ymin(), x.rect.xmax(), x.rect.ymax());
			}else x.rt = insert(x.rt, p, !isReverse);
		}
		return x;
	}
		
    public           boolean contains(Point2D p)            // does the set contain point p?
	{	return contains(root, p);	}
	private boolean contains(Node x, Point2D p) {
		if (x == null) return false;
		if (p.x() == x.p.x() && p.y() == x.p.y()) return true;
		double cmp = p.x() - x.p.x();
		if (x.revs) cmp = p.y() - x.p.y();
		if (cmp < 0) return contains(x.lb, p);
		else return contains(x.rt, p);
	}
		
    public              void draw() 	{                        // draw all points to standard draw
		StdDraw.setScale(0, 1);        
        StdDraw.setPenColor(StdDraw.BLACK);    
        StdDraw.setPenRadius();    
        CONTAINER.draw(); 
		draw(root);
	}
	private void draw(Node x) {
		if (x == null) return;
		draw(x.lb);
		Point2D min, max;
		if (x.revs) {
			StdDraw.setPenColor(StdDraw.BLUE);
		    min = new Point2D(x.rect.xmin(), x.p.y());    
            max = new Point2D(x.rect.xmax(), x.p.y()); 
		}else {
		    StdDraw.setPenColor(StdDraw.RED);
			min = new Point2D(x.p.x(), x.rect.ymin());    
            max = new Point2D(x.p.x(), x.rect.ymax());
		}
		StdDraw.setPenRadius();
		min.drawTo(max);
		StdDraw.setPenRadius(0.01);
		StdDraw.setPenColor(StdDraw.BLACK);
		x.p.draw();
		draw(x.rt);
	}		
		
    public Iterable<Point2D> range(RectHV rect)   {        // all points that are inside the rectangle 
	    Queue<Point2D> queue = new Queue<Point2D>();
		range(root, queue, rect);
		return queue;
	}
	private void range(Node x, Queue<Point2D> queue, RectHV rect) {
		if (x == null) return;
		double cmplo = rect.xmin() - x.p.x();
		double cmphi = rect.xmax() - x.p.x();
		if (x.revs) {
			cmplo = rect.ymin() - x.p.y();
			cmphi = rect.ymax() - x.p.y();			
		}
		if (rect.contains(x.p)) queue.enqueue(x.p);
		if (cmplo <= 0) range(x.lb, queue, rect);
		if (cmphi >= 0) range(x.rt, queue, rect);		
	}
	
    public           Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 
	{   
	    if (root == null) return null;
	    else nearPoint = root.p;
		return nearest(root, p);
	}
	private Point2D nearest(Node x, Point2D p) {
		if (x == null) return nearPoint;
		/*if (x.p.distanceTo(p) <= nearPoint.distanceTo(p)){
			nearPoint = x.p;
			if (x.lb != null) nearPoint = nearest(x.lb, p);
		    if (x.rt != null) nearPoint = nearest(x.rt, p);
		}else {
			double cmp = p.x() - x.p.x();
		    if (x.revs) cmp = p.y() - x.p.y();
			if (x.lb != null && cmp <= 0) nearPoint = nearest(x.lb, p);
			if (x.rt != null && cmp >= 0) nearPoint = nearest(x.rt, p);
		}*/
		if (x.p.distanceTo(p) <= nearPoint.distanceTo(p) || x.rect.distanceSquaredTo(p) <= nearPoint.distanceTo(p)) {
			if (x.p.distanceTo(p) < nearPoint.distanceTo(p)) nearPoint = x.p;
			double cmp = p.x() - x.p.x();
		    if (x.revs) cmp = p.y() - x.p.y();
			//if (x.lb != null && cmp < 0) nearPoint = nearest(x.lb, p);
			//if (x.rt != null && cmp >= 0) nearPoint = nearest(x.rt, p);
			if (cmp < 0.0) {
                     if (x.lb != null) nearPoint = nearest(x.lb, p);
                     if (x.rt != null) nearPoint = nearest(x.rt, p);
            }else {
				if (x.rt != null) nearPoint = nearest(x.rt, p);
                if (x.lb != null) nearPoint = nearest(x.lb, p);
			}
		}
		return nearPoint;
	}
	

    public static void main(String[] args)            // unit testing of the methods (optional)  
	{
		/*KdTree kd = new KdTree();
		Point2D p1 = new Point2D(0.5, 0.5);
		
		StdOut.println(kd.isEmpty());
		kd.insert(new Point2D(0.1, 0.4));
		kd.insert(new Point2D(0.6, 0.4));
		kd.insert(p1);
		StdOut.println(kd.size());
		kd.insert(p1);
		kd.insert(new Point2D(0.2, 0.2));
		StdOut.println(kd.size());
		if (kd.contains(p1))
			StdOut.println("Absolutly right");
		StdOut.println(kd.nearest(new Point2D(0.1, 0.1)));*/
	}   
}

























