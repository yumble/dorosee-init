package dorosee.initial.config.secret;

public class Secret {
    public static String JWT_SECRET_KEY = "UwKYibQQgkW7gkap9kjewxBHb9wdXoBT4vnt4P3sJWtNula";

    public static String TOKEN = "trifactaToken";
    public static String AT_JWT_KEY = "trifactaPrivateSecretKeyAccessToken";
    public static String RT_JWT_KEY = "trifactaPrivateSecretKeyAccessToken";
    public static int AT_EXPRIRATION_TIME = 1000 * 60 * 30 * 2 * 24 * 7;
    public static int RT_EXPRIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
    public static String TOKEN_PREFIX = "Bearer ";
    public static String HEADER_STRING = "Authorization";
    public static String AT_HEADER_STRING = "ACCESS_TOKEN";
    public static String RT_HEADER_STRING = "REFRESH_TOKEN";
    public static String RETURN_SINGLE_OBJECT = "OBJECT";
    public static String RETURN_LIST_OBJECTS = "LIST";
    public static String DELETE_STATE = "Y";
    public static String INSERT_STATE = "N";


}
