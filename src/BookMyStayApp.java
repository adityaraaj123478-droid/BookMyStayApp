import java.util.*;

// 🔹 Reservation (with ID)
class Reservation {
    String reservationId;
    String guestName;
    String roomType;

    Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// 🔹 Service class (Add-on)
class AddOnService {
    String serviceName;
    double price;

    AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }
}

// 🔥 Service Manager
class AddOnServiceManager {

    // Map reservationId → list of services
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Service Added: " + service.serviceName + " for Reservation " + reservationId);
    }

    // Display services
    public void displayServices(String reservationId) {
        System.out.println("\nServices for Reservation: " + reservationId);

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        for (AddOnService s : services) {
            System.out.println(s.serviceName + " - ₹" + s.price);
        }
    }

    // Calculate total cost
    public double calculateTotalCost(String reservationId) {
        double total = 0;

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services != null) {
            for (AddOnService s : services) {
                total += s.price;
            }
        }

        return total;
    }
}

// 🔹 MAIN
public class BookMyStayApp {
    public static void main(String[] args) {

        // Example reservation (already booked in UC6)
        Reservation r1 = new Reservation("R101", "Aditya", "Single Room");

        AddOnServiceManager manager = new AddOnServiceManager();

        // Add services
        manager.addService(r1.reservationId, new AddOnService("Breakfast", 200));
        manager.addService(r1.reservationId, new AddOnService("WiFi", 100));
        manager.addService(r1.reservationId, new AddOnService("Airport Pickup", 500));

        // Display services
        manager.displayServices(r1.reservationId);

        // Total cost
        double total = manager.calculateTotalCost(r1.reservationId);
        System.out.println("\nTotal Add-On Cost: ₹" + total);
    }
}