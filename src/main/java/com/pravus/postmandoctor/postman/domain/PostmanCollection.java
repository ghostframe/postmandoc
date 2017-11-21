package com.pravus.postmandoctor.postman.domain;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostmanCollection {

    private List<String> variables;
    private List<PostmanCollectionItem> item;
    private PostmanCollectionInfo info;

}
