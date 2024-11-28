package org.sunbong.allmart_api.member.domain;

public enum MemberRole {

    USER("USER"),MARTADMIN("MARTADMIN"),SYSTEMADMIN("SYSTEMADMIN");

    String role;

    MemberRole(String role) {
        this.role = role;
    }
}
