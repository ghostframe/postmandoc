package com.ghostframe.postmandoc.postman;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghostframe.postmandoc.postman.domain.PostmanCollection;
import com.ghostframe.postmandoc.postman.domain.PostmanCollectionFolderItem;
import com.ghostframe.postmandoc.postman.domain.PostmanCollectionInfo;
import com.ghostframe.postmandoc.postman.domain.PostmanCollectionItem;
import com.ghostframe.postmandoc.postman.domain.PostmanCollectionRequestItem;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import static java.util.Collections.EMPTY_LIST;
import java.util.List;
import java.util.UUID;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.http.HttpException;

public class PostmanCollectionFactory {

    private static final String HTTP_REQUEST_SNIPPET_FILENAME = "http-request.adoc";
    private static final String COLLECTION_V2_0_0_SCHEMA = "https://schema.getpostman.com/json/collection/v2.0.0/collection.json";
    private static final String TEST_CLASS_SUFFIX_REGEX = "(rest|controller|tests|test)";

    @SneakyThrows(IOException.class)
    public static String fromSnippetsFolder(String collectionName, File generatedSnippetsFolder) {
        PostmanCollection postmanCollection = PostmanCollection.builder()
                .info(PostmanCollectionInfo.builder()
                        .name(collectionName)
                        .schema(COLLECTION_V2_0_0_SCHEMA)
                        .description("")
                        ._postman_id(UUID.randomUUID().toString())
                        .build())
                .item(scanGeneratedSnippetsFolder(generatedSnippetsFolder))
                .variables(EMPTY_LIST)
                .build();
        return new ObjectMapper().writeValueAsString(postmanCollection);
    }

    private static List<PostmanCollectionItem> scanGeneratedSnippetsFolder(File generatedSnippetsFolder) throws IOException {
        return filesIn(generatedSnippetsFolder)
                .map(PostmanCollectionFactory::folderToCollectionItem)
                .collect(toList());
    }

    private static PostmanCollectionItem folderToCollectionItem(File folder) {
        File httpRequestSnippet = FileUtils.getFile(folder, HTTP_REQUEST_SNIPPET_FILENAME);
        if (httpRequestSnippet.exists()) {
            return createRequest(folder.getName(), httpRequestSnippet);
        } else {
            PostmanCollectionFolderItem postmanCollectionItem = createFolder(folder);
            postmanCollectionItem.setItem(filesIn(folder)
                    .map(PostmanCollectionFactory::folderToCollectionItem)
                    .collect(toList()));
            return postmanCollectionItem;
        }
    }

    private static PostmanCollectionFolderItem createFolder(File folder) {
        PostmanCollectionFolderItem postmanCollectionFolder = new PostmanCollectionFolderItem();
        postmanCollectionFolder.setDescription("");
        postmanCollectionFolder.setName(testClassNameToFolderName(humanizeCase(folder.getName())));
        return postmanCollectionFolder;
    }

    private static String testClassNameToFolderName(String text) {
        return text.replaceAll(TEST_CLASS_SUFFIX_REGEX, "").trim();
    }

    @SneakyThrows({HttpException.class, IOException.class})
    private static PostmanCollectionItem createRequest(String name, File httpRequestSnippet) {
        PostmanCollectionRequestItem postmanCollectionRequest = new PostmanCollectionRequestItem();
        postmanCollectionRequest.setName(humanizeCase(name));
        postmanCollectionRequest.setResponse(EMPTY_LIST);
        String httpRequest = FileUtils.readFileToString(httpRequestSnippet, StandardCharsets.UTF_8);
        postmanCollectionRequest.setRequest(PostmanRequestFactory.fromHttpRequestSnippet(httpRequest));
        return postmanCollectionRequest;
    }

    private static String humanizeCase(String text) {
        String dashReplacedWithSpace = text.replace('-', ' ').replace('_', ' ');
        String capitalized = WordUtils.capitalize(dashReplacedWithSpace);
        String camelCase = capitalized.replace(" ", "");
        return StringUtils.capitalize(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(camelCase), " ").toLowerCase());
    }

    @SneakyThrows(IOException.class)
    private static Stream<File> filesIn(File folder) {
        return Files.list(folder.toPath())
                .map(java.nio.file.Path::toFile);
    }
}
