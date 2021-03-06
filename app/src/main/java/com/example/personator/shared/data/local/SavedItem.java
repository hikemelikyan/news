package com.example.personator.shared.data.local;

import io.realm.RealmObject;

public class SavedItem extends RealmObject {

    private String imageSrc;
    private String publicationDate;
    private String publicationBody;
    private String sectionName;
    private String headline;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getPublicationBody() {
        return publicationBody;
    }

    public void setPublicationBody(String publicationBody) {
        this.publicationBody = publicationBody;
    }
}
