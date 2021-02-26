package org.acme.s3;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.services.s3.model.S3Object;


@NoArgsConstructor
public class FileObject {

    @Getter @Setter
    private String objectKey;

    @Getter @Setter
    private Long size;

    public static FileObject from(S3Object s3Object) {
        FileObject file = new FileObject();
        if (s3Object != null) {
            file.setObjectKey(s3Object.key());
            file.setSize(s3Object.size());
        }
        return file;
    }
}
