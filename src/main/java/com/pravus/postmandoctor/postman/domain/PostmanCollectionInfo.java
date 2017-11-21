package com.pravus.postmandoctor.postman.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostmanCollectionInfo {

    private String name;
    private String schema;
    private String _postman_id;
    private String description;
}
