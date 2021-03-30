package pl.gromada.spring_boot_current_weather.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Weather implements Serializable {

    private String main;
    private String description;

    @Override
    public String toString() {
        return main + ": " + description;
    }
}
