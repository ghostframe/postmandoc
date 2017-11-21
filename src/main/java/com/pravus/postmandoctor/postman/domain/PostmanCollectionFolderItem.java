package com.pravus.postmandoctor.postman.domain;

import java.util.List;
import lombok.Data;

@Data
public class PostmanCollectionFolderItem extends PostmanCollectionItem {

    private List<PostmanCollectionItem> item;
}
