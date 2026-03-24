import java.util.*;

public class UseCase7AddOnServiceSelection {

    // Represents an optional service
    static class Service {
        String serviceName;
        double price;

        Service(String serviceName, double price) {
            this.serviceName = serviceName;
            this.price = price;
        }

        @Override
        public String toString() {
            return serviceName + " ($" + price + ")";
        }
    }

    // Add-On Service Manager: Maps Reservation ID to a List of selected Services
    private static Map<String, List<Service>> reservationAddOns = new HashMap<>();

    public static void main(String[] args) {
        // Sample Reservation IDs generated from Use Case 6
        String resId1 = "RES-DELUXE-101";
        String resId2 = "RES-SUITE-201";

        System.out.println("--- Adding Optional Services ---");

        // 1. Guest 1 selects multiple services
        addServiceToReservation(resId1, new Service("Breakfast Buffet", 25.0));
        addServiceToReservation(resId1, new Service("Late Check-out", 15.0));

        // 2. Guest 2 selects one service
        addServiceToReservation(resId2, new Service("Airport Shuttle", 50.0));

        // 3. Display and Calculate Costs
        displayReservationDetails(resId1);
        displayReservationDetails(resId2);
        displayReservationDetails("RES-EMPTY-999"); // Test for non-existent booking
    }

    /**
     * Associates a service with a reservation ID.
     * Uses computeIfAbsent to initialize the list if it doesn't exist.
     */
    public static void addServiceToReservation(String reservationId, Service service) {
        reservationAddOns
            .computeIfAbsent(reservationId, k -> new ArrayList<>())
            .add(service);
        
        System.out.println("Added " + service.serviceName + " to Reservation: " + reservationId);
    }

    /**
     * Calculates total cost and displays services for a specific reservation.
     */
    public static void displayReservationDetails(String reservationId) {
        System.out.println("\n--- Invoice Summary for " + reservationId + " ---");
        
        List<Service> services = reservationAddOns.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No additional services selected.");
            return;
        }

        double totalAddOnCost = 0;
        for (Service s : services) {
            System.out.println("- " + s);
            totalAddOnCost += s.price;
        }

        System.out.println("Total Additional Charges: $" + totalAddOnCost);
    }
}