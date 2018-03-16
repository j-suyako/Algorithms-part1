import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> points;

    public PointSET() {
        points = new TreeSet<>();
    }
    public boolean isEmpty() {
        return points.isEmpty();
    }
    public int size() {
        return points.size();
    }
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        points.add(p);
    }
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }
    public void draw() {
//        StdDraw.setPenColor(StdDraw.BLACK);
//        StdDraw.setPenRadius(0.02);
        for (Point2D each : points)
            each.draw();
    }
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> res = new Stack<>();
        Point2D minum = new Point2D(rect.xmin(), rect.ymin());
        Point2D maxim = new Point2D(rect.xmax(), rect.ymax());
        SortedSet<Point2D> yrangePoints = points.subSet(minum, true, maxim, true);
        for (Point2D each : yrangePoints) {
            if (each.x() >= rect.xmin() && each.x() <= rect.xmax())
                res.push(each);
        }
        return res;
    }
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Point2D res = null;
        double min_dis = Double.POSITIVE_INFINITY;
        for (Point2D each : points) {
            double temp_dis = p.distanceTo(each);
            if (temp_dis < min_dis) {
                min_dis = temp_dis;
                res = each;
            }
            if (min_dis == 0)
                break;
        }
        return res;
    }

    public static void main(String[] args) {
//        String[] temp = {"1","1","100"};
//        Point2D.main(temp);
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.02);
//        rect.draw();
        PointSET mypoints= new PointSET();
        while (true) {
            if (StdDraw.isMousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                StdOut.printf("%8.6f %8.6f\n", x, y);
                Point2D p = new Point2D(x, y);
                if (rect.contains(p)) {
                    StdOut.printf("%8.6f %8.6f\n", x, y);
                    mypoints.insert(p);
                    StdDraw.clear();
                    mypoints.draw();
                    StdDraw.show();
                }
            }
            StdDraw.pause(40);
        }
    }
}
