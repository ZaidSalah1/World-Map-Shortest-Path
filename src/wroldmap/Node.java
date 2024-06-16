package wroldmap;


public class Node {
    Edge data;
    Node next;

    public Node(Edge data) {
        this.data = data;
        this.next = null;
    }
}