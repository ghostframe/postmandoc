package com.ghostframe.postmandoc;

import com.ghostframe.postmandoc.postman.PostmanCollectionFactory;
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
    @Parameter(defaultValue = "${project.build.directory}/${project.name}.postman_collection.json")
    private File outputFile;
    @Parameter(defaultValue = "${project.name}")
    private String collectionName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String collectionJson = PostmanCollectionFactory.fromSnippetsFolder(collectionName, new File(generatedSnippetsDirectory));
        try {
            FileUtils.write(outputFile, collectionJson, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new MojoFailureException("Couldn't write output file");
        }
        getLog().info("Generated Postman collection: " + outputFile);
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
