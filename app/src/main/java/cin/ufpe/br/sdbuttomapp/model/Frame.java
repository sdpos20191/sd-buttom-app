package cin.ufpe.br.sdbuttomapp.model;

public class Frame {
    private String type;
    private Occurrence data;

    Frame(String type, Occurrence data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Occurrence getOccurrence() {
        return data;
    }

    public void setOccurrence(Occurrence occurrence) {
        this.data = occurrence;
    }
}
