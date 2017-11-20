package com.pravus.postdocs;

import com.pravus.postdocs.postman.PostmanCollectionFactory;
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
public class PostdocsMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.directory}/generated-snippets/")
    private String generatedSnippetsDirectory;
    @Parameter(defaultValue = "${project.build.directory}/${project.artifactId}.postman_collection.json")
    private String outputPath;
    @Parameter(defaultValue = "${project.artifactId}")
    private String collectionName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            File outputFile = new File(outputPath);
            String collectionJson = PostmanCollectionFactory.fromSnippetsFolder(collectionName, new File(generatedSnippetsDirectory));
            FileUtils.write(outputFile, collectionJson, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new MojoFailureException("Couldn't write output file");
        }
    }

    public String getGeneratedSnippetsDirectory() {
        return generatedSnippetsDirectory;
    }

    public void setGeneratedSnippetsDirectory(String generatedSnippetsDirectory) {
        this.generatedSnippetsDirectory = generatedSnippetsDirectory;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }
}
