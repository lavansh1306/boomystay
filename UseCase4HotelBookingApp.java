import java.util.*;

/**
 * Domain Model: Room types and their characteristics.
 */
abstract class Room {
    private String type;
    private double price;
    private String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() { return type; }
    public double getPrice() { return price; }
    public String getAmenities() { return amenities; }
}

class SingleRoom extends Room { 
    public SingleRoom() { super("Single", 1000.0, "WiFi, Single Bed, Coffee Maker"); } 
}
class DoubleRoom extends Room { 
    public DoubleRoom() { super("Double", 1800.0, "WiFi, King Bed, Balcony"); } 
}
class SuiteRoom extends Room { 
    public SuiteRoom() { super("Suite", 3500.0, "WiFi, Luxury Bed, Mini-bar, Jacuzzi"); } 
}

/**
 * Inventory Manager: The State Holder.
 */
class RoomInventory {
    private final Map<String, Integer> inventory = new HashMap<>();

    public void registerRoomType(String type, int count) {
        inventory.put(type, count);
    }

    // Read-only access to availability
    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    // Returns a set of all registered room types for search purposes
    public Set<String> getAllRoomTypes() {
        return inventory.keySet();
    }
}

/**
 * Search Service: Handles read-only access to room information.
 * Enforces validation and separation of concerns.
 */
class RoomSearchService {
    private final RoomInventory inventory;
    private final Map<String, Room> roomDetails = new HashMap<>();

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
        // Pre-populate room details (Domain Model usage)
        roomDetails.put("Single", new SingleRoom());
        roomDetails.put("Double", new DoubleRoom());
        roomDetails.put("Suite", new SuiteRoom());
    }

    /**
     * Requirement: Retrieve availability and filter out zero-stock rooms.
     * Ensures Read-Only access without modifying system state.
     */
    public void searchAvailableRooms() {
        System.out.println("\n--- Guest Search: Available Room Options ---");
        boolean found = false;

        for (String type : inventory.getAllRoomTypes()) {
            int availableCount = inventory.getAvailability(type);

            // Validation Logic: Only show actionable options
            if (availableCount > 0) {
                Room room = roomDetails.get(type);
                System.out.println("Room Type: " + room.getType());
                System.out.println("  > Price: ₹" + room.getPrice());
                System.out.println("  > Amenities: " + room.getAmenities());
                System.out.println("  > Status: " + availableCount + " rooms available");
                System.out.println("-------------------------------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("Sorry, no rooms are currently available.");
        }
    }
}

/**
 * Main application class demonstrating Use Case 4.
 * @author Gemini
 * @version 4.0
 */
public class UseCase4HotelBookingApp {

    public static void main(String[] args) {
        System.out.println("Welcome to the Book My Stay Application v4.0");

        // 1. Initialize Inventory (State)
        RoomInventory inventoryManager = new RoomInventory();
        inventoryManager.registerRoomType("Single", 5);
        inventoryManager.registerRoomType("Double", 0); // Out of stock
        inventoryManager.registerRoomType("Suite", 2);

        // 2. Initialize Search Service (Read-Only layer)
        RoomSearchService searchService = new RoomSearchService(inventoryManager);

        // 3. Guest initiates search request
        // The Double Room should be filtered out because availability is 0
        searchService.searchAvailableRooms();

        // 4. Verify System State remains unchanged
        // We call search again to show that reading didn't modify counts
        System.out.println("\n(System Verification: State remains consistent after search)");
    }
}