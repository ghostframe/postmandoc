package com.pravus.postdocs;

import com.pravus.postdocs.postman.PostmanCollectionFactory;
import java.io.File;
import java.io.IOException;
import static org.assertj.core.api.Java6Assertions.contentOf;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.core.io.ClassPathResource;

public class PostmanCollectionFactoryTest {

    @Test
    public void fromSnippetsFolder_withSubfolderPerTestClassAndSubfolderPerTestCaseInCamelCase_generatesExpectedCollection() throws IOException, JSONException {
        File snippetsFolder = new ClassPathResource("folder-per-test-class/generated-snippets-camel-case/").getFile();
        String expectedJson = contentOf(new ClassPathResource("folder-per-test-class/postman_collection.json").getFile());
        String collectionJson = PostmanCollectionFactory.fromSnippetsFolder("rest-docs-sample", snippetsFolder);
        System.out.println(collectionJson);
        JSONAssert.assertEquals(expectedJson, collectionJson, JSONCompareMode.LENIENT);
    }

    @Test
    public void fromSnippetsFolder_withSubfolderPerTestClassAndSubfolderPerTestCaseInKebabCase_generatesExpectedCollection() throws IOException, JSONException {
        File snippetsFolder = new ClassPathResource("folder-per-test-class/generated-snippets-kebab-case/").getFile();
        String expectedJson = contentOf(new ClassPathResource("folder-per-test-class/postman_collection.json").getFile());
        String collectionJson = PostmanCollectionFactory.fromSnippetsFolder("rest-docs-sample", snippetsFolder);
        System.out.println(collectionJson);
        JSONAssert.assertEquals(expectedJson, collectionJson, JSONCompareMode.LENIENT);
    }

    @Test
    public void fromSnippetsFolder_withSubfolderPerTestClassAndSubfolderPerTestCaseInSnakeCase_generatesExpectedCollection() throws IOException, JSONException {
        File snippetsFolder = new ClassPathResource("folder-per-test-class/generated-snippets-snake-case/").getFile();
        String expectedJson = contentOf(new ClassPathResource("folder-per-test-class/postman_collection.json").getFile());
        String collectionJson = PostmanCollectionFactory.fromSnippetsFolder("rest-docs-sample", snippetsFolder);
        System.out.println(collectionJson);
        JSONAssert.assertEquals(expectedJson, collectionJson, JSONCompareMode.LENIENT);
    }

    @Test
    public void sad() {
    }

}
