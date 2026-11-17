package APS1;
import java.util.ArrayList;
import java.util.TreeSet;

class Data {
    static final int KUOTA_CUTI = 12;
    static final String[] NAMA_PEGAWAI = {"", "Budi", "Sari"};
    static final String[] STATUS = {"Semua", "confirmed", "booked", "waiting for the payment"};

    static ArrayList<Cuti> daftarCuti = new ArrayList<>();
    static ArrayList<Penerbangan> daftarPenerbangan = new ArrayList<>();
    static ArrayList<Pengguna> daftarPengguna = new ArrayList<>();
    static ArrayList<Pemesanan> daftarPemesanan = new ArrayList<>();
    static int urutKode = 0;

    static void isiDataAwal() {
        daftarCuti.add(new Cuti(1, "Budi", "2026-12-20", "2026-12-24"));
        daftarCuti.add(new Cuti(2, "Sari", "2026-12-22", "2026-12-23"));
        daftarCuti.add(new Cuti(1, "Budi", "2026-03-02", "2026-03-04"));
        daftarPenerbangan.add(new Penerbangan(1, "SUB", "CGK", "2026-12-01", 8, "Garuda", 1200000));
        daftarPenerbangan.add(new Penerbangan(2, "SUB", "CGK", "2026-12-01", 14, "Citilink", 900000));
        daftarPenerbangan.add(new Penerbangan(3, "SUB", "DPS", "2026-12-02", 10, "Lion Air", 750000));
        daftarPenerbangan.add(new Penerbangan(4, "CGK", "DPS", "2026-12-03", 9, "Garuda", 1500000));
        daftarPengguna.add(new Pengguna(1, "Andi", "andi@mail.com", "0812", "357", true));
        daftarPengguna.add(new Pengguna(2, "Citra", "citra@mail.com", "", "", true));
        daftarPengguna.add(new Pengguna(3, "Dedi", "dedi@mail.com", "0813", "358", false));
        buatPemesanan(1, 1, "confirmed");
        buatPemesanan(2, 3, "waiting for the payment");
    }

    static Pengguna cariPengguna(int id) {
        for (Pengguna p : daftarPengguna) {
            if (p.id == id) {
                return p;
            }
        }
        return null;
    }

    static Pemesanan cariPemesanan(String kode) {
        for (Pemesanan p : daftarPemesanan) {
            if (p.kode.equalsIgnoreCase(kode)) {
                return p;
            }
        }
        return null;
    }

    static Pemesanan buatPemesanan(int idPenerbangan, int idPengguna, String status) {
        urutKode++;
        Pemesanan p = new Pemesanan("BK" + String.format("%04d", urutKode), idPenerbangan, idPengguna, status);
        daftarPemesanan.add(p);
        return p;
    }

    static ArrayList<Penerbangan> cari(String tujuan, String asal, String tanggal, int jam) {
        ArrayList<Penerbangan> hasil = new ArrayList<>();
        String[] daftarTujuan = tujuan.isEmpty() ? new String[0] : tujuan.split(",");
        for (Penerbangan f : daftarPenerbangan) {
            boolean okTujuan = daftarTujuan.length == 0;
            for (String t : daftarTujuan) {
                if (f.tujuan.equalsIgnoreCase(t.trim())) {
                    okTujuan = true;
                }
            }
            boolean okAsal = asal.isEmpty() || f.asal.equalsIgnoreCase(asal);
            boolean okTanggal = tanggal.isEmpty() || f.tanggal.equals(tanggal);
            boolean okJam = jam <= 0 || f.jam == jam;
            if (okTujuan && okAsal && okTanggal && okJam) {
                hasil.add(f);
            }
        }
        return hasil;
    }

    static ArrayList<String> daftarTanggal() {
        TreeSet<String> unik = new TreeSet<>();
        for (Penerbangan f : daftarPenerbangan) {
            unik.add(f.tanggal);
        }
        return new ArrayList<>(unik);
    }

    static ArrayList<String> daftarJam() {
        TreeSet<Integer> unik = new TreeSet<>();
        for (Penerbangan f : daftarPenerbangan) {
            unik.add(f.jam);
        }
        ArrayList<String> hasil = new ArrayList<>();
        for (int jam : unik) {
            hasil.add(String.format("%02d:00", jam));
        }
        return hasil;
    }

    static ArrayList<String> daftarPenerbanganTeks() {
        ArrayList<String> hasil = new ArrayList<>();
        for (Penerbangan f : daftarPenerbangan) {
            hasil.add(f.id + " - " + f.asal + "-" + f.tujuan + " " + f.tanggal + " "
                    + String.format("%02d:00", f.jam) + " " + f.maskapai);
        }
        return hasil;
    }

    static ArrayList<String> daftarPenggunaTeks(boolean hanyaMember) {
        ArrayList<String> hasil = new ArrayList<>();
        for (Pengguna u : daftarPengguna) {
            if (!hanyaMember || u.member) {
                hasil.add(u.id + " - " + u.nama);
            }
        }
        return hasil;
    }
}
