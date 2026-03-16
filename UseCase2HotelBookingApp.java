/**
 * Abstract class representing a generalized Room.
 * Defines common attributes like bed count and price.
 */
abstract class Room {
    private String type;
    private int beds;
    private double price;

    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public String getType() { return type; }
    public int getBeds() { return beds; }
    public double getPrice() { return price; }

    /**
     * Abstract method to describe room-specific features.
     */
    public abstract void displayFeatures();
}

/**
 * Concrete implementation for a Single Room.
 */
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1000.0);
    }
    @Override
    public void displayFeatures() {
        System.out.println("Features: Compact space, high-speed WiFi, perfect for solo travelers.");
    }
}

/**
 * Concrete implementation for a Double Room.
 */
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 1800.0);
    }
    @Override
    public void displayFeatures() {
        System.out.println("Features: Spacious layout, twin beds, ideal for couples or friends.");
    }
}

/**
 * Concrete implementation for a Suite Room.
 */
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 3500.0);
    }
    @Override
    public void displayFeatures() {
        System.out.println("Features: Luxury living area, mini-bar, and panoramic city views.");
    }
}

/**
 * Main application class for Use Case 2.
 * Demonstrates inheritance, polymorphism, and static availability.
 * * @author Gemini
 * @version 1.0
 */
public class UseCase2HotelBookingApp {

    public static void main(String[] args) {
        System.out.println("--- Welcome to Book My Stay: Room Overview ---");

        // 1. Initializing Room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRm = new DoubleRoom();
        Room suite = new SuiteRoom();

        // 2. Static Availability Representation 
        // Note: Using individual variables shows the limitation before using HashMaps/Lists.
        int singleRoomAvailability = 5;
        int doubleRoomAvailability = 3;
        int suiteRoomAvailability = 2;

        // 3. Displaying details using the Room references
        displayRoomInfo(single, singleRoomAvailability);
        displayRoomInfo(doubleRm, doubleRoomAvailability);
        displayRoomInfo(suite, suiteRoomAvailability);

        System.out.println("----------------------------------------------");
    }

    /**
     * Helper method demonstrating polymorphism by accepting the abstract Room type.
     */
    private static void displayRoomInfo(Room room, int availability) {
        System.out.println("\nRoom Type: " + room.getType());
        System.out.println("Beds: " + room.getBeds());
        System.out.println("Price per Night: ₹" + room.getPrice());
        System.out.println("Current Availability: " + availability + " rooms left");
        room.displayFeatures();
    }
}