package life.majiang.communty.community.util;


import com.alibaba.fastjson.JSON;
import life.majiang.communty.community.dto.AccessTokenDTO;
import life.majiang.communty.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType
                = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        //String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
            System.out.println(body.toString());
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String string=response.body().string();
                System.out.println(string);
                String token=string.split("&")[0].split("=")[1];
                return token;
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return null;
        //}
    }
    public GithubUser getGithubUser(String access_token){
        OkHttpClient client = new OkHttpClient();

        //String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url("https://api.github.com/user?access_token="+access_token)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String string= response.body().string();
                GithubUser githubUser= JSON.parseObject(string,GithubUser.class);
                return githubUser;
            }
            catch (IOException e){
                e.printStackTrace();
            }
        //}
        return null;

    }
}
