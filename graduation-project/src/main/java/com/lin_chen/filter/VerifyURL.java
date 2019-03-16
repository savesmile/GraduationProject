package com.lin_chen.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 验证url
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyURL {
    private String method;
    private String url;

    public static VerifyURL analyzeURL(String urlStr) {
        String[] split = urlStr.split(" ");
        VerifyURL verifyURL = new VerifyURL();
        verifyURL.setMethod(split[0]);
        verifyURL.setUrl(split[1]);
        return verifyURL;
    }



}
