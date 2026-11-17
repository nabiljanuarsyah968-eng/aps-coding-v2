package APS1;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class APS extends JFrame {
    public static void main(String[] args) {
        Data.isiDataAwal();
        SwingUtilities.invokeLater(() -> new APS().setVisible(true));
    }

    APS() {
        super("Sistem Cuti dan Tiket Pesawat");
        JTabbedPane tab = new JTabbedPane();
        tab.add("Cuti (HRD)", new TabCuti());
        tab.add("Cari Penerbangan", new TabPenerbangan());
        tab.add("Pemesanan & Manajer Travel", new TabPemesanan());
        tab.add("Member", new TabMember());
        add(tab);
        setSize(900, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}