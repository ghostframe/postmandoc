package com.pravus.postdocs.postman;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pravus.postdocs.postman.domain.PostmanCollection;
import com.pravus.postdocs.postman.domain.PostmanCollectionFolderItem;
import com.pravus.postdocs.postman.domain.PostmanCollectionInfo;
import com.pravus.postdocs.postman.domain.PostmanCollectionItem;
import com.pravus.postdocs.postman.domain.PostmanCollectionRequestItem;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import static java.util.Collections.EMPTY_LIST;
import java.util.List;
import java.util.UUID;
import static java.util.stream.Collectors.toList;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.http.HttpException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;

public class PostmanCollectionFactory {

    private static final String HTTP_REQUEST_SNIPPET_FILENAME = "http-request.adoc";
    private static final String COLLECTION_V2_0_0_SCHEMA = "https://schema.getpostman.com/json/collection/v2.0.0/collection.json";
    private static final String TEST_CLASS_SUFFIX_REGEX = "(rest|controller|tests|test)";

    @SneakyThrows(value = IOException.class)
    public static String fromSnippetsFolder(String collectionName, File generatedSnippetsFolder) {
        PostmanCollection postmanCollection = PostmanCollection.builder()
                .info(PostmanCollectionInfo.builder()
                        .name(collectionName)
                        .schema(COLLECTION_V2_0_0_SCHEMA)
                        .description("")
                        ._postman_id(UUID.randomUUID().toString())
                        .build())
                .item(createFolders(generatedSnippetsFolder.getPath()))
                .variables(EMPTY_LIST)
                .build();
        return new ObjectMapper().writeValueAsString(postmanCollection);
    }

    private static List<PostmanCollectionItem> createFolders(String generatedSnippetsFolderPath) throws IOException {
        Resource[] testClassDirectories = new PathMatchingResourcePatternResolver()
                .getResources(ResourceUtils.FILE_URL_PREFIX + generatedSnippetsFolderPath + "/*");
        return Arrays.stream(testClassDirectories)
                .map(PostmanCollectionFactory::createFolder)
                .collect(toList());
    }

    @SneakyThrows(value = {IOException.class})
    private static PostmanCollectionItem createFolder(Resource testClassDirectory) {
        PostmanCollectionFolderItem postmanCollectionFolder = new PostmanCollectionFolderItem();
        postmanCollectionFolder.setDescription("");
        postmanCollectionFolder.setName(testClassNameToFolderName(humanizeCase(testClassDirectory.getFilename())));
        Resource[] testCaseDirectories = new PathMatchingResourcePatternResolver()
                .getResources(testClassDirectory.getURL() + "/*");
        postmanCollectionFolder.setItem(
                Arrays.stream(testCaseDirectories)
                        .map(PostmanCollectionFactory::createRequest)
                        .collect(toList()));
        return postmanCollectionFolder;
    }

    private static String testClassNameToFolderName(String text) {
        return text.replaceAll(TEST_CLASS_SUFFIX_REGEX, "").trim();
    }

    @SneakyThrows(value = {HttpException.class, IOException.class})
    private static PostmanCollectionItem createRequest(Resource testCaseDirectory) {
        PostmanCollectionRequestItem postmanCollectionRequest = new PostmanCollectionRequestItem();
        postmanCollectionRequest.setName(humanizeCase(testCaseDirectory.getFilename()));
        postmanCollectionRequest.setResponse(EMPTY_LIST);
        Resource httpRequestSnippet = new PathMatchingResourcePatternResolver()
                .getResource(testCaseDirectory.getURL() + "/" + HTTP_REQUEST_SNIPPET_FILENAME);
        String httpRequest = FileUtils.readFileToString(httpRequestSnippet.getFile(), StandardCharsets.UTF_8);
        postmanCollectionRequest.setRequest(PostmanRequestFactory.fromHttpRequestSnippet(httpRequest));
        return postmanCollectionRequest;
    }

    private static String humanizeCase(String text) {
        String dashReplacedWithSpace = text.replace('-', ' ').replace('_', ' ');
        String capitalized = WordUtils.capitalize(dashReplacedWithSpace);
        String camelCase = capitalized.replace(" ", "");
        return StringUtils.capitalize(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(camelCase), " ").toLowerCase());
    }
}
