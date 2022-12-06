package SUSTech_CS307_Project2.Backend;

import SUSTech_CS307_Project2.Courier;

public class CourierHandler {
    static Courier courier;

    public static Courier getCourier() {
        if (courier == null) {
            courier = new Courier();
        }
        return courier;
    }
}
