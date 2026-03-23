import java.io.*;
import java.util.*;

// 🔹 Reservation (Serializable)
class Reservation implements Serializable {
    String id;
    String guestName;
    String roomType;

    Reservation(String id, String guestName, String roomType) {
        this.id = id;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String toString() {
        return id + " | " + guestName + " | " + roomType;
    }
}

// 🔹 Inventory (THREAD SAFE + Serializable)
class RoomInventory implements Serializable {
    private Map<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
    }

    public synchronized boolean bookRoom(String type) {
        int available = inventory.getOrDefault(type, 0);

        if (available > 0) {
            inventory.put(type, available - 1);
            return true;
        }
        return false;
    }
}

// 🔥 Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "data.ser";

    public void save(List<Reservation> bookings, RoomInventory inventory) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(bookings);
            out.writeObject(inventory);

            System.out.println("✅ Data saved successfully");

        } catch (IOException e) {
            System.out.println("❌ Error saving data: " + e.getMessage());
        }
    }

    public Object[] load() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            List<Reservation> bookings = (List<Reservation>) in.readObject();
            RoomInventory inventory = (RoomInventory) in.readObject();

            System.out.println("✅ Data loaded successfully");
            return new Object[]{bookings, inventory};

        } catch (Exception e) {
            System.out.println("⚠️ No previous data found, starting fresh...");
            return null;
        }
    }
}

// 🔹 Booking Task (Thread)
class BookingTask implements Runnable {

    private Reservation reservation;
    private RoomInventory inventory;
    private List<Reservation> bookings;

    BookingTask(Reservation r, RoomInventory inventory, List<Reservation> bookings) {
        this.reservation = r;
        this.inventory = inventory;
        this.bookings = bookings;
    }

    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName() +
                " trying to book for " + reservation.guestName);

        boolean success = inventory.bookRoom(reservation.roomType);

        if (success) {
            synchronized (bookings) {
                bookings.add(reservation);
            }
            System.out.println("✅ Booking SUCCESS for " + reservation.guestName);
        } else {
            System.out.println("❌ Booking FAILED for " + reservation.guestName);
        }
    }
}

// 🔹 MAIN
public class BookMyStayApp {
    public static void main(String[] args) {

        PersistenceService service = new PersistenceService();

        Object[] data = service.load();

        List<Reservation> bookings;
        RoomInventory inventory;

        if (data != null) {
            bookings = (List<Reservation>) data[0];
            inventory = (RoomInventory) data[1];
        } else {
            bookings = new ArrayList<>();
            inventory = new RoomInventory();
        }

        // New booking requests
        Reservation r1 = new Reservation("R1", "Aditya", "Single Room");
        Reservation r2 = new Reservation("R2", "Rahul", "Single Room");

        Thread t1 = new Thread(new BookingTask(r1, inventory, bookings));
        Thread t2 = new Thread(new BookingTask(r2, inventory, bookings));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Show bookings
        System.out.println("\n📋 Current Bookings:");
        for (Reservation r : bookings) {
            System.out.println(r);
        }

        // Save data
        service.save(bookings, inventory);
    }
}