/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.ghostframe.postmandoc.postman.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostmanRequestBody {

    private String mode;
    private String raw;
}
