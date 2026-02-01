package com.orakuma.rogator.file_upload;

import com.orakuma.rogator.application.ApplicationEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "files_locations", schema = "rogator")
@Accessors(chain = true)
public class FileUploadEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "f_upload_gen")
    @SequenceGenerator(name ="f_upload_gen", sequenceName = "f_upload_seq", allocationSize = 1)
    @ToString.Exclude
    private Long              id;
    private String            filename;
    private String            fileType;
    private Long              fileSize;
    @Column(nullable = false)
    private String            filePath;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private ApplicationEntity application;
    private LocalDate         created;
    private String            storedFilename;
}
