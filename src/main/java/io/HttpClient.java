package io;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;

/**
 * Created by Florian Noack on 05.06.2017.
 */
public class HttpClient implements Runnable{

    private Thread t;
    protected org.apache.http.client.HttpClient client;
    protected HttpGet request;
    HttpResponse response;
    protected String URL;
    protected boolean active = true;
    protected int callIntervall = 1000;

    public HttpClient(String url){

        this.URL = url;
        RequestConfig config = RequestConfig.custom()
                .setCircularRedirectsAllowed(false)
                .setConnectionRequestTimeout(4000)
                .setConnectTimeout(4000)
                .setMaxRedirects(0)
                .setRedirectsEnabled(false)
                .setSocketTimeout(4000)
                .build();
        request = new HttpGet(url);
        request.setConfig(config);
    }

    public HttpClient(){

    }

    public void run(){

    }

    public HttpResponse getResponse(){
        return response;
    }

    public void start(){
        if(t == null){
            t = new Thread(this);
            t.setDaemon(true);
            t.start();
        }
    }

    public void stop(){
        active = false;
    }

    public void setCallIntervall(int i){
        callIntervall = i;
    }
}