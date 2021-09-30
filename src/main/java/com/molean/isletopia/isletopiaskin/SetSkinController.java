package com.molean.isletopia.isletopiaskin;


import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class SetSkinController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @ResponseBody
    @RequestMapping("/setskin")
    public String setSkin(String token, String type, @RequestParam("skin") MultipartFile skin) {


        if (token == null || type == null || skin == null) {
            return "输入不可为空";
        }
        if (!type.equals("default") && !type.equals("slim")) {
            return "输入有误";
        }

        try {
            if (type.equals("default")) {
                type = "classic";
            }
            byte[] bytes = skin.getInputStream().readAllBytes();
            if (bytes.length > 3000) {
                return "文件尺寸过大";
            }

            ValueOperations<String, String> ops = redisTemplate.opsForValue();

            String username = ops.get("UserToken:" + token);
            if (username== null) {
                return "用户凭证不存在";
            }

            String url = "https://api.mineskin.org/generate/upload";
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpPost post = new HttpPost(url);
                HttpEntity entity = MultipartEntityBuilder
                        .create()
                        .addTextBody("variant", type)
                        .addBinaryBody("file", bytes)
                        .build();
                post.setEntity(entity);

                try (CloseableHttpResponse response = client.execute(post)) {
                    String responseText = new String(response.getEntity().getContent().readAllBytes());
                    Gson gson = new Gson();
                    MineSkin mineSkin = gson.fromJson(responseText, MineSkin.class);
                    String value = mineSkin.getData().getTexture().getValue();
                    String signature = mineSkin.getData().getTexture().getSignature();
                    ops.set(username + ":SkinValue", value);
                    ops.set(username + ":SkinSignature", signature);
                }
            }

            return "皮肤设置成功, 重新进入游戏即可看到效果.";
        } catch (Exception exception) {
            exception.printStackTrace();
            return "皮肤解析失败, 检查文件是否正确, 并重新上传.";
        }
    }
}
