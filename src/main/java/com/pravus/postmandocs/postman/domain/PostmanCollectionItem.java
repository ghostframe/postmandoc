package com.pravus.postmandocs.postman.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class PostmanCollectionItem {

    private String name;
    private List<PostmanCollectionItem> item;
    private PostmanRequest request;
    private List response;
    private String description;

}
