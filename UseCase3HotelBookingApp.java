import java.util.HashMap;
import java.util.Map;

/**
 * RoomInventory is responsible for managing and exposing room availability 
 * across the system using a centralized data structure.
 */
class RoomInventory {
    // HashMap provides O(1) lookup and acts as the Single Source of Truth
    private final Map<String, Integer> inventory;

    /**
     * Requirement: Initialize room availability using a constructor.
     */
    public RoomInventory() {
        this.inventory = new HashMap<>();
    }

    /**
     * Requirement: Register room types with their available counts.
     */
    public void registerRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    /**
     * Requirement: Provide methods to retrieve current availability.
     * Demonstrates O(1) lookup performance.
     */
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    /**
     * Requirement: Support controlled updates to room availability.
     * Ensures inventory state remains consistent (no negative stock).
     */
    public boolean updateAvailability(String roomType, int change) {
        if (!inventory.containsKey(roomType)) {
            return false;
        }

        int currentCount = inventory.get(roomType);
        int updatedCount = currentCount + change;

        if (updatedCount >= 0) {
            inventory.put(roomType, updatedCount);
            return true;
        }
        return false; // Insufficient inventory
    }

    /**
     * Displays the current inventory state when requested.
     */
    public void displayInventory() {
        System.out.println("--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " | Available: " + entry.getValue());
        }
    }
}

/**
 * Main application class demonstrating Use Case 3.
 * * @author Gemini
 * @version 3.0
 */
public class UseCase3HotelBookingApp {

    public static void main(String[] args) {
        // Welcome message from Use Case 1
        System.out.println("Welcome to the Book My Stay Application v3.0\n");

        // 1. System initializes the inventory component
        RoomInventory inventoryManager = new RoomInventory();

        // 2. Room types are registered with their available counts
        inventoryManager.registerRoomType("Single", 10);
        inventoryManager.registerRoomType("Double", 5);
        inventoryManager.registerRoomType("Suite", 2);

        // 3. Current inventory state is displayed
        inventoryManager.displayInventory();

        // 4. Demonstrate O(1) lookup and controlled updates
        System.out.println("\n--- Processing Booking Requests ---");
        
        processBooking(inventoryManager, "Single");
        processBooking(inventoryManager, "Suite");
        processBooking(inventoryManager, "Suite");
        processBooking(inventoryManager, "Suite"); // Should fail (insufficient stock)

        // 5. Display final state to ensure consistency
        System.out.println();
        inventoryManager.displayInventory();
    }

    /**
     * Helper method to simulate a booking interaction.
     */
    private static void processBooking(RoomInventory inventory, String type) {
        boolean success = inventory.updateAvailability(type, -1);
        if (success) {
            System.out.println("Booking Success: 1 " + type + " room reserved.");
        } else {
            System.out.println("Booking Failed: No " + type + " rooms available.");
        }
    }
}