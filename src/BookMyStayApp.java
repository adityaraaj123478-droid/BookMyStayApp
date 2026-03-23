import java.util.*;

// 🔹 Reservation
class Reservation {
    String reservationId;
    String guestName;
    String roomType;

    Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

// 🔥 Booking History (List)
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addBooking(Reservation r) {
        history.add(r);
    }

    // Get all bookings
    public List<Reservation> getAllBookings() {
        return history;
    }
}

// 🔥 Reporting Service
class BookingReportService {

    // Display all bookings
    public void showAllBookings(List<Reservation> bookings) {
        System.out.println("=== Booking History ===");

        for (Reservation r : bookings) {
            r.display();
        }
    }

    // Summary report
    public void generateSummary(List<Reservation> bookings) {

        System.out.println("\n=== Booking Summary ===");

        Map<String, Integer> countMap = new HashMap<>();

        for (Reservation r : bookings) {
            countMap.put(r.roomType,
                    countMap.getOrDefault(r.roomType, 0) + 1);
        }

        for (String type : countMap.keySet()) {
            System.out.println(type + ": " + countMap.get(type) + " bookings");
        }
    }
}

// 🔹 MAIN
public class BookMyStayApp {
    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from UC6)
        history.addBooking(new Reservation("R101", "Aditya", "Single Room"));
        history.addBooking(new Reservation("R102", "Rahul", "Double Room"));
        history.addBooking(new Reservation("R103", "Priya", "Single Room"));

        BookingReportService reportService = new BookingReportService();

        // Show history
        reportService.showAllBookings(history.getAllBookings());

        // Show summary
        reportService.generateSummary(history.getAllBookings());
    }
}