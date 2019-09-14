package com.example.personator.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {

    private boolean isHosted;
    private String id;
    private String type;
    private String sectionId;
    private String sectionName;
    private String webPublicationDate;
    private String webTitle;
    private String webUrl;
    private String apiUrl;
    private Fields fields;
    private String pillarId;
    private String pillarName;

}