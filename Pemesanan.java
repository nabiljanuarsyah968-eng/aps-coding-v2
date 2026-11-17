package APS1;
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
