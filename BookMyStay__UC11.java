import java.util.*;
import java.util.concurrent.*;

class ReservationRequest {
    String guestName;
    String roomType;
    int nights;

    ReservationRequest(String guestName, String roomType, int nights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }
}

class ThreadSafeInventory {
    private Map<String, Integer> inventory;

    ThreadSafeInventory(Map<String, Integer> initialInventory) {
        this.inventory = new HashMap<>(initialInventory);
    }

    synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    synchronized void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

class BookingProcessor implements Runnable {
    private ReservationRequest request;
    private ThreadSafeInventory inventory;

    BookingProcessor(ReservationRequest request, ThreadSafeInventory inventory) {
        this.request = request;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        if (inventory.allocateRoom(request.roomType)) {
            System.out.println("Booking successful for " + request.guestName + " (" + request.roomType + ")");
        } else {
            System.out.println("Booking failed for " + request.guestName + " (" + request.roomType + ")");
        }
    }
}

public class BookMyStay_UC11 {
    public static void main(String[] args) throws InterruptedException {
        Map<String, Integer> initialInventory = new HashMap<>();
        initialInventory.put("Single Room", 2);
        initialInventory.put("Double Room", 2);
        initialInventory.put("Suite Room", 1);

        ThreadSafeInventory inventory = new ThreadSafeInventory(initialInventory);

        List<ReservationRequest> requests = Arrays.asList(
                new ReservationRequest("Alice", "Single Room", 2),
                new ReservationRequest("Bob", "Single Room", 1),
                new ReservationRequest("Charlie", "Single Room", 1),
                new ReservationRequest("Diana", "Double Room", 2),
                new ReservationRequest("Eve", "Suite Room", 1)
        );

        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (ReservationRequest r : requests) {
            executor.submit(new BookingProcessor(r, inventory));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        inventory.displayInventory();
    }
}