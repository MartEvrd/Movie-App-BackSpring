package org.epita.spring.tpmovieapptest.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.List;

@Entity
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;
    private Long tmdbId;
    private String title;
    private Long duration;
    private String resume;
    private String image_landscape;
    private String image_portrait;
    private Double score;
    private Long votesQuantity;
    private List<GenreEnum> genres;
    private LocalDate date;
    private boolean hasVideo;
    private String video;
    private String media_type;


}
