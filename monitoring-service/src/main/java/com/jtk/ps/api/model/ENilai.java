package com.jtk.ps.api.model;

public enum ENilai {
    BELUM_DINILAI(0),
    SANGAT_BAIK(1),
    BAIK(2),
    CUKUP(3),
    KURANG(4);

    public final int id;

    ENilai(int i) {
        this.id = i;
    }

    public static ENilai valueOfId(int id) {
        for (ENilai e : values()) {
            if (e.id == id) {
                return e;
            }
        }
        return null;
    }
}
