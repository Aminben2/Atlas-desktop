public class Pays {
    private String nom;
    private String capitale;
    private int populution;
    private String continent;

    public Pays(String nom, String capitale, int populution, String continent) {
        this.nom = nom;
        this.capitale = capitale;
        this.populution = populution;
        this.continent = continent;
    }

    public Pays(){}

    public String getNom() {
        return nom;
    }

    public String getCapitale() {
        return capitale;
    }

    public int getPopulution() {
        return populution;
    }

    public String getContinent() {
        return continent;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCapitale(String capitale) {
        this.capitale = capitale;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public void setPopulution(int populution) {
        this.populution = populution;
    }

    public String toString() {
        return nom+";"+capitale+";"+populution+";"+continent;
    }
}
