import java.util.*;

// BookingHistory maintains a chronological record of confirmed bookings
class BookingHistory {
    private List<Reservation> confirmedReservations;

    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    // Add a confirmed reservation to history
    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
        System.out.println("Booking added to history: " + reservation);
    }

    // Retrieve all confirmed reservations
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(confirmedReservations);
    }
}

// BookingReportService generates reports based on booking history
class BookingReportService {

    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    // Display all confirmed bookings
    public void displayAllBookings() {
        List<Reservation> reservations = bookingHistory.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("===== All Confirmed Bookings =====");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
        System.out.println("=================================\n");
    }

    // Generate a summary report: count per room type
    public void displayRoomTypeSummary() {
        List<Reservation> reservations = bookingHistory.getAllReservations();
        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : reservations) {
            summary.put(r.getRoomType(), summary.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("===== Booking Summary by Room Type =====");
        summary.forEach((roomType, count) ->
                System.out.println(roomType + ": " + count + " bookings")
        );
        System.out.println("========================================\n");
    }
}

// Main class to demonstrate booking history and reporting
public class BookMyStay_UC8 {

    public static void main(String[] args) {

        // Simulate confirmed reservations
        BookingHistory bookingHistory = new BookingHistory();
        BookingReportService reportService = new BookingReportService(bookingHistory);

        // Sample reservations (normally created during booking confirmation)
        Reservation r1 = new Reservation("Alice", "Single Room", 2);
        Reservation r2 = new Reservation("Bob", "Double Room", 3);
        Reservation r3 = new Reservation("Charlie", "Suite Room", 1);
        Reservation r4 = new Reservation("Diana", "Single Room", 1);

        // Add confirmed bookings to history
        bookingHistory.addReservation(r1);
        bookingHistory.addReservation(r2);
        bookingHistory.addReservation(r3);
        bookingHistory.addReservation(r4);

        System.out.println();

        // Admin generates reports
        reportService.displayAllBookings();
        reportService.displayRoomTypeSummary();
    }
}