package com.smartrwanda.tourism.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayActivity {

    @Column(name = "day_number")
    private Integer dayNumber;

    @Column(name = "activity_name", nullable = false)
    private String activityName;

    @Column(length = 1000)
    private String description;

    private String location;
}