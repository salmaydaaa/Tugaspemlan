import java.util.Scanner;

class Pelanggan {
    String nomorPelanggan;
    String nama;
    double saldo;
    String pin;
    String statusAkun;  


    public Pelanggan(String nomorPelanggan, String nama, double saldo, String pin) { 
        this.nomorPelanggan = nomorPelanggan;
        this.nama = nama;
        this.saldo = saldo;
        this.pin = pin;
        this.statusAkun = "unblocked";  
    }

    public String getJenisPelanggan() {  
        String kodeAwal = nomorPelanggan.substring(0, 2);  
    
        if (kodeAwal.equals("38")) {
            return "Silver";
        } else if (kodeAwal.equals("56")) {
            return "Gold";
        } else if (kodeAwal.equals("74")) {
            return "Platinum";
        } else {
            return "Unknown";
        } 
    }

    public double hitungCashback(double totalPembelian) {
        double cashback = 0;
        
        System.out.println("Total Pembelian: " + totalPembelian);
        System.out.println("Jenis Pelanggan: " + getJenisPelanggan());
        
        if (totalPembelian > 1000000) {
            if (getJenisPelanggan().equals("Silver")) {
                cashback = 0.05 * totalPembelian; // cashback 5% untuk Silver
            } else if (getJenisPelanggan().equals("Gold")) {
                cashback = 0.07 * totalPembelian; // cashback 7% untuk Gold
            } else if (getJenisPelanggan().equals("Platinum")) {
                cashback = 0.10 * totalPembelian; // cashback 10% untuk Platinum
            }
        } else { 
            if (getJenisPelanggan().equals("Gold")) {
                cashback = 0.02 * totalPembelian; // cashback 2% untuk Gold dibawah 1jt
            } else if (getJenisPelanggan().equals("Platinum")) {
                cashback = 0.05 * totalPembelian; // cashback 5% untuk Platinum dibawah 1jt
            }
        }

       
        System.out.println("Cashback: " + String.format("%.2f", cashback));
        
        return cashback;
    }

    
    public boolean transaksiPembelian(double totalPembelian, String inputPin) {
        if (statusAkun.equals("blocked")) {  
            System.out.println("Akun Anda diblokir.");
            return false;
        }

       
        if (!pin.equals(inputPin)) {
            return false;
        }
       
        double cashback = hitungCashback(totalPembelian);
        double totalSetelahPembelian = totalPembelian - cashback;

        if (saldo - totalSetelahPembelian < 10000) {
            System.out.println("Saldo tidak mencukupi untuk transaksi. Pembelian gagal.");
            return false;
        }

        saldo -= totalSetelahPembelian;  
        saldo += cashback; 

        
        System.out.println("Pembelian berhasil! Cashback sebesar Rp" + String.format("%.2f", cashback) + " telah dikreditkan.");
        System.out.println("Saldo akhir Anda adalah: Rp" + String.format("%.2f", saldo));
        return true;    
    }

    
    public boolean topUp(double jumlahTopUp, String inputPin) { 
        if (statusAkun.equals("blocked")) {  
            System.out.println("Akun Anda diblokir.");
            return false;
        }

        
        if (!pin.equals(inputPin)) {
            return false;
        }

        saldo += jumlahTopUp;
        System.out.println("Top-up berhasil! Saldo Anda sekarang: Rp" + String.format("%.2f", saldo));
        return true;
    }

    public void blokirAkun() {
        statusAkun = "blocked";  
    }
}

public class tinySwalayan {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan nomor pelanggan: ");
        String nomorPelanggan = scanner.nextLine();

        System.out.print("Masukkan nama pelanggan: ");
        String nama = scanner.nextLine();

        System.out.print("Masukkan saldo awal pelanggan: Rp");
        double saldo = scanner.nextDouble();
        scanner.nextLine();  // Clear buffer

        System.out.print("Masukkan PIN pelanggan: ");
        String pin = scanner.nextLine();

        Pelanggan pelanggan = new Pelanggan(nomorPelanggan, nama, saldo, pin);

        int kesalahanPin = 0;

        while (true) {
            System.out.println("Sistem Transaksi Swalayan Tiny");
            System.out.println("1. Pembelian");
            System.out.println("2. Top-up");
            System.out.println("3. Keluar");
            System.out.print("Pilih menu (1/2/3): ");
            int menu = scanner.nextInt(); 
            scanner.nextLine(); 

            if (menu == 1) { // Pembelian
                System.out.print("Masukkan PIN: ");
                String pinInput = scanner.nextLine();

                if (!pinInput.equals(pelanggan.pin)) {
                    System.out.println("PIN salah!");
                    kesalahanPin++;
                    if (kesalahanPin == 3) {
                        pelanggan.blokirAkun();
                        System.out.println("Akun diblokir karena 3 kali kesalahan PIN.");
                    }
                    continue;
                }

                if (kesalahanPin < 3) {
                    System.out.print("Masukkan total pembelian: Rp");   
                    double totalPembelian = scanner.nextDouble(); 
                    if (pelanggan.transaksiPembelian(totalPembelian, pinInput)) {
                        kesalahanPin = 0; // Reset jika transaksi berhasil 
                    }
                }
            } else if (menu == 2) { // Top-up
                System.out.print("Masukkan PIN: ");
                String pinInput = scanner.nextLine();

                if (!pinInput.equals(pelanggan.pin)) {
                    System.out.println("PIN salah!");
                    kesalahanPin++;
                    if (kesalahanPin == 3) {
                        pelanggan.blokirAkun();
                        System.out.println("Akun diblokir karena 3 kali kesalahan PIN.");
                    }
                    continue;
                }
                
                if (kesalahanPin < 3) {
                    System.out.print("Masukkan jumlah top-up: Rp");
                    double jumlahTopUp = scanner.nextDouble();
                    if (pelanggan.topUp(jumlahTopUp, pinInput)) {
                        kesalahanPin = 0; // Reset jika transaksi berhasil
                    }
                }
            } else if (menu == 3) { // Keluar
                System.out.println("Terima kasih telah menggunakan sistem transaksi kami.");
                scanner.close();
                break;
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }
}
