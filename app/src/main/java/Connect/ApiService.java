
package Connect;

import java.util.List;

import RetrofitModel.Message;
import RetrofitModel.Token;
import RetrofitModel.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Menginar on 14.3.2017.
 */

public interface ApiService {

    // User
    @FormUrlEncoded
    @POST("/ArduinoCloud/insertUser.php")
    Call<Message> getUserInsert(
            @Field("adsoyad") String adsoyad,
            @Field("parola") String parola,
            @Field("token") String token,
            @Field("email") String email);

    @FormUrlEncoded
    @POST("/ArduinoCloud/loginUser.php")
    Call<User> getUserLogin(
            @Field("email") String email,
            @Field("parola") String parola);

    @FormUrlEncoded
    @POST("/ArduinoCloud/updateUser.php")
    Call<User> getUserUpdate(
            @Field("userid") String userid,
            @Field("adsoyad") String AdSoyad,
            @Field("parola") String parola,
            @Field("email") String email);


    // Token
    @FormUrlEncoded
    @POST("/ArduinoCloud/tokenAll.php")
    Call<List<Token>> getTokenAlls(
            @Field("userid") String userid);


    @FormUrlEncoded
    @POST("/ArduinoCloud/deleteToken.php")
    Call<List<Token>> getTokenDelete(
            @Field("userid") String userid,
            @Field("tokenid") String tokenid);

    @FormUrlEncoded
    @POST("/ArduinoCloud/updateToken.php")
    Call<List<Token>> getTokenUpdate(
            @Field("userid") String userid,
            @Field("tokenid") String tokenid,
            @Field("token") String token);

    @FormUrlEncoded
    @POST("/ArduinoCloud/insertToken.php")
    Call<List<Token>> getTokenInsert(
            @Field("userid") String userid,
            @Field("token") String token);

    @FormUrlEncoded
    @POST("/ArduinoCloud/updateUserToken.php")
    Call<Message> getUserToken(@Field("userid") String userid,
                               @Field("token") String token);

    @FormUrlEncoded
    @POST("/ArduinoCloud/getUserTokenFirst.php")
    Call<User> getUserTokenFirst(@Field("userid") String userid);

}
