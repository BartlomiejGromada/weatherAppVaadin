package pl.gromada.spring_boot_current_weather.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Sys {

    private long sunrise;
    private long sunset;
}
