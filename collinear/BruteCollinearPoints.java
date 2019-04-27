import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final List<LineSegment> lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] origin) {

        // corner cases and copy
        if (origin == null) throw new java.lang.IllegalArgumentException();
        int n = origin.length;
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            if (origin[i] == null) throw new java.lang.IllegalArgumentException();
            for (int j = i+1; j < n; j++) {
                if (origin[i].compareTo(origin[j]) == 0) throw new java.lang.IllegalArgumentException();
            }
            points[i] = origin[i];
        }

        Arrays.sort(points, Point::compareTo);  // thus points are sorted when form a line

        // brute solution
        lineSegments = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                for (int k = j+1; k < n; k++) {
                    for (int m = k+1; m < n; m++) {
                        if (Double.compare(points[i].slopeTo(points[j]), points[i].slopeTo(points[k])) == 0
                                && Double.compare(points[i].slopeTo(points[j]), points[i].slopeTo(points[m])) == 0) {
                            lineSegments.add(new LineSegment(points[i], points[m]));
                        }
                    }
                }
            }
        }
    }

    // the line segments
    public int numberOfSegments() {
      return lineSegments.size();
  }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
  }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {  p.draw();  }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}




















