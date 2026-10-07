import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class App {

    private static final Path DRIVER_FILE = cariFileDriver();

    private static Path cariFileDriver() {
        Path[] kandidat = {
            Path.of("Driver.txt"),
            Path.of("src", "Driver.txt")
        };

        for (Path path : kandidat) {
            if (Files.exists(path)) {
                return path;
            }
        }

        try {
            Path lokasiClass = Path.of(
                App.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
            );

            if (Files.isDirectory(lokasiClass)) {
                Path dariBin = lokasiClass.resolve("Driver.txt");
                if (Files.exists(dariBin)) {
                    return dariBin;
                }

                Path rootProject = lokasiClass.getParent();
                if (rootProject != null) {
                    Path dariRoot = rootProject.resolve("Driver.txt");
                    if (Files.exists(dariRoot)) {
                        return dariRoot;
                    }
                }
            }
        } catch (Exception e) {
            // Jika path proyek tidak dapat dideteksi, lanjutkan ke pengecekan biasa.
        }

        return Path.of("Driver.txt");
    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("========== U-RIDE ==========");
        System.out.println("INPUT DATA CUSTOMER");
        System.out.println("Silahkan isi data customer yang sesuai.");
        System.out.println();

        // =========================
        // INPUT CUSTOMER
        // Customer selalu diinput setiap membuat order
        // =========================
        String nama;

        do {
            System.out.print("Nama Customer : ");
            nama = input.nextLine();

            if (nama.trim().isEmpty()) {
                System.out.println("Nama customer tidak boleh kosong.");
            }
        } while (nama.trim().isEmpty());

        String telp;

        do {
            System.out.print("No. Telepon   : ");
            telp = input.nextLine();

            if (!telp.matches("08\\d{8,11}")) {
                System.out.println(
                    "Nomor telepon harus diawali 08 "
                    + "dan berjumlah 10-13 digit."
                );
            }
        } while (!telp.matches("08\\d{8,11}"));

        Customer customer = new Customer(nama, telp);

        // =========================
        // INPUT PESANAN
        // =========================
        System.out.println();

        System.out.print("Lokasi Pickup : ");
        String pickup = input.nextLine();

        System.out.print("Lokasi Tujuan : ");
        String tujuan = input.nextLine();

        // =========================
        // DRIVER RANDOM DARI TXT
        // Driver dicari setelah lokasi dimasukkan
        // =========================
        Driver driver = pilihDriverRandom();

        if (driver == null) {
            System.out.println("Tidak ada data driver di Driver.txt.");
            input.close();
            return;
        }

        System.out.println();
        System.out.println("Driver berhasil ditemukan!");
        System.out.println("Driver     : " + driver.getNama());
        System.out.println("Kendaraan  : " + driver.getJenisKendaraan());
        System.out.println("Plat Nomor : " + driver.getPlatNomor());

        // =========================
        // RATING
        // =========================
        int rating;

        do {
            System.out.print("Rating (1-5) : ");
            rating = input.nextInt();

            if (rating < 1 || rating > 5) {
                System.out.println("Rating harus antara 1 sampai 5.");
            }
        } while (rating < 1 || rating > 5);

        // =========================
        // TIP
        // =========================
        System.out.print(
            "Apakah ingin memberikan tip? (y/n) : "
        );

        String pilihanTip = input.next();

        float tip = 0;

        if (pilihanTip.equalsIgnoreCase("y")) {

            do {
                System.out.print("Masukkan jumlah tip : Rp");
                tip = input.nextFloat();

                if (tip < 0) {
                    System.out.println(
                        "Tip tidak boleh kurang dari 0."
                    );
                }
            } while (tip < 0);

        } else {
            System.out.println("Tidak memberikan tip.");
        }

        // =========================
        // TAMPILKAN HARGA
        // =========================
        System.out.println();
        System.out.println("========== DETAIL PEMBAYARAN ==========");

        float harga = 50000.0f;
        float diskon = customer.hitungDiskon(harga);
        float total = harga - diskon + tip;

        System.out.printf(
            "Harga perjalanan : Rp%,.0f%n",
            harga
        );

        System.out.printf(
            "Diskon           : Rp%,.0f%n",
            diskon
        );

        System.out.printf(
            "Tip              : Rp%,.0f%n",
            tip
        );

        System.out.printf(
            "Total pembayaran : Rp%,.0f%n",
            total
        );

        System.out.println("=======================================");
        System.out.println();

        // =========================
        // PEMBAYARAN
        // =========================
        System.out.println("1. GoPay");
        System.out.println("2. Cash");

        int pilihan;

        while (true) {
            System.out.print("Pilih pembayaran : ");

            if (input.hasNextInt()) {
                pilihan = input.nextInt();

                if (pilihan == 1 || pilihan == 2) {
                    break;
                }
            } else {
                input.next();
            }

            System.out.println(
                "Pilihan tidak valid. Masukkan 1 untuk GoPay atau 2 untuk Cash."
            );
        }

        JenisPembayaran pembayaran;

        if (pilihan == 1) {
            pembayaran = new JenisPembayaran("GoPay");
        } else {
            pembayaran = new JenisPembayaran("Cash");
        }

        // =========================
        // BUAT ORDER
        // =========================
        Order order = new Order(
            "UR-" + System.currentTimeMillis(),
            customer,
            driver,
            pembayaran,
            pickup,
            tujuan
        );

        order.setRating(rating);
        order.setTip(tip);

        // =========================
        // PEMBAYARAN
        // =========================
        pembayaran.bayar(order.hitungTotal());

        // =========================
        // CETAK STRUK
        // =========================
        order.cetakStruk();

        // Popup struk ditampilkan dari Order.java

        input.close();
    }

    private static Driver pilihDriverRandom() {

        List<Driver> daftarDriver = new ArrayList<>();

        try {
            if (!Files.exists(DRIVER_FILE)) {
                System.out.println("Driver.txt tidak ditemukan.");
                return null;
            }

            List<String> baris = Files.readAllLines(DRIVER_FILE);

            for (String data : baris) {
                if (data.trim().isEmpty()) {
                    continue;
                }

                String[] bagian = data.split("\\|", -1);

                if (bagian.length == 4) {
                    Driver driver = new Driver(
                        bagian[0].trim(),
                        bagian[1].trim(),
                        bagian[2].trim(),
                        bagian[3].trim()
                    );

                    daftarDriver.add(driver);
                }
            }

        } catch (IOException e) {
            System.out.println("Gagal membaca Driver.txt.");
            return null;
        }

        if (daftarDriver.isEmpty()) {
            return null;
        }

        Random random = new Random();

        return daftarDriver.get(
            random.nextInt(daftarDriver.size())
        );
    }
}
