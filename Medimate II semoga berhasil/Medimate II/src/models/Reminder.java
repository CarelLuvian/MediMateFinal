package models;

import java.time.LocalDateTime;

public class Reminder {
    private int id;
    private String namaObat;
    private LocalDateTime waktu;
    private String status; // misal: "mendatang", "selesai", "dibatalkan"
    private String catatan;

    public Reminder(int id, String namaObat, LocalDateTime waktu, String status, String catatan) {
        this.id = id;
        this.namaObat = namaObat;
        this.waktu = waktu;
        this.status = status;
        this.catatan = catatan;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNamaObat() { return namaObat; }
    public void setNamaObat(String namaObat) { this.namaObat = namaObat; }
    public LocalDateTime getWaktu() { return waktu; }
    public void setWaktu(LocalDateTime waktu) { this.waktu = waktu; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", namaObat='" + namaObat + '\'' +
                ", waktu=" + waktu +
                ", status='" + status + '\'' +
                ", catatan='" + catatan + '\'' +
                '}';
    }
}
