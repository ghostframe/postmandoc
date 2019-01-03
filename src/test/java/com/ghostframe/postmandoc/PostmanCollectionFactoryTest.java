/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.ghostframe.postmandoc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghostframe.postmandoc.postman.PostmanCollectionFactory;
import java.io.File;
import java.io.IOException;
import lombok.SneakyThrows;
import static org.assertj.core.api.Java6Assertions.contentOf;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.core.io.ClassPathResource;

public class PostmanCollectionFactoryTest {

    private static final String COLLECTION_NAME = "rest-docs-sample";

    @Test
    @SneakyThrows
    public void fromSnippetsFolder_withSubfolderPerTestClassAndSubfolderPerTestCaseInCamelCase_generatesExpectedCollection() {
        verifyCollectionMatches("folder-per-test-class/generated-snippets-camel-case/",
                "folder-per-test-class/postman_collection.json");
    }

    @Test
    @SneakyThrows
    public void fromSnippetsFolder_withSubfolderPerTestClassAndSubfolderPerTestCaseInKebabCase_generatesExpectedCollection() {
        verifyCollectionMatches("folder-per-test-class/generated-snippets-kebab-case/",
                "folder-per-test-class/postman_collection.json");
    }

    @Test
    @SneakyThrows
    public void fromSnippetsFolder_withSubfolderPerTestClassAndSubfolderPerTestCaseInSnakeCase_generatesExpectedCollection() {
        verifyCollectionMatches("folder-per-test-class/generated-snippets-snake-case/",
                "folder-per-test-class/postman_collection.json");
    }

    @Test
    @SneakyThrows
    public void fromSnippetsFolder_withSubfolderPerTestCaseInCamelCase_generatesExpectedCollection() {
        verifyCollectionMatches("folder-per-test-case/generated-snippets-camel-case/",
                "folder-per-test-case/postman_collection.json");
    }

    @Test
    @SneakyThrows
    public void fromSnippetsFolder_withReplaceUrlParameters_generatesExpectedCollection() {
        verifyCollectionMatches("folder-per-test-case/generated-snippets-camel-case/",
                "folder-per-test-case/postman_collection_with_replaced_host.json", "{{host}}");
    }

    @Test
    @SneakyThrows
    public void fromSnippetsFolder_withLinuxNewline_generatesExpectedCollection() {
        verifyCollectionMatches("linux-newline/generated-snippets/",
                "linux-newline/postman_collection.json");
    }

    private void verifyCollectionMatches(String snippetsFolderPath, String expectedCollectionPath) throws JsonProcessingException, IOException, JSONException {
        File snippetsFolder = new ClassPathResource(snippetsFolderPath).getFile();
        String expectedJson = contentOf(new ClassPathResource(expectedCollectionPath).getFile());
        String collectionJson = writeAsJson(PostmanCollectionFactory.fromSnippetsFolder(COLLECTION_NAME, snippetsFolder));
        JSONAssert.assertEquals(expectedJson, collectionJson, JSONCompareMode.LENIENT);
    }

    private void verifyCollectionMatches(String snippetsFolderPath, String expectedCollectionPath, String replacementHost) throws JsonProcessingException, IOException, JSONException {
        File snippetsFolder = new ClassPathResource(snippetsFolderPath).getFile();
        String expectedJson = contentOf(new ClassPathResource(expectedCollectionPath).getFile());
        String collectionJson = writeAsJson(PostmanCollectionFactory.fromSnippetsFolder(COLLECTION_NAME, snippetsFolder, replacementHost));
        JSONAssert.assertEquals(expectedJson, collectionJson, JSONCompareMode.LENIENT);
    }

    private String writeAsJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
