package com.lin_chen.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.lin_chen.po.JsonResult;
import com.lin_chen.util.MapBuilder;
import com.lin_chen.util.StringUtils;
import okhttp3.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @author F_lin
 * @since 2019/3/24
 **/
@RestController
@RequestMapping("/api/common")
public class CommonController {

    private static final String imageUpload = "http://106.12.90.33:10030/api/common/uploadByFile?type=avatar";


    @PostMapping("/upload")
    public JsonResult uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty() || StringUtils.isBlank(multipartFile.getOriginalFilename())) {
            return JsonResult.error("文件为空");
        }
        String contentType = multipartFile.getContentType();
        if (!contentType.contains("")) {
            return JsonResult.error("文件类型为空");
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", multipartFile.getOriginalFilename(),
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"),
                                multipartFile.getBytes()))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + UUID.randomUUID())
                .url(imageUpload)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        Object parse = JSONObject.parse(response.body().bytes(), Feature.IgnoreNotMatch);
        Map result = (Map) parse;
        JSONObject data = (JSONObject) result.get("data");
        return JsonResult.success(MapBuilder.of("result", data.getString("imagePath")));
    }


}
