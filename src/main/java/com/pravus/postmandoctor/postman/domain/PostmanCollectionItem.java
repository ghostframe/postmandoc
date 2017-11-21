package com.pravus.postmandoctor.postman.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class PostmanCollectionItem {

    private String name;
    private String description;

}
