package com.example.todoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class NoteDto implements Serializable {
    private long id;
    private String name;
    private String description;
    private Boolean done;
    private String createdAt;

    public NoteDto(long id, String name, String description, Boolean done, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.done = done;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        ZonedDateTime zonedDateTime = createdAt.atZone(ZoneId.of("UTC"));
        this.createdAt = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC+7")).format(formatter);
    }
}
