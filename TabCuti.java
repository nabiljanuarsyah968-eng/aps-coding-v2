package APS1;
import java.awt.BorderLayout;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

class TabCuti extends PanelDasar {
    TabCuti() {
        JTextField tahun = kolom(5);
        JTextField bulan = kolom(3);
        JTextField hari = kolom(3);
        DefaultTableModel model = buatModel("Nama", "Mulai", "Selesai", "Lama (hari)");
        JButton tampilkan = new JButton("Tampilkan");
        tampilkan.addActionListener(e -> {
            model.setRowCount(0);
            for (Cuti c : Data.daftarCuti) {
                if (c.cocok(angka(tahun), angka(bulan), angka(hari))) {
                    model.addRow(new Object[] {c.nama, c.mulai, c.selesai, c.lama()});
                }
            }
            if (model.getRowCount() == 0) {
                info("Tidak ada pengajuan cuti pada periode tersebut.");
            }
        });

        tampilkan.doClick();

        JComboBox<String> pegawai = new JComboBox<>(new String[] {Data.NAMA_PEGAWAI[1], Data.NAMA_PEGAWAI[2]});
        JTextArea riwayat = new JTextArea(6, 40);
        riwayat.setEditable(false);
        JButton lihatRiwayat = new JButton("Lihat riwayat & sisa cuti");
        lihatRiwayat.addActionListener(e -> {
            int id = pegawai.getSelectedIndex() + 1;
            int terpakai = 0;
            StringBuilder teks = new StringBuilder("Riwayat cuti " + Data.NAMA_PEGAWAI[id] + ":\n");
            for (Cuti c : Data.daftarCuti) {
                if (c.idPegawai == id) {
                    teks.append("- ").append(c.mulai).append(" s/d ").append(c.selesai)
                            .append(" (").append(c.lama()).append(" hari)\n");
                    if (c.mulai.getYear() == LocalDate.now().getYear()) {
                        terpakai += c.lama();
                    }
                }
            }
            teks.append("Sisa cuti tahun ini: ").append(Data.KUOTA_CUTI - terpakai).append(" hari");
            riwayat.setText(teks.toString());
        });

        JPanel bawah = new JPanel(new BorderLayout());
        bawah.add(baris(new JLabel("Pegawai"), pegawai, lihatRiwayat), BorderLayout.NORTH);
        bawah.add(new JScrollPane(riwayat), BorderLayout.CENTER);

        add(baris(new JLabel("Tahun"), tahun, new JLabel("Bulan"), bulan, new JLabel("Tanggal"), hari,
                tampilkan), BorderLayout.NORTH);
        add(tabel(model), BorderLayout.CENTER);
        add(bawah, BorderLayout.SOUTH);
    }
}
