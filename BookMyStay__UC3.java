
import java.util.HashMap;
import java.util.Map;

class RoomInventory {


    private Map<String, Integer> inventory;


    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }


    public void updateAvailability(String roomType, int count) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, count);
        } else {
            System.out.println("Room type not found!");
        }
    }


    public boolean bookRoom(String roomType) {
        int available = getAvailability(roomType);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        } else {
            return false;
        }
    }


    public void displayInventory() {
        System.out.println("===== Current Room Inventory =====");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
        System.out.println("==================================");
    }
}


public class BookMyStay_UC3 {

    public static void main(String[] args) {


        RoomInventory inventory = new RoomInventory();


        inventory.displayInventory();


        System.out.println("\nChecking availability for Single Room:");
        System.out.println("Available: " + inventory.getAvailability("Single Room"));


        System.out.println("\nBooking Single Room...");
        boolean booked = inventory.bookRoom("Single Room");

        if (booked) {
            System.out.println("Booking successful!");
        } else {
            System.out.println("No rooms available!");
        }

        System.out.println();
        inventory.displayInventory();
    }
}