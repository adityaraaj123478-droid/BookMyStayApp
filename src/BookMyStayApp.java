import java.util.*;

// 🔹 Reservation class (represents booking request)
class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    void display() {
        System.out.println("Guest: " + guestName + " | Room: " + roomType);
    }
}

// 🔹 Booking Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue;

    BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request
    public void addRequest(Reservation r) {
        queue.add(r);
        System.out.println("Request Added:");
        r.display();
    }

    // Display all requests (FIFO order)
    public void displayQueue() {
        System.out.println("\n=== Booking Requests (FIFO Order) ===");
        for (Reservation r : queue) {
            r.display();
        }
    }
}

// 🔹 MAIN CLASS
public class BookMyStayApp {
    public static void main(String[] args) {

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate requests
        bookingQueue.addRequest(new Reservation("Aditya", "Single Room"));
        bookingQueue.addRequest(new Reservation("Rahul", "Double Room"));
        bookingQueue.addRequest(new Reservation("Priya", "Suite Room"));

        // Show queue
        bookingQueue.displayQueue();
    }
}