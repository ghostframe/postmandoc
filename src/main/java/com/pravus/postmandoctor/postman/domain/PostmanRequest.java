package com.pravus.postmandoctor.postman.domain;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostmanRequest {

    private String url;
    private String method;
    private List<PostmanHeader> header;
    private PostmanRequestBody body;
    private String description;

}
