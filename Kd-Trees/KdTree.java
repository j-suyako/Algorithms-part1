
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Stack;

public class KdTree {
    private class Node implements Comparable<Node>{
        private Point2D point;
        private Node left;
        private Node right;
        private RectHV rect;
        private int height;

        public Node(Point2D p) {
            point = p;
            left = null;
            right = null;
            height = 0;
            rect = new RectHV(0, 0, 1, 1);
        }

        public boolean isInRect(RectHV rect) {
            return rect.contains(point);
        }

        public int rela_to_Rect(RectHV rect) {
            if (height % 2 == 1) {
                if (rect.xmax() < point.x())
                    return -1;
                else if (rect.xmin() > point.x())
                    return 1;
                else
                    return 0;
            }
            else {
                if (rect.ymax() < point.y())
                    return -1;
                else if (rect.ymin() > point.y())
                    return 1;
                else
                    return 0;
            }
        }

        public void draw() {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.02);
            StdDraw.text(point.x(), point.y(), point.toString());
            point.draw();
            StdDraw.setPenRadius(0.01);
            if (height % 2 == 1) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(point.x(), rect.ymin(), point.x(), rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rect.xmin(), point.y(), rect.xmax(), point.y());
            }
        }

        @Override
        public int compareTo(Node that) {
            if (that.height % 2 == 1)
                return Double.compare(this.point.x(), that.point.x());
            else
                return this.point.compareTo(that.point);
        }

        @Override
        public String toString() {
            return point.toString();
        }
    }
    private Node root;
    private int count;

    public KdTree() {
        root = null;
        count = 0;
    }

    public boolean isEmpty() {
//        return root == null;
        return count == 0;
    }

    public int size() {
//        return size(root);
        return count;
    }

    private int size(Node x) {
        if (x == null)
            return 0;
        else
            return size(x.left) + size(x.right) + 1;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node toInsert = new Node(p);
        root = insert(root, root, 0, toInsert);
    }

    private Node insert(Node curr, Node pre, int flag, Node toInsert) {
        ++toInsert.height;
        if (pre == null) {
            ++count;
            return toInsert;
        }
        if (toInsert.point.equals(pre.point)) {
            return curr;
        }
        if (curr == null) {
            ++count;
            if (toInsert.height % 2 != 0) {
                if (flag == 1) toInsert.rect = split(pre.rect, pre.point, 'h')[1];
                else toInsert.rect = split(pre.rect, pre.point, 'h')[0];
            } else {
                if (flag == 1) toInsert.rect = split(pre.rect, pre.point, 'v')[1];
                else toInsert.rect = split(pre.rect, pre.point, 'v')[0];
            }
            return toInsert;
        }
        if (toInsert.compareTo(curr) >= 0) {
            curr.right = insert(curr.right, curr, 1, toInsert);
        }
        else
            curr.left = insert(curr.left, curr, 0, toInsert);
        return curr;
    }

    private static RectHV[] split(RectHV rect, Point2D p, char flag) {
        /* split a rectangle by a line passing point p, flag represent the direction of this line */
        if (!rect.contains(p)) throw new IllegalArgumentException();
        RectHV[] res = new RectHV[2];
        if (flag == 'v') {
            res[0] = new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax());
            res[1] = new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax());
        } else if (flag == 'h') {
            res[0] = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
            res[1] = new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
        }
        return res;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node curr = root;
        Node toFind = new Node(p);
        while (!curr.point.equals(p)) {
            if (toFind.compareTo(curr) >= 0)
                curr = curr.right;
            else
                curr = curr.left;
            if (curr == null)
                break;
        }
        return curr != null;
    }

    public void draw() {
        draw(root);
    }

    private void draw(Node x) {
        if (x == null) {
            return;
        } else {
            x.draw();
            draw(x.left);
            draw(x.right);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> res = new Stack<>();
        range(root, res, rect);
        return res;
    }

    private void range(Node x, Stack<Point2D> res, RectHV rect) {
        if (x == null) return;
        int compare = x.rela_to_Rect(rect);
        if (compare < 0) range(x.left, res, rect);
        else if (compare > 0) {
            range(x.right, res, rect);
        } else {
            if (x.isInRect(rect))
                res.push(x.point);
            range(x.left, res, rect);
            range(x.right, res, rect);
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node target = new Node(p);
        return nearest(root, target);
    }

    private Point2D nearest(Node x, Node target) {
        if (x == null) return null;
        Point2D p_left = null;
        Point2D p_right = null;
        double dis0 = x.point.distanceSquaredTo(target.point);
        double dis1 = Double.POSITIVE_INFINITY;
        double dis2 = Double.POSITIVE_INFINITY;
        int cmp = target.compareTo(x);
        if (cmp < 0) {
            p_left = nearest(x.left, target);
            if (p_left != null)
                dis1 = p_left.distanceSquaredTo(target.point);
            if (x.right != null && dis0 > x.right.rect.distanceSquaredTo(target.point)) {
                p_right = nearest(x.right, target);
                if (p_right != null)
                    dis2 = p_right.distanceSquaredTo(target.point);
            }
        } else {
            p_right = nearest(x.right, target);
            if (p_right != null)
                dis2 = p_right.distanceSquaredTo(target.point);
            if (x.left != null && dis0 > x.left.rect.distanceSquaredTo(target.point)) {
                p_left = nearest(x.left, target);
                if (p_left != null)
                    dis1 = p_left.distanceSquaredTo(target.point);
            }
        }
        double sub = dis1 <= dis2 ? dis1 : dis2;
        Point2D Sub = dis1 <= dis2 ? p_left : p_right;
        return dis0 <= sub ? x.point : Sub;
    }

    public static void main(String[] args) {
        Point2D a = new Point2D(0.7, 0.2);
        Point2D b = new Point2D(0.5, 0.4);
        Point2D c = new Point2D(0.2, 0.3);
        Point2D d = new Point2D(0.4, 0.7);
        Point2D e = new Point2D(0.9, 0.6);
        Point2D f = new Point2D(0.7, 0.2);
        Point2D[] p = {a, b, c, d, e, f};
        KdTree kdtree = new KdTree();
        for (Point2D each : p) {
            kdtree.insert(each);
        }
        StdOut.print(kdtree.contains(new Point2D(0.1, 0.2)));
//        StdOut.print(kdtree.size());
//        kdtree.draw();
//        String filename = "C:\\Users\\JXT\\IdeaProjects\\Kd-Trees\\test\\input100K.txt";
//        In in = new In(filename);
//        PointSET brute = new PointSET();
//        KdTree kdtree = new KdTree();
//        while (!in.isEmpty()) {
//            double x = in.readDouble();
//            double y = in.readDouble();
//            Point2D p = new Point2D(x, y);
//            kdtree.insert(p);
//            brute.insert(p);
//        }
//        brute.draw();
//        StdDraw.setPenColor(StdDraw.BLUE);
//        RectHV rect = new RectHV(0.38, 0.784, 0.936, 0.907);
//        for (Point2D each : kdtree.range(rect))
//            each.draw();
//        Point2D temp = kdtree.nearest(new Point2D(0.37, 0.5));
//        StdOut.print(temp);
    }
}
