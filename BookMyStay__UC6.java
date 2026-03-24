import java.util.*;

// InventoryService manages availability and assigned rooms
class InventoryService {
    private Map<String, Integer> inventory;                  // Room type -> availability
    private Map<String, Set<String>> allocatedRooms;         // Room type -> set of allocated room IDs
    private Map<String, Integer> roomCounters;               // Room type -> counter to generate unique room IDs

    public InventoryService() {
        inventory = new HashMap<>();
        allocatedRooms = new HashMap<>();
        roomCounters = new HashMap<>();

        // Initialize inventory
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);

        // Initialize allocatedRooms map with empty sets
        allocatedRooms.put("Single Room", new HashSet<>());
        allocatedRooms.put("Double Room", new HashSet<>());
        allocatedRooms.put("Suite Room", new HashSet<>());

        // Initialize room counters starting at 1
        roomCounters.put("Single Room", 1);
        roomCounters.put("Double Room", 1);
        roomCounters.put("Suite Room", 1);
    }

    // Check availability for a room type
    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    // Allocate a unique room ID and decrement inventory atomically
    public synchronized String allocateRoom(String roomType) {
        if (!isAvailable(roomType)) {
            return null; // No availability
        }

        // Generate unique room ID
        int count = roomCounters.get(roomType);
        String roomId = roomType.replace(" ", "").substring(0, 2).toUpperCase() + String.format("%03d", count);

        // Ensure uniqueness (usually guaranteed by counter, but just in case)
        Set<String> allocatedSet = allocatedRooms.get(roomType);
        while (allocatedSet.contains(roomId)) {
            count++;
            roomId = roomType.replace(" ", "").substring(0, 2).toUpperCase() + String.format("%03d", count);
        }

        // Update counter
        roomCounters.put(roomType, count + 1);

        // Record allocation
        allocatedSet.add(roomId);

        // Decrement inventory
        inventory.put(roomType, inventory.get(roomType) - 1);

        return roomId;
    }

    // Get current inventory state (read-only)
    public Map<String, Integer> getInventorySnapshot() {
        return Collections.unmodifiableMap(inventory);
    }

    // Get allocated rooms snapshot (read-only)
    public Map<String, Set<String>> getAllocatedRoomsSnapshot() {
        Map<String, Set<String>> snapshot = new HashMap<>();
        for (String roomType : allocatedRooms.keySet()) {
            snapshot.put(roomType, Collections.unmodifiableSet(allocatedRooms.get(roomType)));
        }
        return snapshot;
    }

    // Display inventory and allocated rooms (for debugging)
    public void displayStatus() {
        System.out.println("===== Inventory =====");
        inventory.forEach((roomType, count) -> System.out.println(roomType + ": Available = " + count));
        System.out.println("\n===== Allocated Rooms =====");
        allocatedRooms.forEach((roomType, rooms) -> System.out.println(roomType + ": " + rooms));
        System.out.println("========================\n");
    }
}

// BookingService processes booking requests and performs allocations
class BookingService {
    private BookingRequestQueue requestQueue;
    private InventoryService inventoryService;

    public BookingService(BookingRequestQueue requestQueue, InventoryService inventoryService) {
        this.requestQueue = requestQueue;
        this.inventoryService = inventoryService;
    }

    // Process next booking request in FIFO order
    public void processNextRequest() {
        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests to process.");
            return;
        }

        Reservation reservation = requestQueue.pollNextRequest();
        String requestedRoomType = reservation.getRoomType();

        System.out.println("Processing booking for guest: " + reservation.getGuestName() +
                ", Room Type: " + requestedRoomType);

        // Check availability and allocate room
        if (inventoryService.isAvailable(requestedRoomType)) {
            String assignedRoomId = inventoryService.allocateRoom(requestedRoomType);
            System.out.println("Booking confirmed! Assigned Room ID: " + assignedRoomId +
                    " for " + reservation.getGuestName());
        } else {
            System.out.println("Booking failed: No available rooms of type " + requestedRoomType +
                    " for " + reservation.getGuestName());
        }
    }
}

// Main class demonstrating complete flow
public class BookMyStay_UC6 {

    public static void main(String[] args) {

        // Initialize services and queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        InventoryService inventoryService = new InventoryService();
        BookingService bookingService = new BookingService(bookingQueue, inventoryService);

        // Guests submit booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room", 2));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room", 3));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room", 1));
        bookingQueue.addRequest(new Reservation("Diana", "Single Room", 1));
        bookingQueue.addRequest(new Reservation("Evan", "Suite Room", 2));
        bookingQueue.addRequest(new Reservation("Fiona", "Single Room", 1)); // This may fail if no Single Rooms left

        System.out.println("\nInitial Inventory & Allocations:");
        inventoryService.displayStatus();

        // Process all requests one by one
        while (!bookingQueue.isEmpty()) {
            bookingService.processNextRequest();
            System.out.println();
            inventoryService.displayStatus();
        }
    }
}