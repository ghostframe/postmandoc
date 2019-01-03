/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.ghostframe.postmandoc.postman.domain;

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
