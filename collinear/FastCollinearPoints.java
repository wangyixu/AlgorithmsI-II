import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
     Compilation:  PASSED
     API:          PASSED

     Spotbugs:     PASSED
     PMD:          PASSED
     Checkstyle:   PASSED

     Correctness:  39/41 tests passed
     Memory:       1/1 tests passed
     Timing:       41/41 tests passed

     Aggregate score: 97.07%

    - constructor throws a java.lang.NullPointerException
    - constructor should throw a java.lang.IllegalArgumentException
 *
 * Created by yixu on 2017/1/10, edited on 2018/9/3.
 * */
public class FastCollinearPoints {
    private final List<LineSegment> lineSegments;

    // finds all line segments containing >=4 points
    public FastCollinearPoints(Point[] origin) {

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

        Arrays.sort(points, Point::compareTo);  // for same order

        // fast solution
        lineSegments = new ArrayList<>();
        int range = n - 3;
        for (int i = 0; i < range; i++) {
            Point currPoint = points[i];

            // copy points and sort
            Point[] p = new Point[n];
            System.arraycopy(points, 0, p, 0, n);
            Arrays.sort(p, 0, n, currPoint.slopeOrder());

            // store slopes to currPoint
            double[] slopes = new double[n];
            for (int j = 0; j < n; j++)
                slopes[j] = p[j].slopeTo(currPoint);

            // count collinear points
            double currSlope = slopes[0];
            int count = 1;
            for (int j = 1; j <= n; j++) {
                if (j < n && Double.compare(slopes[j], currSlope) == 0) {
                    count++;
                } else {
                    if (count >= 3) {
                        // sort points to get line's head and tail
                        Point[] line = new Point[count + 1];
                        line[0] = currPoint;
                        for (int k = 1; k <= count; k++)
                            line[k] = p[j - k];
                        Arrays.sort(line);
                        if (line[0].equals(currPoint)) {    // currPoint is the start of line
                            lineSegments.add(new LineSegment(currPoint, line[count]));
                        }
                    }

                    if (j < n) {
                        currSlope = slopes[j];
                        count = 1;
                    }
                }
            }
        }

            /*
            // copy points and sort separately before and after p[i]
            Point[] p = new Point[n];
            System.arraycopy(points, 0, p, 0, n);
            Arrays.sort(p, 0, i, p[i].slopeOrder());
            Arrays.sort(p, i + 1, n, p[i].slopeOrder());

            // store slopes
            double[] slopes = new double[n];
            for (int j = 0; j < n; j++)
                slopes[j] = p[j].slopeTo(p[i]);

            // count collinear points
            int prevIdx = 0;    // points index before p[i], range is [0, i-1]
            double currSlope = slopes[i + 1];
            int count = 1;
            for (int j = i + 2; j <= n; j++) {
                if (j < n && Double.compare(slopes[j], currSlope) == 0) {
                    count++;
                } else {
                    if (count >= 3) {
                        while (prevIdx < i - 1 && Double.compare(slopes[prevIdx], currSlope) == 0)
                            prevIdx++;
                        if (Double.compare(slopes[prevIdx], currSlope) != 0) { // if the slope has occur before, the line has be added
                            // sort points to get line's head and tail
                            Point[] line = new Point[count + 1];
                            line[0] = p[i];
                            for (int k = 1; k <= count; k++)
                                line[k] = p[j - k];
                            Arrays.sort(line);
                            lineSegments.add(new LineSegment(line[0], line[count]));

                            if (prevIdx > 0 && slopes[prevIdx] > currSlope)
                                prevIdx--;
                        }
                    }

                    if (j < n) {
                        currSlope = slopes[j];
                        count = 1;
                    }
                }
            }
        }*/
    }
        /*
    for (int i = 0; i < len-2; i++) {

      // copy points set and sort at points[i]'s sides
      Point[] p_sort = new Point[len];
            System.arraycopy(points, 0, p_sort, 0, len);
      if (i > 1) Arrays.sort(p_sort,  0 , i, p_sort[i].slopeOrder());
      Arrays.sort(p_sort, i+1, len, p_sort[i].slopeOrder());

      // record the slopes and add slopes[len]=slopes[i] as last collinearpoints judge
      double[] slopes = new double[len+1];
      for (int j = 0; j < len; j++) {
        slopes[j] = p_sort[i].slopeTo(p_sort[j]);
      }
      slopes[len] = slopes[i];

      // judge collinearpoints
      int head = 0;
      double slope = slopes[i+1];
      int    count = 1;
      for (int k = i+2; k <= len; k++) {
        if (slopes[k] != slope)  {
            if (count >= 3) {
            while (slopes[head] < slope && head < i-1) {
              head++;
            }
            if (slopes[head] != slope) {
                Point[] sort = new Point[count+1];
                for (int j=0; j<count; j++)
                sort[j] = p_sort[k-1-j];
                sort[count] = p_sort[i];      
                  Arrays.sort(sort);
                lineSegments[n++] = new LineSegment(sort[0],sort[count]);
              if (head > 0 && slopes[head] > slope)
                head--;
            }
          }
          slope = slopes[k];
            count = 1;
        } else count++;
      }
    }*/

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}




























