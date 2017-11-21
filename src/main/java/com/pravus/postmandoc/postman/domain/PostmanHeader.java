package com.pravus.postmandoc.postman.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostmanHeader {

    private String key;
    private String value;
    private String description;

}
