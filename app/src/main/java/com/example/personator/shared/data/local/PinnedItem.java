package com.example.personator.shared.data.local;

import io.realm.RealmObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PinnedItem extends RealmObject {
    private String imageSrc;
    private String publicationDate;
    private String publicationBody;
    private String headline;
}
