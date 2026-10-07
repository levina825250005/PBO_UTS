public class Customer extends Pengguna {

    public Customer(String nama, String telp) {
        super(nama, telp);
    }

    @Override
    public float hitungDiskon(float harga) {

        if (harga >= 50000) {
            return harga * 0.10f;
        }

        return 0;
    }
}