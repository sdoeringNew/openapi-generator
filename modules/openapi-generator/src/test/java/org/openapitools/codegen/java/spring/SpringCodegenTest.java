/*
 * Copyright 2018 OpenAPI-Generator Contributors (https://openapi-generator.tech)
 * Copyright 2018 SmartBear Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openapitools.codegen.java.spring;

import static java.util.stream.Collectors.groupingBy;
import static org.openapitools.codegen.TestUtils.assertFileContains;
import static org.openapitools.codegen.TestUtils.assertFileNotContains;
import static org.openapitools.codegen.languages.SpringCodegen.RESPONSE_WRAPPER;
import static org.openapitools.codegen.languages.features.DocumentationProviderFeatures.DOCUMENTATION_PROVIDER;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.parser.core.models.ParseOptions;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.openapitools.codegen.CliOption;
import org.openapitools.codegen.ClientOptInput;
import org.openapitools.codegen.CodegenConstants;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.SupportingFile;
import org.openapitools.codegen.TestUtils;
import org.openapitools.codegen.languages.SpringCodegen;
import org.openapitools.codegen.languages.features.CXFServerFeatures;
import org.openapitools.codegen.languages.features.DocumentationProviderFeatures;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

public class SpringCodegenTest {

    @Test
    public void clientOptsUnicity() {
        SpringCodegen codegen = new SpringCodegen();
        codegen.cliOptions()
                .stream()
                .collect(groupingBy(CliOption::getOpt))
                .forEach((k, v) -> assertEquals(v.size(), 1, k + " is described multiple times"));
    }

    @Test
    public void doAnnotateDatesOnModelParameters() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();
        String outputPath = output.getAbsolutePath().replace('\\', '/');

        OpenAPI openAPI = new OpenAPIParser()
                .readLocation("src/test/resources/3_0/issue_5436.yml", null, new ParseOptions()).getOpenAPI();

        SpringCodegen codegen = new SpringCodegen();
        codegen.setOutputDir(output.getAbsolutePath());
        codegen.additionalProperties().put(CXFServerFeatures.LOAD_TEST_DATA_FROM_FILE, "true");

        ClientOptInput input = new ClientOptInput();
        input.openAPI(openAPI);
        input.config(codegen);

        DefaultGenerator generator = new DefaultGenerator();

        generator.setGeneratorPropertyDefault(CodegenConstants.MODELS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.APIS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, "false");
        generator.opts(input).generate();

        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/ZebrasApi.java"),
            "AnimalParams");

        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/model/AnimalParams.java"),
            "@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)", "@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)");

        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/model/AnimalParams.java"),
            "import org.springframework.format.annotation.DateTimeFormat;"
        );
    }

    @Test
    public void doGenerateCookieParams() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();
        String outputPath = output.getAbsolutePath().replace('\\', '/');

        OpenAPI openAPI = new OpenAPIParser()
                .readLocation("src/test/resources/3_0/issue_5386.yaml", null, new ParseOptions()).getOpenAPI();

        SpringCodegen codegen = new SpringCodegen();
        codegen.setOutputDir(output.getAbsolutePath());
        codegen.additionalProperties().put(CXFServerFeatures.LOAD_TEST_DATA_FROM_FILE, "true");

        ClientOptInput input = new ClientOptInput();
        input.openAPI(openAPI);
        input.config(codegen);

        DefaultGenerator generator = new DefaultGenerator();

        generator.setGeneratorPropertyDefault(CodegenConstants.MODELS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.APIS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, "false");
        generator.opts(input).generate();

        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/ElephantsApi.java"), "@CookieValue");
        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/ZebrasApi.java"), "@CookieValue");
        assertFileNotContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/BirdsApi.java"), "@CookieValue");
    }

    @Test
    public void doGenerateRequestParamForSimpleParam() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();
        String outputPath = output.getAbsolutePath().replace('\\', '/');

        OpenAPI openAPI = new OpenAPIParser()
                .readLocation("src/test/resources/3_0/issue_3248.yaml", null, new ParseOptions()).getOpenAPI();

        SpringCodegen codegen = new SpringCodegen();
        codegen.setOutputDir(output.getAbsolutePath());
        codegen.additionalProperties().put(CXFServerFeatures.LOAD_TEST_DATA_FROM_FILE, "true");

        ClientOptInput input = new ClientOptInput();
        input.openAPI(openAPI);
        input.config(codegen);

        DefaultGenerator generator = new DefaultGenerator();

        generator.setGeneratorPropertyDefault(CodegenConstants.MODELS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.APIS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, "false");

        generator.opts(input).generate();

        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/MonkeysApi.java"), "@RequestParam");
        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/ElephantsApi.java"), "@RequestParam");
        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/ZebrasApi.java"), "@RequestParam");
        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/BearsApi.java"), "@RequestParam");
        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/CamelsApi.java"), "@RequestParam");
        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/PandasApi.java"), "@RequestParam");
        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/CrocodilesApi.java"), "@RequestParam");
        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/PolarBearsApi.java"), "@RequestParam");
    }

    @Test
    public void doNotGenerateRequestParamForObjectQueryParam() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();
        String outputPath = output.getAbsolutePath().replace('\\', '/');

        OpenAPI openAPI = new OpenAPIParser()
                .readLocation("src/test/resources/3_0/objectQueryParam.yaml", null, new ParseOptions()).getOpenAPI();

        SpringCodegen codegen = new SpringCodegen();
        codegen.setOutputDir(output.getAbsolutePath());
        codegen.additionalProperties().put(CXFServerFeatures.LOAD_TEST_DATA_FROM_FILE, "true");

        ClientOptInput input = new ClientOptInput();
        input.openAPI(openAPI);
        input.config(codegen);

        DefaultGenerator generator = new DefaultGenerator();

        generator.setGeneratorPropertyDefault(CodegenConstants.MODELS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.APIS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, "false");

        generator.opts(input).generate();

        assertFileNotContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/PonyApi.java"), "@RequestParam");
    }

    @Test
    public void generateFormatForDateAndDateTimeQueryParam() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();
        String outputPath = output.getAbsolutePath().replace('\\', '/');

        OpenAPI openAPI = new OpenAPIParser()
                .readLocation("src/test/resources/3_0/issue_2053.yaml", null, new ParseOptions()).getOpenAPI();

        SpringCodegen codegen = new SpringCodegen();
        codegen.setOutputDir(output.getAbsolutePath());
        codegen.additionalProperties().put(CXFServerFeatures.LOAD_TEST_DATA_FROM_FILE, "true");

        ClientOptInput input = new ClientOptInput();
        input.openAPI(openAPI);
        input.config(codegen);

        DefaultGenerator generator = new DefaultGenerator();
        generator.setGeneratorPropertyDefault(CodegenConstants.MODELS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.APIS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, "false");
        generator.opts(input).generate();

        assertFileContains(
                Paths.get(outputPath + "/src/main/java/org/openapitools/api/ElephantsApi.java"),
                "import org.springframework.format.annotation.DateTimeFormat;"
        );
        assertFileContains(
                Paths.get(outputPath + "/src/main/java/org/openapitools/api/ElephantsApi.java"),
                "@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)"
        );
        assertFileContains(
                Paths.get(outputPath + "/src/main/java/org/openapitools/api/ZebrasApi.java"),
                "import org.springframework.format.annotation.DateTimeFormat;"
        );
        assertFileContains(
                Paths.get(outputPath + "/src/main/java/org/openapitools/api/ZebrasApi.java"),
                "@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)"
        );
    }

    @Test
    public void interfaceDefaultImplDisableWithResponseWrapper() {
        final SpringCodegen codegen = new SpringCodegen();
        codegen.additionalProperties().put(RESPONSE_WRAPPER, "aWrapper");
        codegen.processOpts();

        // jdk8 tag has been removed
        Assert.assertEquals(codegen.additionalProperties().get("jdk8"), null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void reactiveRequiredSpringBoot() {
        final SpringCodegen codegen = new SpringCodegen();
        codegen.additionalProperties().put(SpringCodegen.REACTIVE, true);
        codegen.additionalProperties().put(CodegenConstants.LIBRARY, "spring-cloud");
        codegen.processOpts();
    }

    @Test
    public void shouldGenerateRequestParamForRefParams_3248_Regression() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();
        String outputPath = output.getAbsolutePath().replace('\\', '/');

        OpenAPI openAPI = new OpenAPIParser()
                .readLocation("src/test/resources/3_0/3248-regression.yaml", null, new ParseOptions()).getOpenAPI();

        SpringCodegen codegen = new SpringCodegen();
        codegen.setOutputDir(output.getAbsolutePath());
        codegen.additionalProperties().put(CXFServerFeatures.LOAD_TEST_DATA_FROM_FILE, "true");

        ClientOptInput input = new ClientOptInput();
        input.openAPI(openAPI);
        input.config(codegen);

        DefaultGenerator generator = new DefaultGenerator();

        generator.setGeneratorPropertyDefault(CodegenConstants.MODELS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.APIS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, "false");

        generator.opts(input).generate();

        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/ExampleApi.java"),
                "@RequestParam(value = \"format\"",
                "@RequestParam(value = \"query\"");
    }

    @Test
    public void shouldGenerateRequestParamForRefParams_3248_RegressionDates() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();
        String outputPath = output.getAbsolutePath().replace('\\', '/');

        OpenAPI openAPI = new OpenAPIParser()
                .readLocation("src/test/resources/3_0/3248-regression-dates.yaml", null, new ParseOptions()).getOpenAPI();

        SpringCodegen codegen = new SpringCodegen();
        codegen.setOutputDir(output.getAbsolutePath());
        codegen.additionalProperties().put(CXFServerFeatures.LOAD_TEST_DATA_FROM_FILE, "true");

        ClientOptInput input = new ClientOptInput();
        input.openAPI(openAPI);
        input.config(codegen);

        DefaultGenerator generator = new DefaultGenerator();

        generator.setGeneratorPropertyDefault(CodegenConstants.MODELS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.APIS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, "false");

        generator.opts(input).generate();

        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/ExampleApi.java"),
                "@RequestParam(value = \"start\"");
    }

    @Test
    public void springcloudWithAsyncAndJava8HasResponseWrapperCompletableFuture() {
        final SpringCodegen codegen = new SpringCodegen();
        codegen.additionalProperties().put(SpringCodegen.ASYNC, true);
        codegen.additionalProperties().put(CodegenConstants.LIBRARY, "spring-cloud");
        codegen.processOpts();

        Assert.assertEquals(codegen.additionalProperties().get("jdk8-default-interface"), false);
        Assert.assertEquals(codegen.additionalProperties().get(RESPONSE_WRAPPER), "CompletableFuture");
    }

    @Test
    public void springcloudWithJava8DisableJdk8() {
        final SpringCodegen codegen = new SpringCodegen();
        codegen.additionalProperties().put(CodegenConstants.LIBRARY, "spring-cloud");
        codegen.processOpts();

        Assert.assertEquals(codegen.additionalProperties().get("jdk8-default-interface"), false);
    }

    @Test
    public void testAdditionalPropertiesPutForConfigValues() throws Exception {
        final SpringCodegen codegen = new SpringCodegen();
        codegen.additionalProperties().put(CodegenConstants.HIDE_GENERATION_TIMESTAMP, "true");
        codegen.additionalProperties().put(CodegenConstants.MODEL_PACKAGE, "xyz.yyyyy.mmmmm.model");
        codegen.additionalProperties().put(CodegenConstants.API_PACKAGE, "xyz.yyyyy.aaaaa.api");
        codegen.additionalProperties().put(CodegenConstants.INVOKER_PACKAGE, "xyz.yyyyy.iiii.invoker");
        codegen.additionalProperties().put(SpringCodegen.BASE_PACKAGE, "xyz.yyyyy.bbbb.base");
        codegen.additionalProperties().put(SpringCodegen.CONFIG_PACKAGE, "xyz.yyyyy.cccc.config");
        codegen.additionalProperties().put(SpringCodegen.SERVER_PORT, "8088");
        codegen.processOpts();

        OpenAPI openAPI = new OpenAPI();
        openAPI.addServersItem(new Server().url("https://api.abcde.xy:8082/v2"));
        openAPI.setInfo(new Info());
        openAPI.getInfo().setTitle("Some test API");
        codegen.preprocessOpenAPI(openAPI);

        Assert.assertEquals(codegen.additionalProperties().get(CodegenConstants.HIDE_GENERATION_TIMESTAMP), Boolean.TRUE);
        Assert.assertEquals(codegen.isHideGenerationTimestamp(), true);
        Assert.assertEquals(codegen.modelPackage(), "xyz.yyyyy.mmmmm.model");
        Assert.assertEquals(codegen.additionalProperties().get(CodegenConstants.MODEL_PACKAGE), "xyz.yyyyy.mmmmm.model");
        Assert.assertEquals(codegen.apiPackage(), "xyz.yyyyy.aaaaa.api");
        Assert.assertEquals(codegen.additionalProperties().get(CodegenConstants.API_PACKAGE), "xyz.yyyyy.aaaaa.api");
        Assert.assertEquals(codegen.getInvokerPackage(), "xyz.yyyyy.iiii.invoker");
        Assert.assertEquals(codegen.additionalProperties().get(CodegenConstants.INVOKER_PACKAGE), "xyz.yyyyy.iiii.invoker");
        Assert.assertEquals(codegen.getBasePackage(), "xyz.yyyyy.bbbb.base");
        Assert.assertEquals(codegen.additionalProperties().get(SpringCodegen.BASE_PACKAGE), "xyz.yyyyy.bbbb.base");
        Assert.assertEquals(codegen.getConfigPackage(), "xyz.yyyyy.cccc.config");
        Assert.assertEquals(codegen.additionalProperties().get(SpringCodegen.CONFIG_PACKAGE), "xyz.yyyyy.cccc.config");
        Assert.assertEquals(codegen.additionalProperties().get(SpringCodegen.TITLE), "someTest");
        Assert.assertEquals(codegen.additionalProperties().get(SpringCodegen.SERVER_PORT), "8088");
    }

    @Test
    public void testDefaultValuesFixed() {
        // we had an issue where int64, float, and double values were having single character string suffixes
        // included in their defaultValues
        // This test verifies that those characters are no longer present
        final OpenAPI openAPI = TestUtils.parseFlattenSpec("src/test/resources/2_0/issue1226.yaml");
        final SpringCodegen codegen = new SpringCodegen();
        codegen.setOpenAPI(openAPI);

        String int64Val = "9223372036854775807l";
        String floatVal = "3.14159f";
        String doubleVal = "3.14159d";

        // make sure that the model properties include character suffixes
        String modelName = "NumberHolder";
        Schema nhSchema = openAPI.getComponents().getSchemas().get(modelName);
        CodegenModel cm = codegen.fromModel(modelName, nhSchema);
        CodegenProperty int64Prop = cm.vars.get(0);
        CodegenProperty floatProp = cm.vars.get(1);
        CodegenProperty doubleProp = cm.vars.get(2);
        Assert.assertEquals(int64Prop.defaultValue, int64Val);
        Assert.assertEquals(floatProp.defaultValue, floatVal);
        Assert.assertEquals(doubleProp.defaultValue, doubleVal);

        int64Val = "9223372036854775807";
        floatVal = "3.14159";
        doubleVal = "3.14159";

        // make sure that the operation parameters omit character suffixes
        String route = "/numericqueryparams";
        Operation op = openAPI.getPaths().get(route).getGet();
        CodegenOperation co = codegen.fromOperation(route, "GET", op, null);
        CodegenParameter int64Param = co.queryParams.get(0);
        CodegenParameter floatParam = co.queryParams.get(1);
        CodegenParameter doubleParam = co.queryParams.get(2);
        Assert.assertEquals(int64Param.defaultValue, int64Val);
        Assert.assertEquals(floatParam.defaultValue, floatVal);
        Assert.assertEquals(doubleParam.defaultValue, doubleVal);
    }

    @Test
    public void testDoGenerateRequestBodyRequiredAttribute_3134_Regression() throws Exception {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();
        String outputPath = output.getAbsolutePath().replace('\\', '/');

        OpenAPI openAPI = new OpenAPIParser()
                .readLocation("src/test/resources/3_0/3134-regression.yaml", null, new ParseOptions()).getOpenAPI();

        SpringCodegen codegen = new SpringCodegen();
        codegen.setOutputDir(output.getAbsolutePath());
        codegen.additionalProperties().put(CXFServerFeatures.LOAD_TEST_DATA_FROM_FILE, "true");

        ClientOptInput input = new ClientOptInput();
        input.openAPI(openAPI);
        input.config(codegen);

        DefaultGenerator generator = new DefaultGenerator();

        generator.setGeneratorPropertyDefault(CodegenConstants.MODELS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.APIS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, "false");

        generator.opts(input).generate();

        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/ExampleApi.java"),
                "@RequestBody(required = false");
    }

    @Test
    public void testInitialConfigValues() throws Exception {
        final SpringCodegen codegen = new SpringCodegen();
        codegen.processOpts();

        OpenAPI openAPI = new OpenAPI();
        openAPI.addServersItem(new Server().url("https://api.abcde.xy:8082/v2"));
        openAPI.setInfo(new Info());
        codegen.preprocessOpenAPI(openAPI);

        Assert.assertEquals(codegen.additionalProperties().get(CodegenConstants.HIDE_GENERATION_TIMESTAMP), Boolean.FALSE);
        Assert.assertEquals(codegen.isHideGenerationTimestamp(), false);
        Assert.assertEquals(codegen.modelPackage(), "org.openapitools.model");
        Assert.assertEquals(codegen.additionalProperties().get(CodegenConstants.MODEL_PACKAGE), "org.openapitools.model");
        Assert.assertEquals(codegen.apiPackage(), "org.openapitools.api");
        Assert.assertEquals(codegen.additionalProperties().get(CodegenConstants.API_PACKAGE), "org.openapitools.api");
        Assert.assertEquals(codegen.getInvokerPackage(), "org.openapitools.api");
        Assert.assertEquals(codegen.additionalProperties().get(CodegenConstants.INVOKER_PACKAGE), "org.openapitools.api");
        Assert.assertEquals(codegen.getBasePackage(), "org.openapitools");
        Assert.assertEquals(codegen.additionalProperties().get(SpringCodegen.BASE_PACKAGE), "org.openapitools");
        Assert.assertEquals(codegen.getConfigPackage(), "org.openapitools.configuration");
        Assert.assertEquals(codegen.additionalProperties().get(SpringCodegen.CONFIG_PACKAGE), "org.openapitools.configuration");
        Assert.assertEquals(codegen.additionalProperties().get(SpringCodegen.SERVER_PORT), "8082");
        Assert.assertEquals(codegen.additionalProperties().get(SpringCodegen.UNHANDLED_EXCEPTION_HANDLING), false);
    }

    @Test
    public void testMultipartBoot() throws IOException {
        final SpringCodegen codegen = new SpringCodegen();
        codegen.setLibrary("spring-boot");
        codegen.setDelegatePattern(true);
        codegen.additionalProperties().put(DOCUMENTATION_PROVIDER, "springfox");

        final Map<String, File> files = generateFiles(codegen, "src/test/resources/3_0/form-multipart-binary-array.yaml");

        // Check that the delegate handles the array
        final File multipartArrayApiDelegate = files.get("MultipartArrayApiDelegate.java");
        assertFileContains(multipartArrayApiDelegate.toPath(), "List<MultipartFile> files");

        // Check that the api handles the array
        final File multipartArrayApi = files.get("MultipartArrayApi.java");
        assertFileContains(multipartArrayApi.toPath(), "List<MultipartFile> files",
                "@ApiParam(value = \"Many files\")",
                "@RequestPart(value = \"files\", required = false)");

        // UPDATE: the following test has been ignored due to https://github.com/OpenAPITools/openapi-generator/pull/11081/
        // We will contact the contributor of the following test to see if the fix will break their use cases and
        // how we can fix it accordingly.
        //// Check that the delegate handles the single file
        // final File multipartSingleApiDelegate = files.get("MultipartSingleApiDelegate.java");
        // assertFileContains(multipartSingleApiDelegate.toPath(), "MultipartFile file");

        // Check that the api handles the single file
        final File multipartSingleApi = files.get("MultipartSingleApi.java");
        assertFileContains(multipartSingleApi.toPath(), "MultipartFile file",
                "@ApiParam(value = \"One file\")",
                "@RequestPart(value = \"file\", required = false)");

        // Check that api validates mixed multipart request
        final File multipartMixedApi = files.get("MultipartMixedApi.java");
        assertFileContains(multipartMixedApi.toPath(), "MultipartFile file",
                "@RequestPart(value = \"file\", required = true)",
                "@Valid @RequestPart(value = \"marker\", required = false)");
    }

    // Helper function, intended to reduce boilerplate
    private Map<String, File> generateFiles(SpringCodegen codegen, String filePath) throws IOException {
        final File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();
        final String outputPath = output.getAbsolutePath().replace('\\', '/');

        codegen.setOutputDir(output.getAbsolutePath());
        codegen.additionalProperties().put(CXFServerFeatures.LOAD_TEST_DATA_FROM_FILE, "true");

        final ClientOptInput input = new ClientOptInput();
        final OpenAPI openAPI = new OpenAPIParser().readLocation(filePath, null, new ParseOptions()).getOpenAPI();
        input.openAPI(openAPI);
        input.config(codegen);

        final DefaultGenerator generator = new DefaultGenerator();
        List<File> files = generator.opts(input).generate();

        return files.stream().collect(Collectors.toMap(e -> e.getName().replace(outputPath, ""), i -> i));
    }

    /*
     * UPDATE: the following test has been ignored due to https://github.com/OpenAPITools/openapi-generator/pull/11081/
     * We will contact the contributor of the following test to see if the fix will break their use cases and
     * how we can fix it accordingly.
     */
    @Test
    @Ignore
    public void testMultipartCloud() throws IOException {
        final SpringCodegen codegen = new SpringCodegen();
        codegen.setLibrary("spring-cloud");
        codegen.setDelegatePattern(true);

        final Map<String, File> files = generateFiles(codegen, "src/test/resources/3_0/form-multipart-binary-array.yaml");

        // Check that the delegate handles the array and the file
        final File multipartApiDelegate = files.get("MultipartApiDelegate.java");
        assertFileContains(multipartApiDelegate.toPath(),
                "List<MultipartFile> files",
                "MultipartFile file");

        // Check that the api handles the array and the file
        final File multipartApi = files.get("MultipartApi.java");
        assertFileContains(multipartApi.toPath(),
               "List<MultipartFile> files",
               "MultipartFile file");
    }

    @Test
    public void testSettersForConfigValues() throws Exception {
        final SpringCodegen codegen = new SpringCodegen();
        codegen.setHideGenerationTimestamp(true);
        codegen.setModelPackage("xx.yyyyyyyy.model");
        codegen.setApiPackage("xx.yyyyyyyy.api");
        codegen.setInvokerPackage("xx.yyyyyyyy.invoker");
        codegen.setBasePackage("xx.yyyyyyyy.base");
        codegen.setConfigPackage("xx.yyyyyyyy.config");
        codegen.setUnhandledException(true);
        codegen.processOpts();

        Assert.assertEquals(codegen.additionalProperties().get(CodegenConstants.HIDE_GENERATION_TIMESTAMP), Boolean.TRUE);
        Assert.assertEquals(codegen.isHideGenerationTimestamp(), true);
        Assert.assertEquals(codegen.modelPackage(), "xx.yyyyyyyy.model");
        Assert.assertEquals(codegen.additionalProperties().get(CodegenConstants.MODEL_PACKAGE), "xx.yyyyyyyy.model");
        Assert.assertEquals(codegen.apiPackage(), "xx.yyyyyyyy.api");
        Assert.assertEquals(codegen.additionalProperties().get(CodegenConstants.API_PACKAGE), "xx.yyyyyyyy.api");
        Assert.assertEquals(codegen.getInvokerPackage(), "xx.yyyyyyyy.invoker");
        Assert.assertEquals(codegen.additionalProperties().get(CodegenConstants.INVOKER_PACKAGE), "xx.yyyyyyyy.invoker");
        Assert.assertEquals(codegen.getBasePackage(), "xx.yyyyyyyy.base");
        Assert.assertEquals(codegen.additionalProperties().get(SpringCodegen.BASE_PACKAGE), "xx.yyyyyyyy.base");
        Assert.assertEquals(codegen.getConfigPackage(), "xx.yyyyyyyy.config");
        Assert.assertEquals(codegen.additionalProperties().get(SpringCodegen.CONFIG_PACKAGE), "xx.yyyyyyyy.config");
        Assert.assertEquals(codegen.isUnhandledException(), true);
        Assert.assertEquals(codegen.additionalProperties().get(SpringCodegen.UNHANDLED_EXCEPTION_HANDLING), true);
    }

    @Test
    public void useBeanValidationTruePerformBeanValidationFalseJava8TrueForFormatEmail() throws IOException {
        beanValidationForFormatEmail(true, false, true, "@javax.validation.constraints.Email", "@org.hibernate.validator.constraints.Email");
    }

    @Test
    public void useBeanValidationTruePerformBeanValidationTrueJava8FalseForFormatEmail() throws IOException {
        beanValidationForFormatEmail(true, true, false, "@javax.validation.constraints.Email", "@org.hibernate.validator.constraints.Email");
    }

    // note: java8 option/mustache tag has been removed and default to true
    private void beanValidationForFormatEmail(boolean useBeanValidation, boolean performBeanValidation, boolean java8, String contains, String notContains) throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();
        String outputPath = output.getAbsolutePath().replace('\\', '/');

        OpenAPI openAPI = new OpenAPIParser()
                .readLocation("src/test/resources/3_0/issue_4876_format_email.yaml", null, new ParseOptions()).getOpenAPI();

        SpringCodegen codegen = new SpringCodegen();
        codegen.setOutputDir(output.getAbsolutePath());
        codegen.setUseBeanValidation(useBeanValidation);
        codegen.setPerformBeanValidation(performBeanValidation);

        ClientOptInput input = new ClientOptInput();
        input.openAPI(openAPI);
        input.config(codegen);

        DefaultGenerator generator = new DefaultGenerator();

        generator.setGeneratorPropertyDefault(CodegenConstants.MODELS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.APIS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, "false");

        List<File> files = generator.opts(input).generate();

        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/model/PersonWithEmail.java"), contains);
        assertFileNotContains(Paths.get(outputPath + "/src/main/java/org/openapitools/model/PersonWithEmail.java"), notContains);
    }

    @Test
    public void useBeanValidationTruePerformBeanValidationTrueJava8TrueForFormatEmail() throws IOException {
        beanValidationForFormatEmail(true, true, true, "@javax.validation.constraints.Email", "@org.hibernate.validator.constraints.Email");
    }

    @Test
    public void reactiveMapTypeRequestMonoTest() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();
        String outputPath = output.getAbsolutePath().replace('\\', '/');

        final OpenAPI openAPI = TestUtils.parseFlattenSpec("src/test/resources/3_0/spring/issue_8045.yaml");
        final SpringCodegen codegen = new SpringCodegen();
        codegen.setOpenAPI(openAPI);
        codegen.setOutputDir(output.getAbsolutePath());

        codegen.additionalProperties().put(SpringCodegen.DELEGATE_PATTERN, "true");
        codegen.additionalProperties().put(SpringCodegen.REACTIVE, "true");

        ClientOptInput input = new ClientOptInput();
        input.openAPI(openAPI);
        input.config(codegen);

        DefaultGenerator generator = new DefaultGenerator();

        generator.setGeneratorPropertyDefault(CodegenConstants.MODELS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.APIS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, "false");

        generator.opts(input).generate();

        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/SomeApi.java"), "Mono<Map<String, DummyRequest>>");
        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/SomeApiDelegate.java"), "Mono<Map<String, DummyRequest>>");
        assertFileNotContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/SomeApi.java"), "Mono<DummyRequest>");
        assertFileNotContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/SomeApiDelegate.java"), "Mono<DummyRequest>");
    }

    @Test
    public void shouldEscapeReservedKeyWordsForRequestParameters_7506_Regression() throws Exception {
        final SpringCodegen codegen = new SpringCodegen();
        codegen.setLibrary("spring-boot");
        codegen.setDelegatePattern(true);

        final Map<String, File> files = generateFiles(codegen, "src/test/resources/3_0/issue7506.yaml");

        final File multipartArrayApiDelegate = files.get("ExampleApi.java");
        assertFileContains(multipartArrayApiDelegate.toPath(), "@RequestPart(value = \"super\", required = false) MultipartFile _super");
        assertFileContains(multipartArrayApiDelegate.toPath(), "@RequestPart(value = \"package\", required = false) MultipartFile _package");
    }

    @Test
    public void doGeneratePathVariableForSimpleParam() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();
        String outputPath = output.getAbsolutePath().replace('\\', '/');

        OpenAPI openAPI = new OpenAPIParser()
                .readLocation("src/test/resources/3_0/issue_6762.yaml", null, new ParseOptions()).getOpenAPI();

        SpringCodegen codegen = new SpringCodegen();
        codegen.setOutputDir(output.getAbsolutePath());
        codegen.additionalProperties().put(CXFServerFeatures.LOAD_TEST_DATA_FROM_FILE, "true");
        codegen.additionalProperties().put(DOCUMENTATION_PROVIDER, "springfox");

        ClientOptInput input = new ClientOptInput();
        input.openAPI(openAPI);
        input.config(codegen);

        DefaultGenerator generator = new DefaultGenerator();

        generator.setGeneratorPropertyDefault(CodegenConstants.MODELS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.APIS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, "false");

        generator.opts(input).generate();

        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/ZebrasApi.java"), "allowableValues = \"0, 1\"");
        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/ZebrasApi.java"), "@PathVariable");
        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/BearsApi.java"), "allowableValues = \"sleeping, awake\"");
        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/BearsApi.java"), "@PathVariable");
        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/CamelsApi.java"), "allowableValues = \"sleeping, awake\"");
        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/CamelsApi.java"), "@PathVariable");
        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/GirafesApi.java"), "allowableValues = \"0, 1\"");
        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/GirafesApi.java"), "@PathVariable");
    }

    @Test
    public void shouldGenerateDefaultValueForEnumRequestParameter() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();
        String outputPath = output.getAbsolutePath().replace('\\', '/');

        final OpenAPI openAPI = TestUtils.parseFlattenSpec("src/test/resources/3_0/spring/issue_10278.yaml");
        final SpringCodegen codegen = new SpringCodegen();
        codegen.setOpenAPI(openAPI);
        codegen.setOutputDir(output.getAbsolutePath());

        codegen.additionalProperties().put(SpringCodegen.DELEGATE_PATTERN, "true");
        codegen.additionalProperties().put(SpringCodegen.REACTIVE, "true");

        ClientOptInput input = new ClientOptInput();
        input.openAPI(openAPI);
        input.config(codegen);

        DefaultGenerator generator = new DefaultGenerator();

        generator.setGeneratorPropertyDefault(CodegenConstants.MODELS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.APIS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, "false");

        generator.opts(input).generate();

        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/GetApi.java"),
            "@RequestParam(value = \"testParameter1\", required = false, defaultValue = \"BAR\")",
            "@RequestParam(value = \"TestParameter2\", required = false, defaultValue = \"BAR\")");

    }

    /**define the destinationFilename*/
    private final static String DESTINATIONFILE = "SpringFoxConfiguration.java";
    /**define the templateFile*/
    private final static String TEMPLATEFILE = "openapiDocumentationConfig.mustache";

    /**
     * test whether OpenAPIDocumentationConfig.java is generated
     * fix issue #10287
     */
    @Test
    public void testConfigFileGeneration() {

        final SpringCodegen codegen = new SpringCodegen();
        codegen.additionalProperties().put(DOCUMENTATION_PROVIDER, "springfox");
        codegen.additionalProperties().put(SpringCodegen.INTERFACE_ONLY, false);
        codegen.additionalProperties().put(SpringCodegen.SPRING_CLOUD_LIBRARY, "spring-cloud");
        codegen.additionalProperties().put(SpringCodegen.REACTIVE, false);
        codegen.additionalProperties().put(SpringCodegen.API_FIRST, false);

        codegen.processOpts();

        final List<SupportingFile> supList = codegen.supportingFiles();
        String tmpFile;
        String desFile;
        boolean flag = false;
        for (final SupportingFile s : supList) {
            tmpFile = s.getTemplateFile();
            desFile = s.getDestinationFilename();

            if (TEMPLATEFILE.equals(tmpFile)) {
                flag = true;
                assertEquals(desFile, DESTINATIONFILE);
            }
        }
        if (!flag) {
            fail("OpenAPIDocumentationConfig.java not generated");
        }
    }

    @Test
    public void shouldAddNotNullOnRequiredAttributes() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();
        String outputPath = output.getAbsolutePath().replace('\\', '/');

        final OpenAPI openAPI = TestUtils.parseFlattenSpec("src/test/resources/3_0/spring/issue_5026-b.yaml");
        final SpringCodegen codegen = new SpringCodegen();
        codegen.setOpenAPI(openAPI);
        codegen.setOutputDir(output.getAbsolutePath());

        ClientOptInput input = new ClientOptInput();
        input.openAPI(openAPI);
        input.config(codegen);

        DefaultGenerator generator = new DefaultGenerator();

        generator.setGeneratorPropertyDefault(CodegenConstants.MODELS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.APIS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, "false");

        generator.opts(input).generate();

        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/model/Dummy.java"), "status");
        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/model/Dummy.java"), "@NotNull");
        Files.readAllLines(Paths.get(outputPath + "/src/main/java/org/openapitools/model/Dummy.java")).forEach(System.out::println);

    }

    @Test
    public void shouldNotAddNotNullOnReadOnlyAttributes() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();
        String outputPath = output.getAbsolutePath().replace('\\', '/');

        final OpenAPI openAPI = TestUtils.parseFlattenSpec("src/test/resources/3_0/spring/issue_5026.yaml");
        final SpringCodegen codegen = new SpringCodegen();
        codegen.setOpenAPI(openAPI);
        codegen.setOutputDir(output.getAbsolutePath());

        ClientOptInput input = new ClientOptInput();
        input.openAPI(openAPI);
        input.config(codegen);

        DefaultGenerator generator = new DefaultGenerator();

        generator.setGeneratorPropertyDefault(CodegenConstants.MODELS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.APIS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, "false");

        generator.opts(input).generate();

        assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/model/Dummy.java"), "status");
        assertFileNotContains(Paths.get(outputPath + "/src/main/java/org/openapitools/model/Dummy.java"), "@NotNull");
        Files.readAllLines(Paths.get(outputPath + "/src/main/java/org/openapitools/model/Dummy.java")).forEach(System.out::println);

    }

    @Test
    public void testTypeMappings() {
        final SpringCodegen codegen = new SpringCodegen();
        codegen.processOpts();
        Assert.assertEquals(codegen.typeMapping().get("file"), "org.springframework.core.io.Resource");
    }

    @Test
    public void testImportMappings() {
        final SpringCodegen codegen = new SpringCodegen();
        codegen.processOpts();
        Assert.assertEquals(codegen.importMapping().get("org.springframework.core.io.Resource"), "org.springframework.core.io.Resource");
        Assert.assertEquals(codegen.importMapping().get("Pageable"), "org.springframework.data.domain.Pageable");
        Assert.assertEquals(codegen.importMapping().get("DateTimeFormat"), "org.springframework.format.annotation.DateTimeFormat");
        Assert.assertEquals(codegen.importMapping().get("ApiIgnore"), "springfox.documentation.annotations.ApiIgnore");
        Assert.assertEquals(codegen.importMapping().get("ParameterObject"), "org.springdoc.api.annotations.ParameterObject");
    }

    @Test(dataProvider = "issue11464TestCases")
    public void shouldGenerateOneTagAttributeForMultipleTags_Regression11464(String documentProvider, Consumer<String> assertFunction) throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();
        String outputPath = output.getAbsolutePath().replace('\\', '/');

        OpenAPI openAPI = new OpenAPIParser()
            .readLocation("src/test/resources/bugs/issue_11464.yaml", null, new ParseOptions()).getOpenAPI();

        SpringCodegen codegen = new SpringCodegen();
        codegen.setOutputDir(output.getAbsolutePath());
        codegen.additionalProperties().put(DOCUMENTATION_PROVIDER, documentProvider);
        ClientOptInput input = new ClientOptInput();
        input.openAPI(openAPI);
        input.config(codegen);

        DefaultGenerator generator = new DefaultGenerator();

        generator.setGeneratorPropertyDefault(CodegenConstants.MODELS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.APIS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, "false");

        generator.opts(input).generate();

        assertFunction.accept(outputPath);
    }

    @DataProvider
    public Object[][] issue11464TestCases() {
        return new Object[][] {
            { DocumentationProviderFeatures.DocumentationProvider.SPRINGDOC.name(), (Consumer<String>) outputPath -> {
                assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/NoneApi.java"),
                    "@Operation( operationId = \"getNone\", summary = \"No Tag\", responses = {");
                assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/SingleApi.java"),
                    "@Operation( operationId = \"getSingleTag\", summary = \"Single Tag\", tags = { \"tag1\" }, responses = {");
                assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/MultipleApi.java"),
                    "@Operation( operationId = \"getMultipleTags\", summary = \"Multiple Tags\", tags = { \"tag1\", \"tag2\" }, responses = {");
            }},
            { DocumentationProviderFeatures.DocumentationProvider.SPRINGFOX.name(), (Consumer<String>) outputPath -> {
                assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/NoneApi.java"),
                    "@ApiOperation( value = \"No Tag\", nickname = \"getNone\", notes = \"\", response = ");
                assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/SingleApi.java"),
                    "@ApiOperation( tags = { \"tag1\" }, value = \"Single Tag\", nickname = \"getSingleTag\", notes = \"\", response = ");
                assertFileContains(Paths.get(outputPath + "/src/main/java/org/openapitools/api/MultipleApi.java"),
                    "@ApiOperation( tags = { \"tag1\", \"tag2\" }, value = \"Multiple Tags\", nickname = \"getMultipleTags\", notes = \"\", response = ");
            }},
        };
    }

    @Test
    public void apiFirstShouldNotGenerateApiOrModel() {
        final SpringCodegen codegen = new SpringCodegen();
        codegen.additionalProperties().put(SpringCodegen.API_FIRST, true);
        codegen.processOpts();
        Assert.assertTrue(codegen.modelTemplateFiles().isEmpty());
        Assert.assertTrue(codegen.apiTemplateFiles().isEmpty());
    }


}
