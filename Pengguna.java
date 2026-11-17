package APS1;
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
