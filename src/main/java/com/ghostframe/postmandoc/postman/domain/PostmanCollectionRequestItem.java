package com.ghostframe.postmandoc.postman.domain;

import java.util.List;
import lombok.Data;

@Data
public class PostmanCollectionRequestItem extends PostmanCollectionItem {

    private PostmanRequest request;
    private List response;
}
