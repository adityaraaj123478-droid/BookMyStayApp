import java.util.HashMap;

// Room class
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

// Inventory (same as UC3)
class RoomInventory {
    private HashMap<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // one unavailable to test filtering
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

// 🔥 NEW CLASS (Search Service)
class RoomSearchService {

    public void searchRooms(Room[] rooms, RoomInventory inventory) {
        System.out.println("=== Available Rooms ===");

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.type);

            // show only available rooms
            if (available > 0) {
                room.display();
                System.out.println("Available: " + available);
                System.out.println("-------------------");
            }
        }
    }
}

// MAIN
public class BookMyStayApp {
    public static void main(String[] args) {

        Room[] rooms = {
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        };

        RoomInventory inventory = new RoomInventory();

        RoomSearchService searchService = new RoomSearchService();

        // 🔥 Only read operation (no update)
        searchService.searchRooms(rooms, inventory);
    }
}