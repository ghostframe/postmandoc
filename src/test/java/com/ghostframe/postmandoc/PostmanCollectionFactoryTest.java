package com.ghostframe.postmandoc;

import com.ghostframe.postmandoc.postman.PostmanCollectionFactory;
import java.io.File;
import java.io.IOException;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import static org.assertj.core.api.Java6Assertions.contentOf;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.core.io.ClassPathResource;

public class PostmanCollectionFactoryTest {

    @Test
    @SneakyThrows
    public void fromSnippetsFolder_withSubfolderPerTestClassAndSubfolderPerTestCaseInCamelCase_generatesExpectedCollection() {
        File snippetsFolder = new ClassPathResource("folder-per-test-class/generated-snippets-camel-case/").getFile();
        String expectedJson = contentOf(new ClassPathResource("folder-per-test-class/postman_collection.json").getFile());
        String collectionJson = PostmanCollectionFactory.fromSnippetsFolder("rest-docs-sample", snippetsFolder);
        JSONAssert.assertEquals(expectedJson, collectionJson, JSONCompareMode.LENIENT);
    }

    @Test
    @SneakyThrows
    public void fromSnippetsFolder_withSubfolderPerTestClassAndSubfolderPerTestCaseInKebabCase_generatesExpectedCollection() {
        File snippetsFolder = new ClassPathResource("folder-per-test-class/generated-snippets-kebab-case/").getFile();
        String expectedJson = contentOf(new ClassPathResource("folder-per-test-class/postman_collection.json").getFile());
        String collectionJson = PostmanCollectionFactory.fromSnippetsFolder("rest-docs-sample", snippetsFolder);
        JSONAssert.assertEquals(expectedJson, collectionJson, JSONCompareMode.LENIENT);
    }

    @Test
    @SneakyThrows
    public void fromSnippetsFolder_withSubfolderPerTestClassAndSubfolderPerTestCaseInSnakeCase_generatesExpectedCollection() {
        File snippetsFolder = new ClassPathResource("folder-per-test-class/generated-snippets-snake-case/").getFile();
        String expectedJson = contentOf(new ClassPathResource("folder-per-test-class/postman_collection.json").getFile());
        String collectionJson = PostmanCollectionFactory.fromSnippetsFolder("rest-docs-sample", snippetsFolder);
        JSONAssert.assertEquals(expectedJson, collectionJson, JSONCompareMode.LENIENT);
    }

    @Test
    @SneakyThrows
    public void fromSnippetsFolder_withSubfolderPerTestCaseInCamelCase_generatesExpectedCollection() {
        File snippetsFolder = new ClassPathResource("folder-per-test-case/generated-snippets-camel-case/").getFile();
        String expectedJson = contentOf(new ClassPathResource("folder-per-test-case/postman_collection.json").getFile());
        String collectionJson = PostmanCollectionFactory.fromSnippetsFolder("rest-docs-sample", snippetsFolder);
        JSONAssert.assertEquals(expectedJson, collectionJson, JSONCompareMode.LENIENT);
    }

    @Test
    public void asd() throws IOException {
        File folder = new ClassPathResource("folder-per-test-case/generated-snippets-camel-case/").getFile();
        FileUtils a;
    }
}
