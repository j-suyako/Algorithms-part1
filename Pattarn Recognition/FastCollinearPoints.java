import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

    private final LineSegment[] final_res;
    private int count;
    
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        check(points);  // check here doesn't return a copy of point because it's not useful next
        LineSegment[] temp_res = new LineSegment[2];
        count = 0;
        int m = points.length;
        Point[] points_copy = Arrays.copyOf(points, m);
        for (Point each : points) {
            Comparator<Point> c = each.slopeOrder();
            Arrays.sort(points_copy, c);
            // j represents the index of second point in a segment
            int j = 1;
            while (j < m - 2) {
                double slope1 = points_copy[0].slopeTo(points_copy[j]);
                // k represents the index of last point in a segment then plus 1
                int k = j + 2;
                while (k < m && slope1 == points_copy[0].slopeTo(points_copy[k]))
                    k = k + 1;
                if (k == j + 2) j++;
                else {
                    if (count == temp_res.length) {
                        LineSegment[] temp2 = temp_res;
                        temp_res = Arrays.copyOf(temp2, 2*count);
                    }
                    Point[] tuple = Arrays.copyOfRange(points_copy, j, k);
                    Point[] min_max = min_max(tuple);  // find the min and max in tuple
                    if (points_copy[0].compareTo(min_max[0]) < 0) temp_res[count++] = new LineSegment(points_copy[0], min_max[1]);
                    j = k;
                }
            }
        }
        final_res = Arrays.copyOf(temp_res, count);
    }

    private void check(Point[] points){
        Point[] points_copy = Arrays.copyOf(points, points.length);
        for (Point each : points_copy)
            if (each == null) throw new IllegalArgumentException();
        Arrays.sort(points_copy);
        for (int i = 0; i < points_copy.length - 1; i++)
            if (points_copy[i].compareTo(points_copy[i+1]) == 0) throw new IllegalArgumentException();
    }

    private Point[] min_max(Point[] points) {
        Point[] res = {points[0], points[0]};
        for (Point each : points) {
            if (res[0].compareTo(each) > 0)
                res[0] = each;
            if (res[1].compareTo(each) < 0)
                res[1] = each;
        }
        return res;
    }
    
    public int numberOfSegments(){
        return count;
    }
    
    public LineSegment[] segments(){
        return Arrays.copyOf(final_res, final_res.length);
    }
    
    public static void main(String[] args) { 
//        int[] x = {10000,0,3000,7000,20000,3000,14000,6000};
//        int[] y = {0,10000,7000,3000,21000,4000,15000,7000};
//        Point[] points = new Point[8];
//        for (int i = 0; i < 8; i++) points[i] = new Point(x[i],y[i]);
//        FastCollinearPoints collinear = new FastCollinearPoints(points);
////        for (myPoint p : collinear.Points) StdOut.printf("%s",p.getCurr().toString());
//        StdOut.printf("%n");
//        StdOut.println(collinear.numberOfSegments());
//        for (LineSegment segment : collinear.segments()) StdOut.printf("%s",segment.toString());
        int[] x = {31306,18834,31306,11992};
        int[] y = {8336,27894,8336,26502};
        Point[] points = new Point[4];
        for (int i = 0; i < 4; i++) points[i] = new Point(x[i],y[i]);
        FastCollinearPoints collinear = new FastCollinearPoints(points);
    }
    
    /* ADD YOUR CODE HERE */
    
}
