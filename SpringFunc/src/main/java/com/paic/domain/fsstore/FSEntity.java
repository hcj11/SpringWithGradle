package com.paic.domain.fsstore;

import lombok.ToString;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
@ToString
public class FSEntity implements Serializable {
    private static final long serialVersionUID = 2951336268677521004L;
    private String keyId;
    private String name;
    private transient InputStream inputstream;
    private String contentType;
    private long size;
    private Date updatedate;
    private FSAccessControlList acl;

    public FSEntity() {
    }

    public String getKeyId() {
        return this.keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getInputstream() {
        return this.inputstream;
    }

    public void setInputstream(InputStream inputstream) {
        this.inputstream = inputstream;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getUpdatedate() {
        return this.updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public FSAccessControlList getAcl() {
        return this.acl;
    }

    public void setAcl(FSAccessControlList acl) {
        this.acl = acl;
    }
}