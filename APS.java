package APS1;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeSet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

class Cuti {
    int idPegawai;
    String nama;
    LocalDate mulai;
    LocalDate selesai;

    Cuti(int idPegawai, String nama, String mulai, String selesai) {
        this.idPegawai = idPegawai;
        this.nama = nama;
        this.mulai = LocalDate.parse(mulai);
        this.selesai = LocalDate.parse(selesai);
    }

    int lama() {
        return (int) (selesai.toEpochDay() - mulai.toEpochDay()) + 1;
    }

    boolean cocok(int tahun, int bulan, int hari) {
        for (LocalDate d = mulai; !d.isAfter(selesai); d = d.plusDays(1)) {
            boolean okTahun = tahun <= 0 || d.getYear() == tahun;
            boolean okBulan = bulan <= 0 || d.getMonthValue() == bulan;
            boolean okHari = hari <= 0 || d.getDayOfMonth() == hari;
            if (okTahun && okBulan && okHari) {
                return true;
            }
        }
        return false;
    }
}

class Penerbangan {
    int id;
    String asal;
    String tujuan;
    String tanggal;
    int jam;
    String maskapai;
    int harga;

    Penerbangan(int id, String asal, String tujuan, String tanggal, int jam, String maskapai, int harga) {
        this.id = id;
        this.asal = asal;
        this.tujuan = tujuan;
        this.tanggal = tanggal;
        this.jam = jam;
        this.maskapai = maskapai;
        this.harga = harga;
    }
}

class Pengguna {
    int id;
    String nama;
    String email;
    String telepon;
    String noKtp;
    boolean member;

    Pengguna(int id, String nama, String email, String telepon, String noKtp, boolean member) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.telepon = telepon;
        this.noKtp = noKtp;
        this.member = member;
    }

    boolean profilLengkap() {
        return !nama.isEmpty() && !email.isEmpty() && !telepon.isEmpty() && !noKtp.isEmpty();
    }
}

class Pemesanan {
    String kode;
    int idPenerbangan;
    int idPengguna;
    String status;

    Pemesanan(String kode, int idPenerbangan, int idPengguna, String status) {
        this.kode = kode;
        this.idPenerbangan = idPenerbangan;
        this.idPengguna = idPengguna;
        this.status = status;
    }
}

public class APS extends JFrame {
    static final int KUOTA_CUTI = 12;
    static final String[] NAMA_PEGAWAI = {"", "Budi", "Sari"};
    static final String[] STATUS = {"Semua", "confirmed", "booked", "waiting for the payment"};

    static ArrayList<Cuti> daftarCuti = new ArrayList<>();
    static ArrayList<Penerbangan> daftarPenerbangan = new ArrayList<>();
    static ArrayList<Pengguna> daftarPengguna = new ArrayList<>();
    static ArrayList<Pemesanan> daftarPemesanan = new ArrayList<>();
    static int urutKode = 0;

    public static void main(String[] args) {
        isiDataAwal();
        SwingUtilities.invokeLater(() -> new APS().setVisible(true));
    }

