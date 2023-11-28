package pl.edu.agh.to.weatherapp.api;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

class WeatherApiFetcherTest {
    private static final String URL_FOR_CURRENT_QUERY = "https://api.weatherapi.com/v1/current.json?key=key&q=TARN%C3%93W";
    private static final String URL_FOR_FUTURE_QUERY = "https://api.weatherapi.com/v1/forecast.json?key=key&q=TARN%C3%93W&days=1";
    private static final String LOCATION = "TARNÓW";
    private static final String API_KEY = "key";
    private static  final String RESPONSE = "someResponse";
    @Mock
    private final HttpClient client = Mockito.mock(HttpClient.class);
    @Mock
    private final WeatherApiFetcher weatherApiFetcher = new WeatherApiFetcher(API_KEY, client);
    @Mock
    private final HttpResponse httpResponse = Mockito.mock(HttpResponse.class);


    @Test
    @SneakyThrows
    void buildsProperHttpResponseForCurrentDate() {
        //given
        Mockito.when(httpResponse.body()).thenReturn(RESPONSE);
        Mockito.when(client.sendAsync(
                Mockito.eq(creteRequestFromUrl(URL_FOR_CURRENT_QUERY)), Mockito.any())
        ).thenReturn(CompletableFuture.completedFuture(httpResponse));

        //when
        var fetchResult = weatherApiFetcher.fetchCurrent(LOCATION);

        //then
        assertThat(fetchResult.get()).isEqualTo(RESPONSE);
    }

    @Test
    @SneakyThrows
    void buildsProperHttpResponseForFutureDate() {
        //given
        Mockito.when(httpResponse.body()).thenReturn(RESPONSE);
        Mockito.when(client.sendAsync(
                Mockito.eq(creteRequestFromUrl(URL_FOR_FUTURE_QUERY)), Mockito.any())
        ).thenReturn(CompletableFuture.completedFuture(httpResponse));

        //when
        var fetchResult = weatherApiFetcher.fetchForecast(LOCATION, 1);

        //then
        assertThat(fetchResult.get()).isEqualTo(RESPONSE);
    }

    private HttpRequest creteRequestFromUrl(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
    }
}