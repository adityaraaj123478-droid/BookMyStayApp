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

// 🔹 Inventory (Serializable)
class RoomInventory implements Serializable {
    Map<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
    }
}

// 🔥 Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "data.ser";

    // Save data
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

    // Load data
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

// 🔹 MAIN
public class BookMyStayApp {
    public static void main(String[] args) {

        PersistenceService service = new PersistenceService();

        // Try loading previous state
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

        // Add new booking
        bookings.add(new Reservation("R1", "Aditya", "Single Room"));

        // Display bookings
        System.out.println("\nCurrent Bookings:");
        for (Reservation r : bookings) {
            System.out.println(r);
        }

        // Save before exit
        service.save(bookings, inventory);
    }
}