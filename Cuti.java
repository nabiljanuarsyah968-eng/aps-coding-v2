package APS1;
import java.time.LocalDate;

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