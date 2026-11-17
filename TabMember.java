package APS1;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class TabMember extends PanelDasar {
    TabMember() {
        JComboBox<String> id = pilihan(Data.daftarPenggunaTeks(true));
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

        add(form, BorderLayout.NORTH);
    }
}
