import java.util.*;

// Custom exception for invalid bookings
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Validator class to check booking requests
class BookingValidator {

    private static final Set<String> VALID_ROOM_TYPES = new HashSet<>(
            Arrays.asList("Single Room", "Double Room", "Suite Room")
    );

    // Validate reservation fields
    public static void validateReservation(Reservation reservation) throws InvalidBookingException {
        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }
        if (!VALID_ROOM_TYPES.contains(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }
        if (reservation.getNights() <= 0) {
            throw new InvalidBookingException("Number of nights must be positive.");
        }
    }
}

// Demonstration of booking with validation
public class BookMyStay_UC9 {

    public static void main(String[] args) {

        List<Reservation> bookingRequests = Arrays.asList(
                new Reservation("Alice", "Single Room", 2),    // valid
                new Reservation("", "Double Room", 3),         // invalid name
                new Reservation("Charlie", "Penthouse", 1),    // invalid room type
                new Reservation("Diana", "Suite Room", 0)      // invalid nights
        );

        for (Reservation r : bookingRequests) {
            try {
                BookingValidator.validateReservation(r);
                System.out.println("Booking request valid: " + r);
                // Proceed with booking flow (allocation, history, etc.)
            } catch (InvalidBookingException e) {
                System.err.println("Booking request failed: " + e.getMessage());
                // Fail-fast, skip further processing for this request
            }
        }

        System.out.println("\nSystem continues running safely after invalid requests.");
    }
}