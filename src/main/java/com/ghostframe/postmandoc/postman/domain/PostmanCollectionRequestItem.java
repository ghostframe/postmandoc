/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.ghostframe.postmandoc.postman.domain;

import java.util.List;
import lombok.Data;

@Data
public class PostmanCollectionRequestItem extends PostmanCollectionItem {

    private PostmanRequest request;
    private List response;
}
