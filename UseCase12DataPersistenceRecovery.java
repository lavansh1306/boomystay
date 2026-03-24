import java.io.*;
import java.util.*;

/**
 * To be serializable, our Data Model classes must implement Serializable.
 */
class HotelState implements Serializable {
    private static final long serialVersionUID = 1L;
    Map<String, Integer> inventory;
    List<String> bookingHistory;

    HotelState(Map<String, Integer> inv, List<String> history) {
        this.inventory = inv;
        this.bookingHistory = history;
    }
}

public class UseCase12DataPersistenceRecovery {

    private static final String DATA_FILE = "hotel_state.ser";
    private static Map<String, Integer> currentInventory = new HashMap<>();
    private static List<String> currentHistory = new ArrayList<>();

    public static void main(String[] args) {
        // 1. System Startup: Attempt to restore state
        System.out.println("--- System Booting Up ---");
        loadState();

        // 2. Perform some business operations
        if (currentInventory.isEmpty()) {
            System.out.println("No existing state found. Initializing fresh inventory...");
            currentInventory.put("Deluxe", 10);
            currentInventory.put("Suite", 5);
        }

        System.out.println("Current Inventory: " + currentInventory);
        
        // Simulate a new booking
        String newBooking = "Guest-" + (currentHistory.size() + 1) + " | Deluxe";
        currentHistory.add(newBooking);
        currentInventory.put("Deluxe", currentInventory.get("Deluxe") - 1);
        
        System.out.println("Added New Booking: " + newBooking);

        // 3. System Shutdown: Persist state
        System.out.println("\n--- System Shutting Down ---");
        saveState();
        
        System.out.println("Check your folder for '" + DATA_FILE + "' to see the persisted data.");
    }

    /**
     * Persistence Service: Serializes objects to a file.
     */
    public static void saveState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            HotelState state = new HotelState(currentInventory, currentHistory);
            oos.writeObject(state);
            System.out.println("SUCCESS: System state serialized and saved to disk.");
        } catch (IOException e) {
            System.err.println("ERROR: Could not save state: " + e.getMessage());
        }
    }

    /**
     * Recovery Service: Deserializes objects from a file.
     */
    @SuppressWarnings("unchecked")
    public static void loadState() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("INFO: No persistence file detected. Starting with clean state.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            HotelState savedState = (HotelState) ois.readObject();
            currentInventory = savedState.inventory;
            currentHistory = savedState.bookingHistory;
            System.out.println("SUCCESS: Previous state recovered. History count: " + currentHistory.size());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("FAILURE: Recovery failed. The file might be corrupted. " + e.getMessage());
        }
    }
}