    APS() {
        super("Sistem Cuti dan Tiket Pesawat");
        JTabbedPane tab = new JTabbedPane();
        tab.add("Cuti (HRD)", buatTabCuti());
        tab.add("Cari Penerbangan", buatTabPenerbangan());
        tab.add("Pemesanan & Manajer Travel", buatTabPemesanan());
        tab.add("Member", buatTabMember());
        add(tab);
        setSize(900, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

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

    static JTextField kolom(int lebar) {
        return new JTextField(lebar);
    }

    static JComboBox<String> pilihanKota() {
        JComboBox<String> pilihan = new JComboBox<>(
                new String[] {"", "SUB - Surabaya", "CGK - Jakarta", "DPS - Denpasar"});
        pilihan.setEditable(true);
        pilihan.setSelectedIndex(0);
        return pilihan;
    }

    static String ambilKode(JComboBox<String> pilihan) {
        String teks = ((JTextField) pilihan.getEditor().getEditorComponent()).getText();
        StringBuilder hasil = new StringBuilder();
        for (String bagian : teks.split(",")) {
            String kode = bagian.split(" - ")[0].trim();
            if (!kode.isEmpty()) {
                if (hasil.length() > 0) {
                    hasil.append(",");
                }
                hasil.append(kode);
            }
        }
        return hasil.toString();
    }

    static JComboBox<String> pilihan(ArrayList<String> isi) {
        JComboBox<String> kotak = new JComboBox<>();
        kotak.addItem("");
        for (String item : isi) {
            kotak.addItem(item);
        }
        kotak.setEditable(true);
        kotak.setSelectedIndex(0);
        return kotak;
    }

    static String teks(JComboBox<String> kotak) {
        return ((JTextField) kotak.getEditor().getEditorComponent()).getText().trim();
    }

    static int angka(JComboBox<String> kotak) {
        String awal = teks(kotak).split(" - ")[0].split(":")[0].trim();
        try {
            return Integer.parseInt(awal);
        } catch (NumberFormatException e) {
            return -1;
        }
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

    static JPanel baris(Component... isi) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (Component c : isi) {
            panel.add(c);
        }
        return panel;
    }

    static DefaultTableModel buatModel(String... namaKolom) {
        return new DefaultTableModel(namaKolom, 0) {
            @Override
            public boolean isCellEditable(int baris, int kolom) {
                return false;
            }
        };
    }

    static JScrollPane tabel(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setFillsViewportHeight(true);
        return new JScrollPane(t);
    }

    static int angka(JTextField f) {
        try {
            return Integer.parseInt(f.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static void info(String pesan) {
        JOptionPane.showMessageDialog(null, pesan);
    }

    static void galat(String pesan) {
        JOptionPane.showMessageDialog(null, pesan, "Perhatian", JOptionPane.WARNING_MESSAGE);
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

    static Pengguna cekMember(JComboBox<String> kolomId) {
        Pengguna p = cariPengguna(angka(kolomId));
        if (p == null || !p.member) {
            galat("Hanya untuk member frequent traveller (isi ID member yang valid).");
            return null;
        }
        return p;
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

    static void tampilPenerbangan(DefaultTableModel model, ArrayList<Penerbangan> hasil) {
        model.setRowCount(0);
        for (Penerbangan f : hasil) {
            model.addRow(new Object[] {f.id, f.asal, f.tujuan, f.tanggal, String.format("%02d:00", f.jam),
                f.maskapai, String.format("Rp%,d", f.harga)});
        }
        if (hasil.isEmpty()) {
            info("Penerbangan tidak ditemukan.");
        }
    }

    static void tampilPemesanan(DefaultTableModel model, String status, int idPengguna) {
        model.setRowCount(0);
        for (Pemesanan p : daftarPemesanan) {
            boolean okStatus = status == null || status.equals("Semua") || p.status.equals(status);
            boolean okPengguna = idPengguna <= 0 || p.idPengguna == idPengguna;
            if (okStatus && okPengguna) {
                model.addRow(new Object[] {p.kode, p.idPenerbangan, p.idPengguna, p.status});
            }
        }
        if (model.getRowCount() == 0) {
            info("Tidak ada data pemesanan.");
        }
    }

    JPanel buatTabCuti() {
        JTextField tahun = kolom(5);
        JTextField bulan = kolom(3);
        JTextField hari = kolom(3);
        DefaultTableModel model = buatModel("Nama", "Mulai", "Selesai", "Lama (hari)");
        JButton tampilkan = new JButton("Tampilkan");
        tampilkan.addActionListener(e -> {
            model.setRowCount(0);
            for (Cuti c : daftarCuti) {
                if (c.cocok(angka(tahun), angka(bulan), angka(hari))) {
                    model.addRow(new Object[] {c.nama, c.mulai, c.selesai, c.lama()});
                }
            }
            if (model.getRowCount() == 0) {
                info("Tidak ada pengajuan cuti pada periode tersebut.");
            }
        });

        tampilkan.doClick();

        JComboBox<String> pegawai = new JComboBox<>(new String[] {NAMA_PEGAWAI[1], NAMA_PEGAWAI[2]});
        JTextArea riwayat = new JTextArea(6, 40);
        riwayat.setEditable(false);
        JButton lihatRiwayat = new JButton("Lihat riwayat & sisa cuti");
        lihatRiwayat.addActionListener(e -> {
            int id = pegawai.getSelectedIndex() + 1;
            int terpakai = 0;
            StringBuilder teks = new StringBuilder("Riwayat cuti " + NAMA_PEGAWAI[id] + ":\n");
            for (Cuti c : daftarCuti) {
                if (c.idPegawai == id) {
                    teks.append("- ").append(c.mulai).append(" s/d ").append(c.selesai)
                            .append(" (").append(c.lama()).append(" hari)\n");
                    if (c.mulai.getYear() == LocalDate.now().getYear()) {
                        terpakai += c.lama();
                    }
                }
            }
            teks.append("Sisa cuti tahun ini: ").append(KUOTA_CUTI - terpakai).append(" hari");
            riwayat.setText(teks.toString());
        });

        JPanel bawah = new JPanel(new BorderLayout());
        bawah.add(baris(new JLabel("Pegawai"), pegawai, lihatRiwayat), BorderLayout.NORTH);
        bawah.add(new JScrollPane(riwayat), BorderLayout.CENTER);

        JPanel isi = new JPanel(new BorderLayout());
        isi.add(baris(new JLabel("Tahun"), tahun, new JLabel("Bulan"), bulan, new JLabel("Tanggal"), hari,
                tampilkan), BorderLayout.NORTH);
        isi.add(tabel(model), BorderLayout.CENTER);
        isi.add(bawah, BorderLayout.SOUTH);
        return isi;
    }

    JPanel buatTabPenerbangan() {
        JComboBox<String> tujuan = pilihanKota();
        JComboBox<String> asal = pilihanKota();
        JComboBox<String> tanggal = pilihan(daftarTanggal());
        JComboBox<String> jam = pilihan(daftarJam());
        JComboBox<String> idMember = pilihan(daftarPenggunaTeks(true));
        DefaultTableModel model = buatModel("ID", "Asal", "Tujuan", "Tanggal", "Jam", "Maskapai", "Harga");

        JButton cariBtn = new JButton("Cari");
        cariBtn.addActionListener(e -> tampilPenerbangan(model,
                cari(ambilKode(tujuan), ambilKode(asal), teks(tanggal), 0)));

        JButton multi = new JButton("Multi-destinasi (member)");
        multi.addActionListener(e -> {
            if (cekMember(idMember) != null) {
                tampilPenerbangan(model,
                        cari(ambilKode(tujuan), ambilKode(asal), teks(tanggal), 0));
            }
        });

        JButton banding = new JButton("Bandingkan harga (member)");
        banding.addActionListener(e -> {
            if (cekMember(idMember) == null) {
                return;
            }
            ArrayList<Penerbangan> hasil = cari(ambilKode(tujuan), "", teks(tanggal), angka(jam));
            hasil.sort((a, b) -> Integer.compare(a.harga, b.harga));
            tampilPenerbangan(model, hasil);
        });

        JPanel atas = new JPanel(new GridLayout(0, 1));
        atas.add(baris(new JLabel("Tujuan (pisah koma, mis. CGK,DPS)"), tujuan, new JLabel("Asal"), asal,
                new JLabel("Tanggal yyyy-mm-dd"), tanggal, new JLabel("Jam"), jam));
        atas.add(baris(new JLabel("ID member"), idMember, cariBtn, multi, banding));

        JPanel isi = new JPanel(new BorderLayout());
        isi.add(atas, BorderLayout.NORTH);
        isi.add(tabel(model), BorderLayout.CENTER);
        return isi;
    }

    JPanel buatTabPemesanan() {
        JComboBox<String> idPenerbangan = pilihan(daftarPenerbanganTeks());
        JComboBox<String> idPengguna = pilihan(daftarPenggunaTeks(false));
        JTextField kode = kolom(8);
        JComboBox<String> idPengunjung = pilihan(daftarPenggunaTeks(false));
        JComboBox<String> status = new JComboBox<>(STATUS);
        DefaultTableModel model = buatModel("Kode booking", "ID penerbangan", "ID pengguna", "Status");

        JButton pesan = new JButton("Pesan tiket");
        pesan.addActionListener(e -> {
            boolean ada = false;
            for (Penerbangan f : daftarPenerbangan) {
                if (f.id == angka(idPenerbangan)) {
                    ada = true;
                }
            }
            Pengguna pengguna = cariPengguna(angka(idPengguna));
            if (!ada || pengguna == null) {
                galat("ID penerbangan atau ID pengguna tidak valid.");
                return;
            }
            Pemesanan p = buatPemesanan(angka(idPenerbangan), pengguna.id, "booked");
            info("Pemesanan berhasil. Kode: " + p.kode + "\nE-tiket dikirim ke " + pengguna.email);
            tampilPemesanan(model, "Semua", 0);
        });

        JButton batal = new JButton("Batalkan tiket");
        batal.addActionListener(e -> {
            Pemesanan p = cariPemesanan(kode.getText().trim());
            if (p == null) {
                galat("Pesanan tidak ditemukan.");
                return;
            }
            daftarPemesanan.remove(p);
            info("Pesanan " + p.kode + " dibatalkan.");
            tampilPemesanan(model, "Semua", 0);
        });

        JButton otorisasi = new JButton("Otorisasi tiket");
        otorisasi.addActionListener(e -> {
            Pemesanan p = cariPemesanan(kode.getText().trim());
            if (p == null) {
                galat("kode booking reference tidak sesuai, silakan ulangi input");
            } else {
                info("Status booking " + p.kode + ": " + p.status);
            }
        });

        JButton tampilStatus = new JButton("Tampilkan");
        tampilStatus.addActionListener(e -> tampilPemesanan(model, (String) status.getSelectedItem(), 0));

        JButton tiketPengunjung = new JButton("Tiket pengunjung");
        tiketPengunjung.addActionListener(e -> tampilPemesanan(model, "Semua", angka(idPengunjung)));

        JPanel atas = new JPanel(new GridLayout(0, 1));
        atas.add(baris(new JLabel("ID penerbangan"), idPenerbangan, new JLabel("ID pengguna/pengunjung"),
                idPengguna, pesan));
        atas.add(baris(new JLabel("Kode booking"), kode, batal, otorisasi));
        atas.add(baris(new JLabel("Filter status"), status, tampilStatus, new JLabel("ID pengunjung"),
                idPengunjung, tiketPengunjung));

        tampilPemesanan(model, "Semua", 0);
        JPanel isi = new JPanel(new BorderLayout());
        isi.add(atas, BorderLayout.NORTH);
        isi.add(tabel(model), BorderLayout.CENTER);
        return isi;
    }

    JPanel buatTabMember() {
        JComboBox<String> id = pilihan(daftarPenggunaTeks(true));
        JTextField nama = kolom(15);
        JTextField email = kolom(15);
        JTextField telepon = kolom(12);
        JTextField ktp = kolom(12);

        JButton muat = new JButton("Muat profil");
        muat.addActionListener(e -> {
            Pengguna p = cekMember(id);
            if (p != null) {
                nama.setText(p.nama);
                email.setText(p.email);
                telepon.setText(p.telepon);
                ktp.setText(p.noKtp);
            }
        });

        JButton simpan = new JButton("Simpan profil");
        simpan.addActionListener(e -> {
            Pengguna p = cekMember(id);
            if (p != null) {
                p.nama = nama.getText().trim();
                p.email = email.getText().trim();
                p.telepon = telepon.getText().trim();
                p.noKtp = ktp.getText().trim();
                info("Profil tersimpan.");
            }
        });

        JButton validasi = new JButton("Validasi tiket");
        validasi.addActionListener(e -> {
            Pengguna p = cekMember(id);
            if (p == null) {
                return;
            }
            if (p.profilLengkap()) {
                info("Tiket tervalidasi untuk " + p.nama);
            } else {
                galat("data profil tidak lengkap");
            }
        });

        JPanel form = new JPanel(new GridLayout(0, 1));
        form.add(baris(new JLabel("ID member (1 = lengkap, 2 = tidak lengkap)"), id, muat));
        form.add(baris(new JLabel("Nama"), nama));
        form.add(baris(new JLabel("Email"), email));
        form.add(baris(new JLabel("Telepon"), telepon));
        form.add(baris(new JLabel("No KTP"), ktp));
        form.add(baris(simpan, validasi));

        JPanel isi = new JPanel(new BorderLayout());
        isi.add(form, BorderLayout.NORTH);
        return isi;
    }
}