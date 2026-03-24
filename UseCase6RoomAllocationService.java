import java.util.*;

public class UseCase6RoomAllocationService {

    // Inventory: Maps Room Type to available count
    private static Map<String, Integer> inventory = new HashMap<>();
    
    // Request Queue: FIFO order for processing
    private static Queue<BookingRequest> requestQueue = new LinkedList<>();
    
    // Allocation Tracking: Maps Room Type to a Set of assigned Room IDs
    // The Set ensures that no Room ID is duplicated (No double-booking)
    private static Map<String, Set<String>> allocatedRooms = new HashMap<>();

    static class BookingRequest {
        String guestName;
        String roomType;

        BookingRequest(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    public static void main(String[] args) {
        // 1. Initialize Inventory
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);

        // 2. Initialize Allocation Sets for each room type
        allocatedRooms.put("Deluxe", new HashSet<>());
        allocatedRooms.put("Suite", new HashSet<>());

        // 3. Queueing incoming requests (FIFO)
        requestQueue.add(new BookingRequest("Alice", "Deluxe"));
        requestQueue.add(new BookingRequest("Bob", "Deluxe"));
        requestQueue.add(new BookingRequest("Charlie", "Deluxe")); // Should fail (no inventory)
        requestQueue.add(new BookingRequest("Diana", "Suite"));

        System.out.println("--- Processing Room Allocations ---");
        processAllocations();
    }

    public static void processAllocations() {
        while (!requestQueue.isEmpty()) {
            BookingRequest request = requestQueue.poll();
            String type = request.roomType;

            // Check if room type exists and has availability
            if (inventory.containsKey(type) && inventory.get(type) > 0) {
                
                // Generate a unique Room ID (e.g., Deluxe-101)
                // In a real system, this would pull from a list of physical rooms
                String roomID = type + "-" + (100 + inventory.get(type));

                // Atomic-like Logic: Assign and Update
                if (!allocatedRooms.get(type).contains(roomID)) {
                    allocatedRooms.get(type).add(roomID); // Record to prevent reuse
                    inventory.put(type, inventory.get(type) - 1); // Decrement inventory
                    
                    System.out.println("CONFIRMED: " + request.guestName + 
                                       " assigned to " + roomID);
                }
            } else {
                System.out.println("FAILED: No availability for " + request.guestName + 
                                   " (" + type + ")");
            }
        }
        
        System.out.println("\n--- Final Inventory State ---");
        inventory.forEach((k, v) -> System.out.println(k + " rooms left: " + v));
    }
}