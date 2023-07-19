package com.jtk.ps.api.model;

public enum EGrade {
    BELUM_DINILAI(0),
    SANGAT_BAIK(1),
    BAIK(2),
    CUKUP(3),
    KURANG(4);

    public final int id;

    EGrade(int i) {
        this.id = i;
    }

    public static EGrade valueOfId(int id) {
        for (EGrade e : values()) {
            if (e.id == id) {
                return e;
            }
        }
        return null;
    }
}
