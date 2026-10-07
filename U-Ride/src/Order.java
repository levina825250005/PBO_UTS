import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Order implements CetakStruk {

    private String orderId;
    private float harga;

    private Customer customer;
    private Driver driver;
    private JenisPembayaran pembayaran;

    private String lokasiPickup;
    private String lokasiDropoff;

    private int rating;
    private float tip;

    private LocalDateTime waktuOrder;

    public Order(
        String orderId,
        Customer customer,
        Driver driver,
        JenisPembayaran pembayaran,
        String lokasiPickup,
        String lokasiDropoff
    ) {

        this.orderId = orderId;
        this.customer = customer;
        this.driver = driver;
        this.pembayaran = pembayaran;
        this.lokasiPickup = lokasiPickup;
        this.lokasiDropoff = lokasiDropoff;

        this.harga = 50000;
        this.rating = 0;
        this.tip = 0;
        this.waktuOrder = LocalDateTime.now();
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setTip(float tip) {
        this.tip = tip;
    }

    // Mengambil diskon dari Customer
    public float hitungDiskon() {
        return customer.hitungDiskon(harga);
    }

    // Menghitung total harga
    public float hitungTotal() {

        return harga
            - hitungDiskon()
            + tip;
    }

    // Implementasi interface
    @Override
    public void cetakStruk() {

        String struk = buatStruk();

        System.out.println();
        System.out.println(struk);

        simpanTransaksi(struk);

        // Tampilkan invoice dalam popup yang lebih rapi
        JTextArea area = new JTextArea(struk);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        area.setEditable(false);
        area.setLineWrap(false);
        area.setMargin(new Insets(16, 20, 16, 20));
        area.setBorder(BorderFactory.createEmptyBorder());

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 620));
        scrollPane.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        JOptionPane.showMessageDialog(
            null,
            scrollPane,
            "U-RIDE - Invoice Pesanan",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Membuat isi invoice
    private String buatStruk() {

        DateTimeFormatter format =
            DateTimeFormatter.ofPattern(
                "dd-MM-yyyy HH:mm:ss"
            );

        StringBuilder struk = new StringBuilder();

        struk.append(
            "==================================================\n"
        );
        struk.append(
            "                  U-RIDE\n"
        );
        struk.append(
            "                Untar Ride\n"
        );
        struk.append(
            "==================================================\n"
        );

        struk.append(
            String.format("%-14s : %s%n", "ID Pesanan", orderId)
        );
        struk.append(
            String.format(
                "%-14s : %s%n%n",
                "Waktu",
                waktuOrder.format(format)
            )
        );

        struk.append("CUSTOMER\n");
        struk.append("--------------------------------------------------\n");
        struk.append(
            String.format("%-14s : %s%n", "Nama", customer.getNama())
        );
        struk.append(
            String.format("%-14s : %s%n%n", "Telepon", customer.getTelp())
        );

        struk.append("DRIVER\n");
        struk.append("--------------------------------------------------\n");
        struk.append(
            String.format("%-14s : %s%n", "Nama", driver.getNama())
        );
        struk.append(
            String.format(
                "%-14s : %s%n",
                "Kendaraan",
                driver.getJenisKendaraan()
            )
        );
        struk.append(
            String.format(
                "%-14s : %s%n%n",
                "Plat",
                driver.getPlatNomor()
            )
        );

        struk.append("PERJALANAN\n");
        struk.append("--------------------------------------------------\n");
        struk.append(
            String.format(
                "%-14s : %s%n",
                "Jemput",
                lokasiPickup
            )
        );
        struk.append(
            String.format(
                "%-14s : %s%n%n",
                "Tujuan",
                lokasiDropoff
            )
        );

        float diskon = hitungDiskon();
        float total = hitungTotal();

        struk.append("PEMBAYARAN\n");
        struk.append("--------------------------------------------------\n");
        struk.append(
            String.format(
                "%-14s : Rp%,12.0f%n",
                "Harga",
                harga
            )
        );
        struk.append(
            String.format(
                "%-14s : Rp%,12.0f%n",
                "Diskon",
                diskon
            )
        );
        struk.append(
            String.format(
                "%-14s : Rp%,12.0f%n",
                "Tip",
                tip
            )
        );
        struk.append("--------------------------------------------------\n");
        struk.append(
            String.format(
                "%-14s : Rp%,12.0f%n",
                "Total",
                total
            )
        );
        struk.append(
            String.format(
                "%-14s : %s%n",
                "Metode",
                pembayaran.getJenis()
            )
        );
        struk.append(
            String.format(
                "%-14s : %d/5%n%n",
                "Rating",
                rating
            )
        );

        struk.append(
            "Terima kasih telah menggunakan U-RIDE!\n"
        );
        struk.append(
            "=================================================="
        );

        return struk.toString();
    }

    // Menyimpan transaksi ke file
    private void simpanTransaksi(String struk) {

        try (
            PrintWriter writer =
                new PrintWriter(
                    new FileWriter(
                        "Transaksi.txt",
                        true
                    )
                )
        ) {

            writer.println(struk);
            writer.println();

            System.out.println(
                "Transaksi berhasil disimpan ke Transaksi.txt"
            );

        } catch (IOException e) {

            System.out.println(
                "Gagal menyimpan transaksi."
            );
        }
    }
}