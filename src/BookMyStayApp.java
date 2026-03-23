import java.util.*;

// 🔹 Reservation
class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// 🔹 Shared Inventory (THREAD SAFE)
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 1); // only 1 room to show conflict
    }

    // 🔥 synchronized → critical section
    public synchronized boolean bookRoom(String type) {

        int available = inventory.getOrDefault(type, 0);

        if (available > 0) {
            inventory.put(type, available - 1);
            return true;
        } else {
            return false;
        }
    }
}

// 🔹 Booking Task (Thread)
class BookingTask implements Runnable {

    private Reservation reservation;
    private RoomInventory inventory;

    BookingTask(Reservation r, RoomInventory inventory) {
        this.reservation = r;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName() +
                " trying to book for " + reservation.guestName);

        boolean success = inventory.bookRoom(reservation.roomType);

        if (success) {
            System.out.println("✅ Booking SUCCESS for " + reservation.guestName);
        } else {
            System.out.println("❌ Booking FAILED for " + reservation.guestName);
        }
    }
}

// 🔹 MAIN
public class BookMyStayApp {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        // Multiple requests (same room → conflict)
        Reservation r1 = new Reservation("Aditya", "Single Room");
        Reservation r2 = new Reservation("Rahul", "Single Room");

        // Threads
        Thread t1 = new Thread(new BookingTask(r1, inventory));
        Thread t2 = new Thread(new BookingTask(r2, inventory));

        t1.start();
        t2.start();
    }
}