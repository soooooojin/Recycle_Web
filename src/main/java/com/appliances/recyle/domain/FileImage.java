package com.appliances.recyle.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "file_images")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileImage {

    @Id
    private String id;

    private String fileName;
    private String contentType;
    private byte[] data;

    public FileImage(String fileName, String contentType, byte[] data) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
    }

}
