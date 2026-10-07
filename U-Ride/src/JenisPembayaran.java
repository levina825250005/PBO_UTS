public class JenisPembayaran {

    private String jenis;

    public JenisPembayaran(String jenis) {
        this.jenis = jenis;
    }

    public String getJenis() {
        return jenis;
    }

    public void bayar(float jumlah) {

        if (jenis.equalsIgnoreCase("GoPay")) {
            System.out.println("Pembayaran GoPay berhasil.");
            System.out.printf(
                "Saldo GoPay berkurang sebesar Rp%,.0f%n",
                jumlah
            );
        } else {
            System.out.printf(
                "Pembayaran Cash berhasil: Rp%,.0f%n",
                jumlah
            );
        }
    }
}