import java.util.*;

public class UseCase10BookingCancellation {

    // Current Inventory
    private static Map<String, Integer> inventory = new HashMap<>();
    
    // Active Bookings: Map Reservation ID to Room Type
    private static Map<String, String> activeBookings = new HashMap<>();
    
    // Rollback Structure: Stack to track released Room IDs (LIFO)
    private static Stack<String> releasedRoomIDs = new Stack<>();

    public static void main(String[] args) {
        // 1. Setup Initial State
        inventory.put("Deluxe", 1);
        activeBookings.put("RES-101", "Deluxe");
        System.out.println("Initial State: Inventory " + inventory + ", Bookings " + activeBookings);

        // 2. Perform Valid Cancellation
        cancelBooking("RES-101", "Deluxe-101");

        // 3. Attempt Invalid Cancellation (Non-existent reservation)
        cancelBooking("RES-999", "Deluxe-999");

        // 4. Attempt Duplicate Cancellation
        cancelBooking("RES-101", "Deluxe-101");

        System.out.println("\n--- Final System State ---");
        System.out.println("Inventory: " + inventory);
        System.out.println("Released Rooms (Stack): " + releasedRoomIDs);
    }

    /**
     * Cancellation Service: Validates and rolls back system state.
     */
    public static void cancelBooking(String reservationId, String roomId) {
        System.out.println("\nProcessing Cancellation for: " + reservationId);

        // Step 1: Validation of Cancellation Request
        if (!activeBookings.containsKey(reservationId)) {
            System.err.println("ERROR: Cancellation failed. Reservation ID " + reservationId + " not found or already cancelled.");
            return;
        }

        // Step 2: Controlled Mutation & State Reversal
        String roomType = activeBookings.remove(reservationId); // Remove from active records

        // Step 3: LIFO Rollback Logic
        releasedRoomIDs.push(roomId); // Push to stack for potential immediate re-assignment

        // Step 4: Inventory Restoration
        inventory.put(roomType, inventory.get(roomType) + 1);

        System.out.println("SUCCESS: " + reservationId + " cancelled. " + roomId + " returned to pool.");
        System.out.println("Updated Inventory for " + roomType + ": " + inventory.get(roomType));
    }
}