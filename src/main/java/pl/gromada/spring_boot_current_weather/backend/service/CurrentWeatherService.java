package pl.gromada.spring_boot_current_weather.backend.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.gromada.spring_boot_current_weather.backend.model.CurrentWeather;

import java.io.IOException;
import java.util.Objects;

@Service
public class CurrentWeatherService {

    @Value("${current.weather.api}")
    private String API_KEY;

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CurrentWeather findWeatherByCityName(String city, String unit) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=" + unit + "&appid=" + API_KEY;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try (Response response = client.newCall(request).execute()) {
            String json = Objects.requireNonNull(response.body()).string();
            CurrentWeather currentWeather = objectMapper.readValue(json, CurrentWeather.class);
            return currentWeather;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

}
