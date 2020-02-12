package life.majiang.communty.community.controller;


import life.majiang.communty.community.dto.AccessTokenDTO;
import life.majiang.communty.community.dto.GithubUser;
import life.majiang.communty.community.util.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state){
        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setClient_secret(client_secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_url(redirect_uri);
        accessTokenDTO.setState(state);
        System.out.println(code+state);
    String token=githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user=githubProvider.getGithubUser(token);
        System.out.println(user.getId());
        return "index";
    }
}