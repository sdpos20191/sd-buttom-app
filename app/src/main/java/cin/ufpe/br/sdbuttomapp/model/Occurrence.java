package cin.ufpe.br.sdbuttomapp.model;

public class Occurrence {

    private long timestamp;
    private double lat;
    private double lng;

    public Occurrence(long timestamp, double lat, double lng) {
        this.timestamp = timestamp;
        this.lat = lat;
        this.lng = lng;
    }
}
