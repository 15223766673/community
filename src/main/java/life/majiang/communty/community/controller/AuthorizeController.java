package life.majiang.communty.community.controller;


import life.majiang.communty.community.dto.AccessTokenDTO;
import life.majiang.communty.community.dto.GithubUser;
import life.majiang.communty.community.mapper.UserMapper;
import life.majiang.communty.community.model.User;
import life.majiang.communty.community.util.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String client_id;
    @Value("${github.client.secret}")
    private String client_secret;
    @Value("${github.redirect.uri}")
    private String redirect_uri;
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setClient_secret(client_secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_url(redirect_uri);
        accessTokenDTO.setState(state);
        System.out.println(code+state);
    String accessToken=githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser=githubProvider.getGithubUser(accessToken);
        if(githubUser!=null){
            String accountId = String.valueOf(githubUser.getId());
            //判断该用户存储在数据库没有，如果存储更新用户信息，如果没有则存储
            User newUser = userMapper.findById(accountId);
            if(newUser==null){

                String token =UUID.randomUUID().toString();
                //System.out.println(token);
                newUser=new User();
                newUser.setToken(token);

                newUser.setName(githubUser.getName());
                newUser.setAccountId(accountId);
                newUser.setGmtCreate(System.currentTimeMillis());
                newUser.setGmtModified(newUser.getGmtCreate());
                System.out.println(newUser.getGmtCreate()+" "+newUser.getGmtModified()+" "+newUser.getAccountId()+" "+newUser.getName()+" "+newUser.getToken());
                userMapper.insert(newUser);
                response.addCookie(new Cookie("token",token));
            }
            else {
                newUser.setGmtModified(System.currentTimeMillis());
                newUser.setName(githubUser.getName());
                userMapper.updateUser(newUser);
                response.addCookie(new Cookie("token",newUser.getToken()));
            }


            //登陆成功，写cookie和seesion,设置session时自动创建cookie
//            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";

        }
        else{
            //登陆失败
            return "redirect:/";
        }

    }
}
