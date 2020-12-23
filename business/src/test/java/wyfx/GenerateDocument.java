//package doc;
//
//import com.wyfx.business.BusinessApplication;
//import io.github.swagger2markup.GroupBy;
//import io.github.swagger2markup.Language;
//import io.github.swagger2markup.Swagger2MarkupConfig;
//import io.github.swagger2markup.Swagger2MarkupConverter;
//import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
//import io.github.swagger2markup.markup.builder.MarkupLanguage;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.util.ResourceUtils;
//
//import java.net.URL;
//import java.nio.file.Paths;
//
///**
// * @author johnson liu
// * @description 生成文档文件
// * @date 2020/4/18 11:28
// */
//@SpringBootTest(classes = BusinessApplication.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)//这里的BusinessApplication是springboot的启动类名
//public class GenerateDocument {
//    private static final String url="http://127.0.0.1:8083/docs/v2/api-docs";
//    private static final String projectPath=System.getProperty("user.dir");
//    private static final String newAsciiDocs=projectPath+"/business/docs/asciidoc";
//    private static final String markdownDocs=projectPath+"/business/docs/markdown";
//    @Test
//    public void testPath() throws Exception{
//        String path = ResourceUtils.getURL("classpath:").getPath();
//        System.out.println(path);
//        System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
//        System.out.println(System.getProperty("user.dir"));
//    }
//
//    /**
//     * 生成AsciiDocs格式文档
//     * @throws Exception
//     */
//    @Test
//    public void generateAsciiDocs() throws Exception {
//        //    输出Ascii格式
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL(url))
//                .withConfig(config)
//                .build()
//                /*.toFolder(Paths.get("./docs/asciidoc/generated"));*/
//                .toFolder(Paths.get(newAsciiDocs));
//    }
//
//    /**
//     * 生成Markdown格式文档
//     * @throws Exception
//     */
//    @Test
//    public void generateMarkdownDocs() throws Exception {
//        //    输出Markdown格式
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.MARKDOWN)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL(url))
//                .withConfig(config)
//                .build()
//                /*.toFolder(Paths.get("./docs/markdown/generated"));*/
//                .toFolder(Paths.get(markdownDocs));
//    }
//    /**
//     * 生成Confluence格式文档
//     * @throws Exception
//     */
//    @Test
//    public void generateConfluenceDocs() throws Exception {
//        //    输出Confluence使用的格式
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.CONFLUENCE_MARKUP)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL(url))
//                .withConfig(config)
//                .build()
//                /*.toFolder(Paths.get("./docs/confluence/generated"));*/
//                .toFolder(Paths.get("./docs/confluence/generated"));
//    }
//
//    /**
//     * 生成AsciiDocs格式文档,并汇总成一个文件
//     * @throws Exception
//     */
//    @Test
//    public void generateAsciiDocsToFile() throws Exception {
//        //    输出Ascii到单文件
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL(url))
//                .withConfig(config)
//                .build()
//                .toFile(Paths.get(newAsciiDocs+"/all"));
//    }
//
//    /**
//     * 生成Markdown格式文档,并汇总成一个文件
//     * @throws Exception
//     */
//    @Test
//    public void generateMarkdownDocsToFile() throws Exception {
//        //    输出Markdown到单文件
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.MARKDOWN)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL(url))
//                .withConfig(config)
//                .build()
//                /*.toFile(Paths.get("./docs/markdown/generated/all"));*/
//                .toFile(Paths.get(markdownDocs+"/all"));
//    }
//}
