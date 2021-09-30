package com.paic.domain.fsstore;

public enum FSAccessControlList {
    Default("default"),
    Private("private"),
    PublicRead("public-read"),
    PublicReadWrite("public-read-write");

    private String cannedAclString;

    private FSAccessControlList(String cannedAclString) {
        this.cannedAclString = cannedAclString;
    }

    public String toString() {
        return this.cannedAclString;
    }
}