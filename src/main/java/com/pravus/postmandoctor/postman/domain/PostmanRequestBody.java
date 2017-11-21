package com.pravus.postmandoctor.postman.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostmanRequestBody {

    private String mode;
    private String raw;
}
