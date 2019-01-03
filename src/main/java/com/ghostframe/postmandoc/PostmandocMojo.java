/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.ghostframe.postmandoc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghostframe.postmandoc.postman.PostmanCollectionFactory;
import com.ghostframe.postmandoc.postman.domain.PostmanCollection;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class PostmandocMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.directory}/generated-snippets/")
    private String generatedSnippetsDirectory;
    @Parameter
    private String replacementHost;
    @Parameter(defaultValue = "${project.build.directory}/${project.name}.postman_collection.json")
    private File outputFile;
    @Parameter(defaultValue = "${project.name}")
    private String collectionName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            PostmanCollection postmanCollection = PostmanCollectionFactory.fromSnippetsFolder(collectionName, new File(generatedSnippetsDirectory), replacementHost);
            String collectionJson = writeAsJson(postmanCollection);
            FileUtils.write(outputFile, collectionJson, StandardCharsets.UTF_8);
            getLog().info("Generated Postman collection: " + outputFile);
        } catch (IOException ex) {
            getLog().error(ex);
        }
    }

    private String writeAsJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    public String getGeneratedSnippetsDirectory() {
        return generatedSnippetsDirectory;
    }

    public void setGeneratedSnippetsDirectory(String generatedSnippetsDirectory) {
        this.generatedSnippetsDirectory = generatedSnippetsDirectory;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
}
