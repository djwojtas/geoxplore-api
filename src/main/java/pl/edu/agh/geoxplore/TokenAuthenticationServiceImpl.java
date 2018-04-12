package pl.edu.agh.geoxplore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationServiceImpl implements TokenAuthenticationService {
    private static final String AUTH_HEADER_NAME = "token";

    @Autowired
    @Qualifier("userDaoImpl")
    private UserDao userDao;

    @Autowired
    @Qualifier("userTokenDaoImpl")
    private UserTokenDao userTokenDao;

    @Autowired
    @Qualifier("tokenHandlerServiceImpl")
    private TokenHandlerService tokenHandlerService;

    @Autowired
    private UtilService utilService;

    @Override
    public Authentication getAuthenticationForLogin(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        User user = new User();
        String token = request.getHeader("token");


        UserAuthentication auth = new UserAuthentication(user, token, true);


//        if(isUsernameEmpty && isProfileIdEmpty){ //minimum one of them should be as input
//            info.setCode(100L);
//
//            if(isUsernameEmpty){
//                info.setDesc(utilService.getMessage("user.emptyusername"));
//            } else {
//                info.setDesc(utilService.getMessage("user.emptyprofileid"));
//            }
//        } else { //ikisinden birisi dolu demektir
//
//            if(isUsernameEmpty){ //o zaman facebook login
//                loginType = AuthType.FACEBOOK;
//                if(Util.isNullOrEmpty(accessToken)){
//                    info.setCode(101L);
//                    info.setDesc(utilService.getMessage("token.notfound"));
//                } else {
//                    user = userDao.readUserByProfileId(profileId, UserStatus.ACTIVE);
//
//                    if (Util.isNullObject(user)) {
//                        info.setCode(199L);
//                        info.setDesc(utilService.getMessage("user.notfound"));
//                    } else {
//                        String fbAccessToken = user.getFbUserAccessToken();
//
//                        if(Util.isNullOrEmpty(fbAccessToken)){
//                            info.setCode(102L);
//                            info.setDesc(utilService.getMessage("token.notfound"));
//                        } else if(!accessToken.equals(fbAccessToken)){
//                            try {
//                                FacebookUtil.getUser(profileId, accessToken);
//                                user.setFbUserAccessToken(accessToken);
//                                userDao.update(user);
//                                isAuthorized = true;
//                            } catch (FacebookException e) {
//                                info.setCode(104L);
//                                info.setDesc(e.getErrorMessage());
//                            }
//                        } else {
//                            try {
//                                FacebookUtil.getUser(profileId, fbAccessToken);
//                                isAuthorized = true;
//                            } catch (FacebookException e) {
//                                info.setCode(106L);
//                                info.setDesc(e.getErrorMessage());
//                            }
//                        }
//                    }
//                }
//            } else {
//                loginType = AuthType.BASIC;
//                if(Util.isNullOrEmpty(password)){
//                    info.setCode(101L);
//                    info.setDesc(utilService.getMessage("user.emptypassword"));
//                } else {
//                    user = userDao.readUserByUsername(username, UserStatus.ACTIVE);
//
//                    if (Util.isNullObject(user)) {
//                        info.setCode(199L);
//                        info.setDesc(utilService.getMessage("user.notfound"));
//                    } else if(Util.isNullOrEmpty(user.getPassword())){
//                        info.setCode(101L);
//                        info.setDesc(utilService.getMessage("user.nodefinedpassword"));
//                    } else {
//                        String md5Password = SecurityUtil.md5(password);
//                        if (!md5Password.equals(user.getPassword())) {// Password
//                            // not
//                            // equal
//                            info.setCode(103L);
//                            info.setDesc(utilService.getMessage("user.wrongpass"));
//                        } else { // user is authorized successfully
//                            isAuthorized = true;
//                        }
//                    }
//                }
//            }
//
//        }

        if(token == "KURWA"){
            //info.setCode(0L);
            //info.setDesc(loginType.getLoginType());
            auth.setUser(user);
            auth.setAuthenticated(true);
            //auth.setAuthType(loginType);
            return auth;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        auth.setAuthenticated(false);
        return auth;
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String token = request.getHeader(AUTH_HEADER_NAME);

        UserAuthentication auth = new UserAuthentication(null);
        auth.setIpAddress(IPUtil.getRemoteIp(request));

        Info info = auth.getInfo();

        if (!Util.isNullOrEmpty(token)) {
            User user = null;
            try {
                user = tokenHandlerService.parseUserFromToken(token);

                if (!Util.isNullObject(user)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    auth.setUser(user);
                    auth.setAuthenticated(true);
                    auth.setToken(token);
                    return auth;
                } else {
                    info.setCode(101L);
                    info.setDesc(utilService.getMessage("user.notfound"));
                }
            } catch (TokenException e) {
                info.setCode(102L);
                info.setDesc(e.getDescription());
                info.setObject(e);

                logger.warn("Token Exception : " + e.getMessage());
            } catch (Exception e){
                info.setCode(103L);
                info.setDesc("UNWANTED_EXCEPTION_RELOGIN_NEEDED");

                logger.warn("Exception", e);
            }

        } else {
            info.setCode(100L);
            info.setDesc(utilService.getMessage("token.notfound"));
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        auth.setAuthenticated(false);
        return auth;
    }

    @Override
    public UserToken addAuthentication(HttpServletResponse response, UserAuthentication authResult) {
        final User user = authResult.getDetails();
        String token = tokenHandlerService.calculateTokenForUser(user);

        UserToken utoken = new UserToken();
        utoken.setUser(user);
        utoken.setToken(token);
        utoken.setStatus(TokenStatus.ACTIVE.getTokenStatus());
        utoken.setIpAddress(authResult.getIpAddress());
        utoken.setCreatedDate(new Date());
        utoken.setAuthType(authResult.getAuthType().getLoginType());

        userTokenDao.deactivateAllTokensByUser(user.getId());
        userTokenDao.create(utoken);
        tokenHandlerService.insertToCache(token, user);

        response.addHeader(AUTH_HEADER_NAME, token);
        return utoken;
    }

}
