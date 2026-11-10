package model;

public class Client {
    private int id;
    private String nume;
    private String email;
    private String telefon;

    public Client(int id, String nume, String email, String telefon) {
        this.id = id;
        this.nume = nume;
        this.email = email;
        this.telefon = telefon;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public String getEmail() { return email; }
    public String getTelefon() { return telefon; }
}

