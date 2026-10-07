public abstract class Pengguna {

    private String nama;
    private String telp;

    public Pengguna(String nama, String telp) {
        this.nama = nama;
        this.telp = telp;
    }

    public String getNama() {
        return nama;
    }

    public String getTelp() {
        return telp;
    }

    // ABSTRACT METHOD
    public abstract float hitungDiskon(float harga);
}