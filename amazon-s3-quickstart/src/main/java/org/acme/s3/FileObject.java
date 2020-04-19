package org.acme.s3;

import software.amazon.awssdk.services.s3.model.S3Object;

public class FileObject {
    private String objectKey;

    private Long size;

    public FileObject() {
    }

    public static FileObject from(S3Object s3Object) {
        FileObject file = new FileObject();
        if (s3Object != null) {
            file.setObjectKey(s3Object.key());
            file.setSize(s3Object.size());
        }
        return file;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public Long getSize() {
        return size;
    }

    public FileObject setObjectKey(String objectKey) {
        this.objectKey = objectKey;
        return this;
    }

    public FileObject setSize(Long size) {
        this.size = size;
        return this;
    }
}
