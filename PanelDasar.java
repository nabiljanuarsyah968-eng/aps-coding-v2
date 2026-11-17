package APS1;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

abstract class PanelDasar extends JPanel {
    PanelDasar() {
        super(new BorderLayout());
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

    static Pengguna cekMember(JComboBox<String> kolomId) {
        Pengguna p = Data.cariPengguna(angka(kolomId));
        if (p == null || !p.member) {
            galat("Hanya untuk member frequent traveller (isi ID member yang valid).");
            return null;
        }
        return p;
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
        for (Pemesanan p : Data.daftarPemesanan) {
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
}
