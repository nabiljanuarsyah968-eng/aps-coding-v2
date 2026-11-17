package APS1;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

class TabPenerbangan extends PanelDasar {
    TabPenerbangan() {
        JComboBox<String> tujuan = pilihanKota();
        JComboBox<String> asal = pilihanKota();
        JComboBox<String> tanggal = pilihan(Data.daftarTanggal());
        JComboBox<String> jam = pilihan(Data.daftarJam());
        JComboBox<String> idMember = pilihan(Data.daftarPenggunaTeks(true));
        DefaultTableModel model = buatModel("ID", "Asal", "Tujuan", "Tanggal", "Jam", "Maskapai", "Harga");

        JButton cariBtn = new JButton("Cari");
        cariBtn.addActionListener(e -> tampilPenerbangan(model,
                Data.cari(ambilKode(tujuan), ambilKode(asal), teks(tanggal), 0)));

        JButton multi = new JButton("Multi-destinasi (member)");
        multi.addActionListener(e -> {
            if (cekMember(idMember) != null) {
                tampilPenerbangan(model,
                        Data.cari(ambilKode(tujuan), ambilKode(asal), teks(tanggal), 0));
            }
        });

        JButton banding = new JButton("Bandingkan harga (member)");
        banding.addActionListener(e -> {
            if (cekMember(idMember) == null) {
                return;
            }
            ArrayList<Penerbangan> hasil = Data.cari(ambilKode(tujuan), "", teks(tanggal), angka(jam));
            hasil.sort((a, b) -> Integer.compare(a.harga, b.harga));
            tampilPenerbangan(model, hasil);
        });

        JPanel atas = new JPanel(new GridLayout(0, 1));
        atas.add(baris(new JLabel("Tujuan (pisah koma, mis. CGK,DPS)"), tujuan, new JLabel("Asal"), asal,
                new JLabel("Tanggal yyyy-mm-dd"), tanggal, new JLabel("Jam"), jam));
        atas.add(baris(new JLabel("ID member"), idMember, cariBtn, multi, banding));

        add(atas, BorderLayout.NORTH);
        add(tabel(model), BorderLayout.CENTER);
    }
}