package com.lin_chen.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 需要token验证的配置类
 **/
@Setter
@Getter
@ToString
@ConfigurationProperties(prefix = "lin-chen.token-verify")
public class TokenVerifyProperties {
    private List<String> verifyURL = new ArrayList<>();

    /**
     * 判断是否需要验证
     *
     * @param method 请求方法
     * @param url    请求路由
     */
    public boolean needVerify(String method, String url) {
        return verifyURL.stream().anyMatch(verifyStr -> {
            VerifyURL verifyURL = VerifyURL.analyzeURL(verifyStr);
            if (verifyURL.getMethod().equals(method)) {
                String template = verifyURL.getUrl();
                String replacement = "\\D*/*";
                if (template.endsWith("**")) {
                    int indexOf = template.lastIndexOf("**");
                    template = template.substring(0, indexOf - 1);
                    template += replacement;
                }
                String pattern = template.replace("**", "\\D+");
                return Pattern.matches(pattern, url);
            }
            return false;
        });
    }
}
