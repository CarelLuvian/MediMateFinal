package models;

import java.time.LocalDate;

public class RincianObat {
    private String namaObat;
    private String kategori;
    private String dosis;
    private String frekuensi;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;
    private String waktuMinum;
    private String reminder;
    private String catatan;
    private String email;

    public RincianObat() {}

    public RincianObat(String namaObat, String kategori, String dosis, String frekuensi, LocalDate tanggalMulai, LocalDate tanggalSelesai, String waktuMinum, String reminder, String catatan) {
        this.namaObat = namaObat;
        this.kategori = kategori;
        this.dosis = dosis;
        this.frekuensi = frekuensi;
        this.tanggalMulai = tanggalMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.waktuMinum = waktuMinum;
        this.reminder = reminder;
        this.catatan = catatan;
    }

    public String getNamaObat() { return namaObat; }
    public void setNamaObat(String namaObat) { this.namaObat = namaObat; }
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }
    public String getFrekuensi() { return frekuensi; }
    public void setFrekuensi(String frekuensi) { this.frekuensi = frekuensi; }
    public LocalDate getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(LocalDate tanggalMulai) { this.tanggalMulai = tanggalMulai; }
    public LocalDate getTanggalSelesai() { return tanggalSelesai; }
    public void setTanggalSelesai(LocalDate tanggalSelesai) { this.tanggalSelesai = tanggalSelesai; }
    public String getWaktuMinum() { return waktuMinum; }
    public void setWaktuMinum(String waktuMinum) { this.waktuMinum = waktuMinum; }
    public String getReminder() { return reminder; }
    public void setReminder(String reminder) { this.reminder = reminder; }
    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
} 