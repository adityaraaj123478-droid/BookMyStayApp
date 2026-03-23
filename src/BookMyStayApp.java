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

// 🔹 Booking Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.add(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// 🔹 Inventory
class RoomInventory {
    private HashMap<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void reduceAvailability(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }
}

// 🔥 Booking Service (MAIN LOGIC)
class BookingService {

    // Store allocated room IDs (no duplicates)
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type → allocated room IDs
    private HashMap<String, Set<String>> roomAllocations = new HashMap<>();

    public void processBookings(BookingRequestQueue queue, RoomInventory inventory) {

        while (!queue.isEmpty()) {

            Reservation r = queue.getNextRequest();

            String type = r.roomType;

            if (inventory.getAvailability(type) > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(type);

                // Ensure uniqueness
                while (allocatedRoomIds.contains(roomId)) {
                    roomId = generateRoomId(type);
                }

                allocatedRoomIds.add(roomId);

                // Store allocation
                roomAllocations.putIfAbsent(type, new HashSet<>());
                roomAllocations.get(type).add(roomId);

                // Update inventory
                inventory.reduceAvailability(type);

                // Confirm booking
                System.out.println("Booking Confirmed:");
                System.out.println("Guest: " + r.guestName);
                System.out.println("Room Type: " + type);
                System.out.println("Room ID: " + roomId);
                System.out.println("----------------------");

            } else {
                System.out.println("Booking Failed (No Availability): " + r.guestName + " - " + type);
            }
        }
    }

    // Generate random room ID
    private String generateRoomId(String type) {
        return type.substring(0, 2).toUpperCase() + new Random().nextInt(1000);
    }
}

// 🔹 MAIN
public class BookMyStayApp {
    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();

        // Add booking requests
        queue.addRequest(new Reservation("Aditya", "Single Room"));
        queue.addRequest(new Reservation("Rahul", "Single Room"));
        queue.addRequest(new Reservation("Priya", "Single Room")); // should fail
        queue.addRequest(new Reservation("Aman", "Double Room"));

        RoomInventory inventory = new RoomInventory();

        BookingService service = new BookingService();

        // Process bookings
        service.processBookings(queue, inventory);
    }
}