import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.*;

public class Main {
    public static ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=uUM50geeFaUq8ORF4aVJWeoqWc8WP944dIyJAQt4");

        CloseableHttpResponse response = httpClient.execute(request);
        Response responseNasa = mapper.readValue(
                response.getEntity().getContent(), new TypeReference<>(){}
        );
        response.close();
        String url = responseNasa.getUrl();
        System.out.println(url);
        HttpGet request1 = new HttpGet(url);
        CloseableHttpResponse response1 = httpClient.execute(request1);
        BufferedInputStream bis = new BufferedInputStream(response1.getEntity().getContent());
        String filePath = "67P_210907_1024.jpg";
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
        int inByte;
        while((inByte = bis.read()) != -1) bos.write(inByte);
        bis.close();
        bos.close();
        response1.close();
        httpClient.close();
    }
}
