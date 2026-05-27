package models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Course {
    private String title;
    private LocalDate date;
    private int price;
}
