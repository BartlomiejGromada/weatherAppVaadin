package pl.gromada.spring_boot_current_weather.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class CurrentWeather {

    private Coord coord;
    private List<Weather> weather;
    private Main main;
    private Wind wind;
    private Sys sys;

}
