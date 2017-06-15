package model;

/**
 * Created by ss on 3.3.2017.
 */

public class Users {
    // Labels table name
    public static final String TABLE_USERS = "users";

    // Labels Table Columns names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_TOKEN = "token";

    private Integer id;
    private String userName;
    private String userSurname;
    private String userEmail;
    private String userPassword;
    private String userToken;
    //private ArrayList<String> userTokens;

    public Users() {}

    public Users(String userName, String userSurname, String userEmail, String userPassword) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userSurname = userSurname;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }
    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() { return userPassword; }
    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }

    public String getUserToken() { return userToken; }
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }


    /*public void setUserTokens(ArrayList<String> userTokens) {
        this.userTokens = userTokens;
    }
    public ArrayList<String> getUserTokens() { return userTokens; }*/
}
