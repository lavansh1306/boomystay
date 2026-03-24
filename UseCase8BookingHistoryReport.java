import java.util.*;

public class UseCase8BookingHistoryReport {

    // Model for a confirmed Reservation
    static class Reservation {
        String reservationId;
        String guestName;
        String roomType;
        double basePrice;
        double addOnCost;

        Reservation(String id, String name, String type, double price, double addOns) {
            this.reservationId = id;
            this.guestName = name;
            this.roomType = type;
            this.basePrice = price;
            this.addOnCost = addOns;
        }

        double getTotalBill() {
            return basePrice + addOnCost;
        }

        @Override
        public String toString() {
            return String.format("[%s] Guest: %-8s | Room: %-7s | Total: $%.2f", 
                                 reservationId, guestName, roomType, getTotalBill());
        }
    }

    // Booking History: A List to preserve chronological insertion order
    private static List<Reservation> bookingHistory = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("--- Simulating Booking Confirmations ---");

        // 1. Record confirmed bookings (Simulating data from UC6 and UC7)
        confirmAndRecord(new Reservation("RES-101", "Alice", "Deluxe", 150.0, 40.0));
        confirmAndRecord(new Reservation("RES-102", "Bob", "Suite", 300.0, 50.0));
        confirmAndRecord(new Reservation("RES-103", "Charlie", "Deluxe", 150.0, 0.0));

        // 2. Generate Admin Reports
        generateSummaryReport();
        
        // 3. Search History (Audit)
        viewBookingDetails("RES-102");
    }

    /**
     * Adds a confirmed reservation to the history list.
     */
    public static void confirmAndRecord(Reservation res) {
        bookingHistory.add(res);
        System.out.println("History Updated: " + res.reservationId + " recorded.");
    }

    /**
     * Booking Report Service: Analyzes historical data for operational visibility.
     */
    public static void generateSummaryReport() {
        System.out.println("\n--- ADMINISTRATIVE BOOKING REPORT ---");
        System.out.println("Total Reservations Processed: " + bookingHistory.size());

        double totalRevenue = 0;
        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation res : bookingHistory) {
            System.out.println(res);
            totalRevenue += res.getTotalBill();
            roomTypeCount.put(res.roomType, roomTypeCount.getOrDefault(res.roomType, 0) + 1);
        }

        System.out.println("-------------------------------------");
        System.out.printf("Total Revenue Generated: $%.2f\n", totalRevenue);
        System.out.println("Bookings by Category: " + roomTypeCount);
        System.out.println("-------------------------------------");
    }

    /**
     * Allows retrieval of specific records for review (Audit Trail).
     */
    public static void viewBookingDetails(String id) {
        System.out.println("\nSearching Audit Trail for ID: " + id);
        bookingHistory.stream()
            .filter(r -> r.reservationId.equals(id))
            .findFirst()
            .ifPresentOrElse(
                r -> System.out.println("Found Record: " + r),
                () -> System.out.println("Record not found.")
            );
    }
}