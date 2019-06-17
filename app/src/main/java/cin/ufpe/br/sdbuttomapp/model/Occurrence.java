package cin.ufpe.br.sdbuttomapp.model;

public class Occurrence {

    private String dataHora;
    private String id;
    private Location localizacao;

    public Occurrence(String dataHora, double lat, double lng) {
        this.dataHora = dataHora;
        this.localizacao = new Location(dataHora, lat, lng);
    }

    public Location getLocalizacao() {
        return localizacao;
    }
}
