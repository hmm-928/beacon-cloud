package com.mashibing.search.config;

import com.netflix.ribbon.proxy.annotation.Http;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author zjw
 * @description
 */
@Configuration
public class RestHighLevelClientConfig {

    @Value("#{'${elasticsearch.hostAndPorts}'.split(',')}")
    private List<String> hostAndPorts;

    @Value("${elasticsearch.username}")
    private String username;


    @Value("${elasticsearch.password}")
    private String password;

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        // 初始化连接ES的HttpHost信息
        HttpHost[] httpHosts = new HttpHost[hostAndPorts.size()];
        for (int i = 0; i < hostAndPorts.size(); i++) {
            String[] hostAndPort = hostAndPorts.get(i).split(":");
            httpHosts[i] = new HttpHost(hostAndPort[0],Integer.parseInt(hostAndPort[1]));
        }

        // 设置认证信息
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials(username,password));

        // 构建时设置连接信息，基于set设置认证信息
        RestClientBuilder restClientBuilder = RestClient.builder(httpHosts);
        restClientBuilder.setHttpClientConfigCallback(f -> f.setDefaultCredentialsProvider(credentialsProvider));

        // 构建连接ES的client对象
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);

        // 返回
        return restHighLevelClient;
    }
}

