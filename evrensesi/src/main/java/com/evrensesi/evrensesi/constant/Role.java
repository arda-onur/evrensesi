package com.evrensesi.evrensesi.constant;

public enum Role {
    USER;

    public String asAuthority() {
        return "ROLE_" + this.name();
    }
}
