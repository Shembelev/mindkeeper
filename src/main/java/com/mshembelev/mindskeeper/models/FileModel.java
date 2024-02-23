package com.mshembelev.mindskeeper.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class FileModel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_id_seq")
    @SequenceGenerator(name = "file_id_seq", sequenceName = "file_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @Column(name = "size")
    private long size = 0;

    @Column(name = "hash", nullable = false, unique = true)
    private String hash;

    public static final int RADIX = 16;

    public void setHash() throws NoSuchAlgorithmException {
        String transformedName = new StringBuilder().append(this.name).append(this.mimeType).append(this.size)
                .append(new Date().getTime()).toString();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(transformedName.getBytes(StandardCharsets.UTF_8));
        this.hash = new BigInteger(1, messageDigest.digest()).toString(RADIX);
    }
}
