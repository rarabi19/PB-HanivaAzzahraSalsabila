package com.example.applicationdev.PenyimpananData;

public class Pengingat {
    private String id;
    private String label;
    private String tanggal;
    private String dering;
    private boolean aktif;

    public Pengingat() {
        // Diperlukan untuk Firebase
    }

    public Pengingat(String id, String label, String waktu, String tanggal, String ringtone, boolean aktif) {
        this.id = id;
        this.label = label;
        this.tanggal = tanggal;
        this.dering = dering;
        this.aktif = aktif;
    }

    public String getId() { return id; }
    public String getLabel() { return label; }
    public String getTanggal() { return tanggal; }
    public String getDering() { return dering; }
    public boolean isAktif() { return aktif; }

    public void setId(String id) { this.id = id; }
    public void setLabel(String label) { this.label = label; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public void setDering(String ringtone) { this.dering= ringtone; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }
}
