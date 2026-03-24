import java.io.*;
import java.util.*;

class PersistentBookingSystem implements Serializable {
    private Map<String, Integer> inventory;
    private List<Reservation> bookingHistory;

    public PersistentBookingSystem(Map<String, Integer> inventory, List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public List<Reservation> getBookingHistory() {
        return bookingHistory;
    }
}

class PersistenceService {

    private static final String FILE_NAME = "booking_system_state.ser";

    public static void saveState(PersistentBookingSystem system) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(system);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save system state: " + e.getMessage());
        }
    }

    public static PersistentBookingSystem loadState() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No previous state found. Starting fresh.");
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (PersistentBookingSystem) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load system state: " + e.getMessage());
            return null;
        }
    }
}

public class BookMyStay_UC12 {

    public static void main(String[] args) {

        PersistentBookingSystem recoveredSystem = PersistenceService.loadState();

        Map<String, Integer> inventory;
        List<Reservation> bookingHistory;

        if (recoveredSystem == null) {
            inventory = new HashMap<>();
            inventory.put("Single Room", 5);
            inventory.put("Double Room", 3);
            inventory.put("Suite Room", 2);
            bookingHistory = new ArrayList<>();
        } else {
            inventory = recoveredSystem.getInventory();
            bookingHistory = recoveredSystem.getBookingHistory();
        }

        System.out.println("Current inventory: " + inventory);
        System.out.println("Booking history: " + bookingHistory);

        Reservation newBooking = new Reservation("Alice", "Single Room", 2);
        bookingHistory.add(newBooking);
        inventory.put("Single Room", inventory.get("Single Room") - 1);

        PersistentBookingSystem systemToSave = new PersistentBookingSystem(inventory, bookingHistory);
        PersistenceService.saveState(systemToSave);

        System.out.println("After booking:");
        System.out.println("Inventory: " + inventory);
        System.out.println("Booking history: " + bookingHistory);
    }
}