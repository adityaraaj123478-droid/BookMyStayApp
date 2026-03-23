import java.util.*;

// 🔹 Reservation
class Reservation {
    String reservationId;
    String guestName;
    String roomType;

    Reservation(String id, String guestName, String roomType) {
        this.reservationId = id;
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// 🔹 Inventory
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrease(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public void increase(String type) {
        inventory.put(type, inventory.get(type) + 1);
    }
}

// 🔹 Booking Service
class BookingService {

    // Track allocated rooms
    Map<String, String> reservationToRoomId = new HashMap<>();

    public void book(Reservation r, RoomInventory inventory) {

        if (inventory.getAvailability(r.roomType) > 0) {

            String roomId = generateRoomId(r.roomType);

            reservationToRoomId.put(r.reservationId, roomId);
            inventory.decrease(r.roomType);

            System.out.println("Booked: " + r.guestName + " → " + roomId);

        } else {
            System.out.println("Booking Failed (No rooms): " + r.guestName);
        }
    }

    private String generateRoomId(String type) {
        return type.substring(0, 2).toUpperCase() + new Random().nextInt(100);
    }
}

// 🔥 Cancellation Service (MAIN CONCEPT)
class CancellationService {

    // Stack for rollback (LIFO)
    private Stack<String> releasedRoomIds = new Stack<>();

    public void cancel(Reservation r,
                       BookingService bookingService,
                       RoomInventory inventory) {

        // Validate existence
        if (!bookingService.reservationToRoomId.containsKey(r.reservationId)) {
            System.out.println("Cancellation Failed: Reservation not found → " + r.reservationId);
            return;
        }

        // Get room ID
        String roomId = bookingService.reservationToRoomId.get(r.reservationId);

        // Push to stack (rollback tracking)
        releasedRoomIds.push(roomId);

        // Remove booking
        bookingService.reservationToRoomId.remove(r.reservationId);

        // Restore inventory
        inventory.increase(r.roomType);

        System.out.println("Cancelled: " + r.guestName + " → Room Released: " + roomId);
    }

    // Show rollback stack
    public void showRollbackStack() {
        System.out.println("\nRollback Stack (LIFO): " + releasedRoomIds);
    }
}

// 🔹 MAIN
public class BookMyStayApp {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService();
        CancellationService cancelService = new CancellationService();

        // Book rooms
        Reservation r1 = new Reservation("R1", "Aditya", "Single Room");
        Reservation r2 = new Reservation("R2", "Rahul", "Double Room");

        bookingService.book(r1, inventory);
        bookingService.book(r2, inventory);

        // Cancel one booking
        cancelService.cancel(r1, bookingService, inventory);

        // Try invalid cancel
        cancelService.cancel(new Reservation("R3", "Fake", "Single Room"),
                bookingService, inventory);

        // Show rollback stack
        cancelService.showRollbackStack();
    }
}