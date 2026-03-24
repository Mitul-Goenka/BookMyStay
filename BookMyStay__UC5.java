
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String guestName, String roomType, int nights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "guestName='" + guestName + '\'' +
                ", roomType='" + roomType + '\'' +
                ", nights=" + nights +
                '}';
    }
}


import java.util.LinkedList;
import java.util.Queue;

class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }


    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added: " + reservation);
    }


    public Reservation peekNextRequest() {
        return requestQueue.peek();
    }


    public Reservation pollNextRequest() {
        return requestQueue.poll();
    }


    public boolean isEmpty() {
        return requestQueue.isEmpty();
    }


    public int getQueueSize() {
        return requestQueue.size();
    }
}


public class BookMyStay_UC5 {

    public static void main(String[] args) {

        BookingRequestQueue bookingQueue = new BookingRequestQueue();


        bookingQueue.addRequest(new Reservation("Alice", "Single Room", 2));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room", 3));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room", 1));

        System.out.println("\nTotal booking requests in queue: " + bookingQueue.getQueueSize());
        System.out.println("Next request to process: " + bookingQueue.peekNextRequest());


    }
}