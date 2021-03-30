package pl.gromada.spring_boot_current_weather.backend.enums;

public enum Unit {

    Celsius("metric"), Fahrenheit("imperial"), Kelvin("standard");

    public String name;

    Unit(String name) {
        this.name = name;
    }

}

