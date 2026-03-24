import java.util.*;


class CancellationService {

    private InventoryService inventoryService;
    private BookingHistory bookingHistory;
    private Stack<String> rollbackStack;

    public CancellationService(InventoryService inventoryService, BookingHistory bookingHistory) {
        this.inventoryService = inventoryService;
        this.bookingHistory = bookingHistory;
        rollbackStack = new Stack<>();
    }

    public void cancelBooking(Reservation reservation, String roomId) {
        List<Reservation> allReservations = new ArrayList<>(bookingHistory.getAllReservations());


        if (!allReservations.contains(reservation)) {
            System.err.println("Cancellation failed: Reservation does not exist or already cancelled: " + reservation);
            return;
        }

        String roomType = reservation.getRoomType();

        inventoryService.incrementInventory(roomType);


        rollbackStack.push(roomId);

        allReservations.remove(reservation);


        bookingHistory.updateHistory(allReservations);

        System.out.println("Cancellation successful for " + reservation.getGuestName() +
                ", Room ID " + roomId + " restored to inventory.");
    }


    public void displayRollbackStack() {
        System.out.println("Rollback stack (released room IDs): " + rollbackStack);
    }
}


class InventoryServiceForUC10 extends InventoryService {

    public InventoryServiceForUC10() {
        super();
    }


    public void incrementInventory(String roomType) {
        int current = super.getInventorySnapshot().getOrDefault(roomType, 0);
        Map<String, Integer> mutableInventory = new HashMap<>(super.getInventorySnapshot());
        mutableInventory.put(roomType, current + 1);


        super.replaceInventory(mutableInventory);
    }
}


class BookingHistoryForUC10 extends BookingHistory {

    public void updateHistory(List<Reservation> updatedList) {
        super.replaceHistory(updatedList);
    }
}


public class BookMyStay_UC10 {

    public static void main(String[] args) {

        InventoryServiceForUC10 inventoryService = new InventoryServiceForUC10();
        BookingHistoryForUC10 bookingHistory = new BookingHistoryForUC10();

        Reservation r1 = new Reservation("Alice", "Single Room", 2);
        Reservation r2 = new Reservation("Bob", "Double Room", 3);


        bookingHistory.addReservation(r1);
        bookingHistory.addReservation(r2);

        System.out.println("\nBefore cancellation:");
        inventoryService.displayStatus();
        System.out.println("Booking history: " + bookingHistory.getAllReservations() + "\n");


        String roomIdR1 = "SI001";
        String roomIdR2 = "DO001";

        CancellationService cancellationService = new CancellationService(inventoryService, bookingHistory);


        cancellationService.cancelBooking(r1, roomIdR1);

        System.out.println("\nAfter cancellation:");
        inventoryService.displayStatus();
        System.out.println("Booking history: " + bookingHistory.getAllReservations());
        cancellationService.displayRollbackStack();
    }
}