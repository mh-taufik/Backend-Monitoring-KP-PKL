package com.jtk.ps.api.model;

public enum EMonth {
    Januari(1),
    Pebruari(2),
    Maret(3),
    April(4),
    Mei(5),
    Juni(6),
    Juli(7),
    Agustus(8),
    September(9),
    Oktober(10),
    Nopember(11),
    Desember(12);

    public final int id;

    EMonth(int i) {
        this.id = i;
    }

    public static EMonth valueOfId(int id) {
        for (EMonth e : values()) {
            if (e.id == id) {
                return e;
            }
        }
        return null;
    }
}
