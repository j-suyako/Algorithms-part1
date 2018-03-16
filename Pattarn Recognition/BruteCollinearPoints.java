import java.lang.IllegalArgumentException;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

    private final LineSegment[] final_res;
    private int count;
    
    public BruteCollinearPoints(Point[] points) {
        /* find all line segments containing 4 points */
        if (points == null) throw new IllegalArgumentException();
        Point[] points_copy = check(points);
        LineSegment[] temp_res = new LineSegment[2];
        count = 0;
        int m = points_copy.length;
        for (int i = 0; i < m - 3; i++) {
            for (int j = i + 1; j < m - 2; j++) {

                double slope1 = points_copy[i].slopeTo(points_copy[j]);  // get slope of point i and point j

                for (int k = j + 1; k < m - 1; k++) {
                    double slope2 = points_copy[i].slopeTo(points_copy[k]);  // get slope of point i and point k
                    // reduce loop of slope3
                    if (slope1 == slope2) {
                        for (int l = k + 1; l < m; l++) {
                            double slope3 = points_copy[i].slopeTo(points_copy[l]);  // get slope of point i and point l
                            if (slope1 == slope3) {
                                if (count == temp_res.length) {
                                    LineSegment[] temp = temp_res;
                                    temp_res = Arrays.copyOf(temp, 2*count);
                                }
//                                Point[] tuple = {points_copy[i], points_copy[j], points_copy[k], points_copy[l]};
//                                Arrays.sort(tuple);
                                temp_res[count++] = new LineSegment(points_copy[i], points_copy[l]);
                                break;  // no more than 5 collinear points in input
                            }
                        }
                    }
                }
            }
        }
        final_res = Arrays.copyOf(temp_res, count);
    }
    
    private Point[] check(Point[] points){
        /* throw IllegalArgumentException if any point in points is null or any repeated points */
        Point[] points_copy = Arrays.copyOf(points, points.length);
        for (Point each : points_copy)
            if (each == null) throw new IllegalArgumentException();
        Arrays.sort(points_copy);
        for (int i = 0; i < points_copy.length - 1; i++)
            if (points_copy[i].compareTo(points_copy[i+1]) == 0) throw new IllegalArgumentException();
        return points_copy;
    }
    
    public int numberOfSegments(){
        /* the number of line segments */
        return count;
    }
    
    public LineSegment[] segments(){
        /* the line segments */
        return Arrays.copyOf(final_res, final_res.length);
    }
    
    public static void main(String[] args) {
//        BruteCollinearPoints temp = new BruteCollinearPoints(null);
        int[] x = {10000,0,3000,7000,20000,3000,14000,6000};
        int[] y = {0,10000,7000,3000,21000,4000,15000,7000};
        Point[] points = new Point[8];
        for (int i = 0; i < 8; i++) points[i] = new Point(x[i],y[i]);
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
//        for (Point p : collinear.Points) StdOut.printf("%s",p.toString());
        StdOut.printf("%n");
        for (LineSegment segment : collinear.segments()) StdOut.printf("%s",segment.toString());
//        int[] x = {31308,18834,31306,11992};
//        int[] y = {8336,27894,8336,26502};
//        Point[] points = new Point[4];
//        for (int i = 0; i < 4; i++) points[i] = new Point(x[i],y[i]);
//        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    }
}
