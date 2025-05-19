package org.abrohamovich.dto;

import lombok.*;
import org.abrohamovich.entity.Format;
import org.abrohamovich.entity.Status;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookInstanceDto {
    private long id;
    @NonNull
    private BookDto book;
    @NonNull
    private PublisherDto publisher;
    private int numberOfPages;
    @NonNull
    private LocalDate receiptDate;
    @NonNull
    private Status status;
    @NonNull
    private Format format;

    @Override
    public String toString() {
        return "BookInstanceDto{" +
                "id=" + id +
                ", bookDto=" + book +
                ", publisher=" + publisher +
                ", numberOfPages=" + numberOfPages +
                ", receiptDate=" + receiptDate +
                ", status=" + status +
                ", format=" + format +
                '}';
    }
}
