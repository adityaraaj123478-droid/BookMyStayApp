import java.util.*;

// 🔥 Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// 🔹 Reservation
class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// 🔹 Inventory
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, -1);
    }

    public void reduceAvailability(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public boolean isValidRoomType(String type) {
        return inventory.containsKey(type);
    }
}

// 🔥 Validator
class BookingValidator {

    public static void validate(Reservation r, RoomInventory inventory)
            throws InvalidBookingException {

        // Check null/empty input
        if (r.guestName == null || r.guestName.isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (r.roomType == null || r.roomType.isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty");
        }

        // Check valid room type
        if (!inventory.isValidRoomType(r.roomType)) {
            throw new InvalidBookingException("Invalid room type: " + r.roomType);
        }

        // Check availability
        if (inventory.getAvailability(r.roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for: " + r.roomType);
        }
    }
}

// 🔹 Booking Service
class BookingService {

    public void bookRoom(Reservation r, RoomInventory inventory) {
        try {
            // 🔥 Validate first (fail-fast)
            BookingValidator.validate(r, inventory);

            // Proceed booking
            inventory.reduceAvailability(r.roomType);

            System.out.println("Booking Successful:");
            System.out.println("Guest: " + r.guestName);
            System.out.println("Room: " + r.roomType);
            System.out.println("------------------");

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("Booking Failed: " + e.getMessage());
            System.out.println("------------------");
        }
    }
}

// 🔹 MAIN
public class BookMyStayApp {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService();

        // Valid booking
        service.bookRoom(new Reservation("Aditya", "Single Room"), inventory);

        // Invalid room type
        service.bookRoom(new Reservation("Rahul", "Luxury Room"), inventory);

        // No availability
        service.bookRoom(new Reservation("Priya", "Double Room"), inventory);
        service.bookRoom(new Reservation("Aman", "Double Room"), inventory);

        // Empty name
        service.bookRoom(new Reservation("", "Single Room"), inventory);
    }
}