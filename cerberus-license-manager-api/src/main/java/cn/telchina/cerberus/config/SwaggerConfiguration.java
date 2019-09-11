package cn.telchina.cerberus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Value("${custom.baseurl}")
    private String baseUrl;
    @Value("${custom.description}")
    private String description;
    @Value("${custom.version}")
    private String version;
    @Value("${custom.title}")
    private String title;
    @Value("${custom.host}")
    private String host;

    @Bean
    public Docket userApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName(null)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex(baseUrl + "/.*"))
                .build()
                .apiInfo(apiInfo());
        if (host != null) {
            docket.host(host);
        }
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .build();
    }
}