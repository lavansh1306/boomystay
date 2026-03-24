import java.util.*;

// Custom Exceptions for domain-specific errors
class InvalidRoomTypeException extends Exception {
    public InvalidRoomTypeException(String message) { super(message); }
}

class InsufficientInventoryException extends Exception {
    public InsufficientInventoryException(String message) { super(message); }
}

public class UseCase9ErrorHandlingValidation {

    // System State
    private static Map<String, Integer> inventory = new HashMap<>();
    
    public static void main(String[] args) {
        // Initialize Inventory
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 2);

        System.out.println("--- Starting Booking Validation Service ---");

        // Test Case 1: Valid Booking
        processBooking("Alice", "Deluxe");

        // Test Case 2: Invalid Room Type (Spelling error/Non-existent)
        processBooking("Bob", "Penthouse");

        // Test Case 3: Out of Stock (Inventory check)
        processBooking("Charlie", "Deluxe");

        // Test Case 4: Null or Empty Input
        processBooking("", "Suite");

        System.out.println("\n--- System remains stable. Final Inventory: " + inventory + " ---");
    }

    /**
     * Orchestrates validation and booking. 
     * Uses a try-catch block to handle failures gracefully without crashing.
     */
    public static void processBooking(String guestName, String roomType) {
        try {
            System.out.println("\nAttempting booking for: " + (guestName.isEmpty() ? "Unknown" : guestName));
            
            // Step 1: Validate Input
            validateInput(guestName, roomType);
            
            // Step 2: Validate System State (Inventory)
            validateAvailability(roomType);

            // Step 3: Perform Atomic State Change
            inventory.put(roomType, inventory.get(roomType) - 1);
            System.out.println("SUCCESS: Booking confirmed for " + guestName + " in " + roomType);

        } catch (InvalidRoomTypeException | InsufficientInventoryException | IllegalArgumentException e) {
            // Step 4: Graceful Failure Handling
            System.err.println("VALIDATION FAILED: " + e.getMessage());
        }
    }

    /**
     * Fail-Fast Validation for Inputs
     */
    private static void validateInput(String name, String type) throws InvalidRoomTypeException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name cannot be empty.");
        }
        if (!inventory.containsKey(type)) {
            throw new InvalidRoomTypeException("Room type '" + type + "' does not exist in our system.");
        }
    }

    /**
     * Guarding System State
     */
    private static void validateAvailability(String type) throws InsufficientInventoryException {
        if (inventory.get(type) <= 0) {
            throw new InsufficientInventoryException("No rooms of type '" + type + "' currently available.");
        }
    }
}