
abstract class Room {
    private String roomType;
    private int beds;
    private double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }


    public void displayRoomInfo() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price: ₹" + price);
    }


    public abstract void showFeatures();
}


class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 1000);
    }

    @Override
    public void showFeatures() {
        System.out.println("Features: Suitable for 1 person");
    }
}


class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 1800);
    }

    @Override
    public void showFeatures() {
        System.out.println("Features: Suitable for 2 persons");
    }
}


class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 3000);
    }

    @Override
    public void showFeatures() {
        System.out.println("Features: Luxury stay with extra space");
    }
}


public class BookMyStay_UC2 {

    public static void main(String[] args) {

        System.out.println("===== Available Room Types =====");


        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;


        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        System.out.println("\n--- Single Room ---");
        single.displayRoomInfo();
        single.showFeatures();
        System.out.println("Available: " + singleAvailable);

        System.out.println("\n--- Double Room ---");
        doubleRoom.displayRoomInfo();
        doubleRoom.showFeatures();
        System.out.println("Available: " + doubleAvailable);

        System.out.println("\n--- Suite Room ---");
        suite.displayRoomInfo();
        suite.showFeatures();
        System.out.println("Available: " + suiteAvailable);

        System.out.println("\n================================");
    }
}