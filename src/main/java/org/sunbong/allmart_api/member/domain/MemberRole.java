package org.sunbong.allmart_api.member.domain;

public enum MemberRole {

    USER("USER"),ADMIN("ADMIN");

    String role;

    MemberRole(String role) {
        this.role = role;
    }
}
