package Entity;

public class Strumento {
    private String nomestrumento;
    private String nomesat;
    private Double banda;

    public Strumento(String nomestrumento,String nomesat, double banda) {
        this.nomestrumento = nomestrumento;
        this.nomesat = nomesat;
        this.banda = banda;
    }

    public String getNomeStrumento() {
        return nomestrumento;
    }

    public void setNomeStrumento(String nomestrumento) {
        this.nomestrumento = nomestrumento;
    }

    public String getNomeSat() {
        return nomesat;
    }

    public void setNomeSat(String nomesat) {
        this.nomesat = nomesat;
    }

    public Double getBanda() {
        return banda;
    }

    public void setBanda(Double banda) {
        this.banda = banda;
    }
}