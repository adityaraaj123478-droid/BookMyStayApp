import java.util.HashMap;

// Room Class (same as before)
abstract class Room {
    String type;
    int beds;
    double price;

    Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    void display() {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Price: " + price);
    }
}

class SingleRoom extends Room {
    SingleRoom() {
        super("Single Room", 1, 1000);
    }
}

class DoubleRoom extends Room {
    DoubleRoom() {
        super("Double Room", 2, 2000);
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super("Suite Room", 3, 5000);
    }
}

// 🔥 NEW CLASS (IMPORTANT)
class RoomInventory {
    private HashMap<String, Integer> inventory;

    // Constructor
    RoomInventory() {
        inventory = new HashMap<>();

        // Initialize availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability
    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Display inventory
    public void displayInventory() {
        System.out.println("=== Room Inventory ===");
        for (String key : inventory.keySet()) {
            System.out.println(key + " Available: " + inventory.get(key));
        }
    }
}

// MAIN CLASS
public class BookMyStayApp {
    public static void main(String[] args) {

        Room single = new SingleRoom();
        Room doub = new DoubleRoom();
        Room suite = new SuiteRoom();

        RoomInventory inventory = new RoomInventory();

        System.out.println("=== Room Details ===");

        single.display();
        System.out.println("Available: " + inventory.getAvailability("Single Room"));

        doub.display();
        System.out.println("Available: " + inventory.getAvailability("Double Room"));

        suite.display();
        System.out.println("Available: " + inventory.getAvailability("Suite Room"));

        // Show full inventory
        inventory.displayInventory();
    }
}