import java.util.LinkedList;
import java.util.*;
public class RPSQueue {
public static void main(String[] args) {
// We declare the type as the Queue interface, but implement it with a LinkedList
Queue<String> coffeeLine = new LinkedList<>();
// Add customers to the BACK of the line (Enqueue)
coffeeLine.offer("Alice");
coffeeLine.offer("Bob");
coffeeLine.offer("Charlie");
System.out.println("\nCurrent Line: " + coffeeLine);
System.out.println("Next customer to be served: " + coffeeLine.peek());
// Serve the customer and remove them from the FRONT (Dequeue)
if (!coffeeLine.isEmpty()) {
String servedCustomer = coffeeLine.poll();
System.out.println("Served: " + servedCustomer);
}
System.out.println("Current Line: " + coffeeLine);
}
}