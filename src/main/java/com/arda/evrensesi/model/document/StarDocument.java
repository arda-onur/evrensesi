package com.arda.evrensesi.model.document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName="stars")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StarDocument {

    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String message;
    @Field(type = FieldType.Integer)
    private int x;
    @Field(type = FieldType.Integer)
    private int y;
}
