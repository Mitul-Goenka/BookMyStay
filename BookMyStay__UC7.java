import java.util.*;

class Service {
    private String serviceName;
    private double price;

    public Service(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + price + ")";
    }
}


class AddOnServiceManager {

    private Map<String, List<Service>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }


    public void addService(String reservationId, Service service) {
        reservationServices.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
        System.out.println("Added service " + service + " to reservation " + reservationId);
    }


    public List<Service> getServices(String reservationId) {
        return reservationServices.getOrDefault(reservationId, Collections.emptyList());
    }


    public double calculateTotalCost(String reservationId) {
        List<Service> services = getServices(reservationId);
        return services.stream().mapToDouble(Service::getPrice).sum();
    }


    public void displayServices(String reservationId) {
        List<Service> services = getServices(reservationId);
        if (services.isEmpty()) {
            System.out.println("No add-on services for reservation " + reservationId);
        } else {
            System.out.println("Add-on services for reservation " + reservationId + ":");
            for (Service s : services) {
                System.out.println("- " + s);
            }
            System.out.printf("Total additional cost: ₹%.2f%n", calculateTotalCost(reservationId));
        }
    }
}


public class BookMyStay_UC7 {

    public static void main(String[] args) {

        AddOnServiceManager serviceManager = new AddOnServiceManager();


        String reservation1 = "SI001";
        String reservation2 = "DO001";


        Service breakfast = new Service("Breakfast", 250);
        Service airportPickup = new Service("Airport Pickup", 500);
        Service spaAccess = new Service("Spa Access", 1000);


        serviceManager.addService(reservation1, breakfast);
        serviceManager.addService(reservation1, airportPickup);
        serviceManager.addService(reservation2, spaAccess);

        System.out.println();


        serviceManager.displayServices(reservation1);
        System.out.println();
        serviceManager.displayServices(reservation2);
        System.out.println();


        System.out.println("Core booking and inventory state unaffected by add-ons.");
    }
}