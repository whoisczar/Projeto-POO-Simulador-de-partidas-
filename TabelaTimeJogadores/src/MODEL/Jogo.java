package MODEL;

public class Jogo {
    private int id;
    private int timeAId; // Armazena o ID do Time A
    private int timeBId; // Armazena o ID do Time B
    private String timeA; // Nome do Time A
    private String timeB; // Nome do Time B
    private String data; // Você pode usar java.util.Date se preferir
    private String resultado;

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimeAId() {
        return timeAId;
    }

    public void setTimeAId(int timeAId) {
        this.timeAId = timeAId;
    }

    public int getTimeBId() {
        return timeBId;
    }

    public void setTimeBId(int timeBId) {
        this.timeBId = timeBId;
    }

    public String getTimeA() {
        return timeA;
    }

    public void setTimeA(String timeA) {
        this.timeA = timeA;
    }

    public String getTimeB() {
        return timeB;
    }

    public void setTimeB(String timeB) {
        this.timeB = timeB;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
