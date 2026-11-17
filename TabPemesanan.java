package APS1;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

class TabPemesanan extends PanelDasar {
    TabPemesanan() {
        JComboBox<String> idPenerbangan = pilihan(Data.daftarPenerbanganTeks());
        JComboBox<String> idPengguna = pilihan(Data.daftarPenggunaTeks(false));
        JTextField kode = kolom(8);
        JComboBox<String> idPengunjung = pilihan(Data.daftarPenggunaTeks(false));
        JComboBox<String> status = new JComboBox<>(Data.STATUS);
        DefaultTableModel model = buatModel("Kode booking", "ID penerbangan", "ID pengguna", "Status");

        JButton pesan = new JButton("Pesan tiket");
        pesan.addActionListener(e -> {
            boolean ada = false;
            for (Penerbangan f : Data.daftarPenerbangan) {
                if (f.id == angka(idPenerbangan)) {
                    ada = true;
                }
            }
            Pengguna pengguna = Data.cariPengguna(angka(idPengguna));
            if (!ada || pengguna == null) {
                galat("ID penerbangan atau ID pengguna tidak valid.");
                return;
            }
            Pemesanan p = Data.buatPemesanan(angka(idPenerbangan), pengguna.id, "booked");
            info("Pemesanan berhasil. Kode: " + p.kode + "\nE-tiket dikirim ke " + pengguna.email);
            tampilPemesanan(model, "Semua", 0);
        });

        JButton batal = new JButton("Batalkan tiket");
        batal.addActionListener(e -> {
            Pemesanan p = Data.cariPemesanan(kode.getText().trim());
            if (p == null) {
                galat("Pesanan tidak ditemukan.");
                return;
            }
            Data.daftarPemesanan.remove(p);
            info("Pesanan " + p.kode + " dibatalkan.");
            tampilPemesanan(model, "Semua", 0);
        });

        JButton otorisasi = new JButton("Otorisasi tiket");
        otorisasi.addActionListener(e -> {
            Pemesanan p = Data.cariPemesanan(kode.getText().trim());
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
        add(atas, BorderLayout.NORTH);
        add(tabel(model), BorderLayout.CENTER);
    }
}