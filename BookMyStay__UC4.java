import java.util.ArrayList;
import java.util.List;
import java.util.Map;


class RoomSearchService {

    private RoomInventory inventory;
    private List<Room> rooms;

    public RoomSearchService(RoomInventory inventory, List<Room> rooms) {
        this.inventory = inventory;
        this.rooms = rooms;
    }


    public void showAvailableRooms() {
        System.out.println("===== Available Rooms for Guests =====");

        for (Room room : rooms) {
            String type = room.getRoomType();
            int available = inventory.getAvailability(type);

            if (available > 0) {
                room.displayRoomInfo();
                room.showFeatures();
                System.out.println("Available: " + available);
                System.out.println("-----------------------------------");
            }
        }

        System.out.println("=====================================");
    }
}


public class BookMyStay_UC4 {

    public static void main(String[] args) {


        RoomInventory inventory = new RoomInventory();

        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());


        RoomSearchService searchService = new RoomSearchService(inventory, rooms);


        searchService.showAvailableRooms();

        System.out.println("\nInventory remains unchanged after search:");
        inventory.displayInventory();
    }
}