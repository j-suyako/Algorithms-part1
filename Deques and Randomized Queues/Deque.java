import java.util.NoSuchElementException;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;

/**
 * A generalization of a stack and a queue that supports adding and removing items
 * from either the front or the back of the data structure
 *
 * @author suyako
 */
public class Deque<Item> implements Iterable<Item> {
    private Node first;  // the first node
    private Node last;   // the last node
    private int N;       // size of nodes
    // construct class Node
    private class Node{
        Item item;       // current node
        Node next;       // point to next node
        Node pre;        // point to previous node
        public Node(Item e) {
            item = e;
        }
    }
    
    public Deque(){
        /* construct an empty deque */
        first = null;
        last = first;
        N = 0;
    }

    public boolean isEmpty(){
        /* is the deque empty */
        return first == null;
    }

    public int size(){
        /* return the number of items on the deque */
        return N;
    }

    public void addFirst(Item item){
        /* add the item to the front */
        if(item == null) {throw new IllegalArgumentException("Null is not supported in this structure.");}
        if(N == 0) {first =new Node(item); last = first; N++;}
        else{
            Node old_first = first;
            first = new Node(item);
            first.next = old_first;
            old_first.pre = first;
            N++;
        }
    }

    public void addLast(Item item){
        /* add the item to the end */
        if(item == null){throw new IllegalArgumentException("Null is not supported in this structure.");}
        if(N == 0){first =new Node(item); last = first; N++;}
        else{
            Node new_last = new Node(item);
            new_last.pre = last;
            last.next = new_last;
            last = new_last;
            N++;
        }
    }

    public Item removeFirst(){
        /* remove and return the item from the front*/
        if(isEmpty()) {throw new NoSuchElementException("deque is empty.");}
        Node res = first;
        if(N == 1) {first = null; last = null;}
        else {first = first.next; first.pre = null;}
        N--;
        return res.item;
    }

    public Item removeLast(){
        /* remove and return the item from the end */
        if(isEmpty()){throw new NoSuchElementException("deque is empty.");}
        Node res = last;
        if(N == 1){first = null; last = null;}
        else {last = last.pre; last.next = null;}
        N--;
        return res.item;
    }

    public Iterator<Item> iterator(){
        /* return an iterator over items in order from front to end */
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item>{
        private Node current = first;
        public boolean hasNext() { return current != null;}
        public void remove() {throw new UnsupportedOperationException("This operation is not supported.");}
        public Item next(){
            if(current == null){throw new NoSuchElementException("No more items to return");}
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }

    public static void main(String[] args){
        Deque<String> deque = new Deque<String>();
        deque.addLast("A");
        String temp = deque.removeFirst();
        deque.addFirst("B");
        for(String s : deque){StdOut.println(s);}
    }
}