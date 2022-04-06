package org.estrada.tp1.http;

import org.estrada.tp1.CustomGson;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitCookie {

    private static ServiceCookie instance;

    public static ServiceCookie get(){
        if(instance == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(CustomGson.getIt()))
                    .client(client())
                    .baseUrl("http://10.0.2.2:8080/")
                    .build();

            instance = retrofit.create(ServiceCookie.class);
            return instance;
        }else {
            return instance;
        }

    }

    private static OkHttpClient client() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        CookieJar jar = new SessionCookieJar();
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(jar)
                .addInterceptor(interceptor)
                .build();
        return client;
    }

}
