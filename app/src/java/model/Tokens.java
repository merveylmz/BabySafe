package model;

/**
 * Created by ss on 6.3.2017.
 */

public class Tokens {

    // Labels table name
    public static final String TABLE_TOKENS = "tokens";

    // Labels Table Columns names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TOKEN = "token";

    private Integer id;
    private String token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
