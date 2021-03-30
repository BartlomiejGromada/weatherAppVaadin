package pl.gromada.spring_boot_current_weather.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Coord {

    private double lon;
    private double lat;
}
