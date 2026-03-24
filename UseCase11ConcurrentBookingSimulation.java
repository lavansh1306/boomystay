import java.util.*;
import java.util.concurrent.*;

public class UseCase11ConcurrentBookingSimulation {

    // Shared Mutable State: Room inventory
    private static final Map<String, Integer> inventory = new HashMap<>();
    
    // Shared List for confirmed bookings (Thread-safe version)
    private static final List<String> confirmations = Collections.synchronizedList(new ArrayList<>());

    static {
        inventory.put("Deluxe", 5); // Only 5 rooms available
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- Starting Concurrent Booking Simulation ---");
        System.out.println("Initial Deluxe Inventory: " + inventory.get("Deluxe"));

        // Create a thread pool to simulate 10 guests booking simultaneously
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 1; i <= 10; i++) {
            String guestName = "Guest-" + i;
            executor.execute(() -> attemptBooking(guestName, "Deluxe"));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\n--- Final System State ---");
        System.out.println("Total Successful Bookings: " + confirmations.size());
        System.out.println("Final Deluxe Inventory: " + inventory.get("Deluxe"));
        
        // Verification: If logic is correct, Inventory + Confirmations must = 5
        if (confirmations.size() > 5) {
            System.err.println("CRITICAL ERROR: Double-booking detected!");
        } else {
            System.out.println("SUCCESS: No double-bookings occurred.");
        }
    }

    /**
     * Critical Section: Only one thread can execute this block at a time
     * to prevent race conditions during inventory checks and updates.
     */
    public static void attemptBooking(String guest, String type) {
        // Synchronizing on the inventory object to ensure thread safety
        synchronized (inventory) {
            Integer count = inventory.get(type);

            if (count != null && count > 0) {
                // Simulate some processing delay to expose potential race conditions
                try { Thread.sleep(50); } catch (InterruptedException e) {}

                // Update shared state
                inventory.put(type, count - 1);
                confirmations.add(guest + " reserved " + type);
                
                System.out.println("[Thread: " + Thread.currentThread().getId() + "] " + 
                                   guest + " successfully booked " + type + 
                                   ". Remaining: " + (count - 1));
            } else {
                System.out.println("[Thread: " + Thread.currentThread().getId() + "] " + 
                                   guest + " FAILED: No rooms left.");
            }
        }
    }
}