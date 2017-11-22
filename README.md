# postmandoc
A Maven plugin that generates a Postman collection for all requests documented with Spring Rest Docs.

## Usage

1. Clone this repository and run the `install` maven goal.
2. Add postmandoc as a build plugin and configure the `postmandoc:generate` goal:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.ghostframe</groupId>
            <artifactId>postmandoc-maven-plugin</artifactId>
            <version>1.0.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
3. Build your project. There should be a file called `${yourproject}.postman_collection.json` in the `target` directory.
4. Open up Postman and import the collection.

## Configuration
Below are the possible configuration parameters with their default values:
```xml
(...)
<configuration>
    <collectionName>${project.name}</collectionName>
    <generatedSnippetsDirectory>${project.build.directory}/generated-snippets/</generatedSnippetsDirectory>
    <outputFile>${project.build.directory}/${project.name}.postman_collection.json</outputFile>
</configuration>
```
