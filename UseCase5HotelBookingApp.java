import java.util.*;

/**
 * Reservation class represents a guest's intent to book a room.
 * It encapsulates the request data without modifying the system state.
 */
class ReservationRequest {
    private String guestName;
    private String roomType;
    private long timestamp;

    public ReservationRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.timestamp = System.currentTimeMillis();
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    @Override
    public String toString() {
        return "Request [Guest: " + guestName + ", Room: " + roomType + "]";
    }
}

/**
 * BookingRequestQueue manages the intake of requests.
 * It ensures fairness using the FIFO (First-In-First-Out) principle.
 */
class BookingRequestQueue {
    // Queue preserves the order of arrival
    private final Queue<ReservationRequest> requestQueue;

    public BookingRequestQueue() {
        this.requestQueue = new LinkedList<>();
    }

    /**
     * Requirement: Accept booking requests and store them in a queue.
     */
    public void enqueueRequest(ReservationRequest request) {
        requestQueue.add(request);
        System.out.println("Queued: " + request);
    }

    /**
     * Requirement: Prepare requests for subsequent processing.
     * This method would be used by the allocation system later.
     */
    public ReservationRequest nextRequest() {
        return requestQueue.poll();
    }

    public boolean isEmpty() {
        return requestQueue.isEmpty();
    }

    /**
     * Displays the current state of the queue to show ordering.
     */
    public void displayQueueStatus() {
        System.out.println("\n--- Current Booking Request Queue (Waiting List) ---");
        if (requestQueue.isEmpty()) {
            System.out.println("No pending requests.");
        } else {
            requestQueue.forEach(req -> System.out.println(" >> " + req));
        }
        System.out.println("----------------------------------------------------\n");
    }
}

/**
 * Main application class demonstrating Use Case 5.
 * @author Gemini
 * @version 5.0
 */
public class UseCase5HotelBookingApp {

    public static void main(String[] args) {
        System.out.println("Welcome to the Book My Stay Application v5.0\n");

        // 1. Initialize the Booking Request Queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // 2. Simulate guests submitting requests (Arrival Order)
        System.out.println("Incoming Booking Requests...");
        bookingQueue.enqueueRequest(new ReservationRequest("Alice", "Suite"));
        bookingQueue.enqueueRequest(new ReservationRequest("Bob", "Single"));
        bookingQueue.enqueueRequest(new ReservationRequest("Charlie", "Double"));
        bookingQueue.enqueueRequest(new ReservationRequest("Diana", "Suite"));

        // 3. Preserve and display arrival order (FIFO Principle)
        bookingQueue.displayQueueStatus();

        // 4. Verify no inventory mutation occurs at this stage
        System.out.println("Note: Requests are queued. No rooms have been allocated yet.");
        System.out.println("System is ready for fair processing.");
    }
}