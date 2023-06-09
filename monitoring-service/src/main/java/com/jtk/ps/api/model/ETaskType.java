package com.jtk.ps.api.model;

public enum ETaskType {
    Exploration(1),
    Analysis(2),
    Design(3),
    Implementation(4),
    Testing(5);

    public final int id;

    ETaskType(int i) {
        this.id = i;
    }

    public static ETaskType valueOfId(int id) {
        for (ETaskType e : values()) {
            if (e.id == id) {
                return e;
            }
        }
        return null;
    }
}