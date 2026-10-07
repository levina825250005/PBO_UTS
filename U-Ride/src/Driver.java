public class Driver extends Pengguna {

    private String jenisKendaraan;
    private String platNomor;

    public Driver(
        String nama,
        String telp,
        String jenisKendaraan,
        String platNomor
    ) {
        super(nama, telp);
        this.jenisKendaraan = jenisKendaraan;
        this.platNomor = platNomor;
    }

    public String getJenisKendaraan() {
        return jenisKendaraan;
    }

    public String getPlatNomor() {
        return platNomor;
    }

    @Override
    public float hitungDiskon(float harga) {
        return 0;
    }
}