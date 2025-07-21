package models;

import java.time.LocalDate;

public class User {
    private String email;
    private String password;
    private String nama;
    private LocalDate tanggalLahir;
    private String nomorHp;
    private String kodeNegara;
    private String bio;
    private String jenisKelamin;

    public User(String email, String password, String nama, LocalDate tanggalLahir, String nomorHp, String kodeNegara, String bio, String jenisKelamin) {
        this.email = email;
        this.password = password;
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.nomorHp = nomorHp;
        this.kodeNegara = kodeNegara;
        this.bio = bio;
        this.jenisKelamin = jenisKelamin;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public LocalDate getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(LocalDate tanggalLahir) { this.tanggalLahir = tanggalLahir; }
    public String getNomorHp() { return nomorHp; }
    public void setNomorHp(String nomorHp) { this.nomorHp = nomorHp; }
    public String getKodeNegara() { return kodeNegara; }
    public void setKodeNegara(String kodeNegara) { this.kodeNegara = kodeNegara; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", nama='" + nama + '\'' +
                ", tanggalLahir=" + tanggalLahir +
                ", nomorHp='" + nomorHp + '\'' +
                ", kodeNegara='" + kodeNegara + '\'' +
                ", bio='" + bio + '\'' +
                ", jenisKelamin='" + jenisKelamin + '\'' +
                '}';
    }
}
