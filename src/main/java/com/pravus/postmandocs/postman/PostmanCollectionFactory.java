package com.pravus.postmandocs.postman;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pravus.postmandocs.postman.domain.PostmanCollection;
import com.pravus.postmandocs.postman.domain.PostmanCollectionFolderItem;
import com.pravus.postmandocs.postman.domain.PostmanCollectionInfo;
import com.pravus.postmandocs.postman.domain.PostmanCollectionItem;
import com.pravus.postmandocs.postman.domain.PostmanCollectionRequestItem;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import static java.util.Collections.EMPTY_LIST;
import java.util.List;
import static java.util.stream.Collectors.toList;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;

public class PostmanCollectionFactory {

    private static final String HTTP_REQUEST_SNIPPET_FILENAME = "http-request.adoc";
    private static final String COLLECTION_V2_SCHEMA = "https://schema.getpostman.com/json/collection/v2.0.0/collection.json";
    private static final String TEST_CLASS_SUFFIX_REGEX = "(Rest|Controller|Tests|Test)";

    @SneakyThrows(value = IOException.class)
    public static String fromSnippetsFolder(String collectionName, File generatedSnippetsFolder) {
        PostmanCollection postmanCollection = PostmanCollection.builder()
                .info(PostmanCollectionInfo.builder()
                        .name(collectionName)
                        .schema(COLLECTION_V2_SCHEMA)
                        .description("")
                        ._postman_id("024735d8-3505-b53f-059b-405abeabba24")
                        .build())
                .item(createItems(generatedSnippetsFolder.getPath()))
                .variables(EMPTY_LIST)
                .build();
        return new ObjectMapper().writeValueAsString(postmanCollection);
    }

    public static List<PostmanCollectionItem> createItems(String generatedSnippetsFolderPath) throws IOException {
        Resource[] testClassDirectories = new PathMatchingResourcePatternResolver()
                .getResources(ResourceUtils.FILE_URL_PREFIX + generatedSnippetsFolderPath + "/*");
        return Arrays.stream(testClassDirectories)
                .map(PostmanCollectionFactory::createCollectionFolder)
                .collect(toList());
    }

    @SneakyThrows(value = {IOException.class})
    public static PostmanCollectionItem createCollectionFolder(Resource testClassDirectory) {
        PostmanCollectionFolderItem postmanCollectionFolder = new PostmanCollectionFolderItem();
        postmanCollectionFolder.setDescription("");
        String testClassName = testClassDirectory.getFilename();
        String entityName = stripTestClassSuffixes(testClassName);
        postmanCollectionFolder.setName(entityName);
        Resource[] testCaseDirectories = new PathMatchingResourcePatternResolver()
                .getResources(testClassDirectory.getURL() + "/*");
        postmanCollectionFolder.setItem(
                Arrays.stream(testCaseDirectories)
                        .map(PostmanCollectionFactory::createCollectionRequest)
                        .collect(toList()));
        return postmanCollectionFolder;
    }

    private static String stripTestClassSuffixes(String testClassName) {
        return testClassName.replaceAll(TEST_CLASS_SUFFIX_REGEX, "");
    }

    @SneakyThrows(value = {HttpException.class, IOException.class})
    public static PostmanCollectionItem createCollectionRequest(Resource testCaseDirectory) {
        PostmanCollectionRequestItem postmanCollectionRequest = new PostmanCollectionRequestItem();
        postmanCollectionRequest.setName(testCaseDirectory.getFilename());
        postmanCollectionRequest.setResponse(EMPTY_LIST);
        Resource httpRequestSnippet = new PathMatchingResourcePatternResolver()
                .getResource(testCaseDirectory.getURL() + "/" + HTTP_REQUEST_SNIPPET_FILENAME);
        String httpRequest = FileUtils.readFileToString(httpRequestSnippet.getFile(), StandardCharsets.UTF_8);
        postmanCollectionRequest.setRequest(PostmanRequestFactory.fromHttpRequestSnippet(httpRequest));
        return postmanCollectionRequest;

    }
}
