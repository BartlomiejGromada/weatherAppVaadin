package pl.gromada.spring_boot_current_weather.frontend;


import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import pl.gromada.spring_boot_current_weather.backend.enums.Unit;
import pl.gromada.spring_boot_current_weather.backend.model.CurrentWeather;
import pl.gromada.spring_boot_current_weather.backend.model.Weather;
import pl.gromada.spring_boot_current_weather.backend.service.CurrentWeatherService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Route("/")
@CssImport("./styles/style.css")
public class WeatherUI extends HorizontalLayout {

    private CurrentWeatherService currentWeatherService;

    private TextField textFieldCity = new TextField();
    private ComboBox<Unit> unitComboBox = new ComboBox<>();
    private Button buttonCheck = new Button(VaadinIcon.SEARCH.create());

    private VerticalLayout resultMainLayout = new VerticalLayout();

    private String tempUnit;
    private String windUnit;

    public WeatherUI(CurrentWeatherService currentWeatherService) {
        this.currentWeatherService = currentWeatherService;

        setSizeFull();
        setDefaultVerticalComponentAlignment(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(buildSearchLayout(), buildResultLayout());
    }

    private VerticalLayout buildSearchLayout() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addClassName("searchBox");
        mainLayout.setWidth("35%");
        mainLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        mainLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        Image image = new Image("https://images.squarespace-cdn.com/content/v1/5572b7b4e4b0a20071d407d4/1487090874274" +
                "-FH2ZNWOTRU90UAF5TA2B/ke17ZwdGBToddI8pDm48kCMWMBFcqQftRz-JqZZoIB5Zw-zPPgdn4jUwVcJE1ZvWEtT5uBSRWt4vQZAgT" +
                "JucoTqqXjS3CfNDSuuf31e0tVFI99ncPZu898P4WAmVYNBp8mgB1qWbp5RirnU_Xvq-XCb8BodarTVrzIWCp72ioWw/Weather+Targeting", "sun");
        image.addClassName("imageSearch");

        HorizontalLayout layoutCheckHorizontal = new HorizontalLayout();
        layoutCheckHorizontal.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        layoutCheckHorizontal.setWidth("70%");
        layoutCheckHorizontal.setAlignItems(Alignment.CENTER);

        textFieldCity.setPlaceholder("City...");
        textFieldCity.addClassName("searchTextField");
        textFieldCity.setClearButtonVisible(true);
        textFieldCity.setAutofocus(true);

        buttonCheck.addClassName("searchButton");
        buttonCheck.addClickShortcut(Key.ENTER);
        buttonCheck.addClickListener(click -> checkCurrentWeather());
        layoutCheckHorizontal.add(textFieldCity, buttonCheck);

        unitComboBox.setItems(Unit.values());
        unitComboBox.addClassName("unitComboBox");
        unitComboBox.setValue(Unit.Celsius);
        mainLayout.add(image, unitComboBox, layoutCheckHorizontal);
        mainLayout.setHorizontalComponentAlignment(Alignment.END, unitComboBox);

        return mainLayout;
    }

    private VerticalLayout buildResultLayout() {
        resultMainLayout.addClassName("resultBox");
        resultMainLayout.setWidth("35%");
        resultMainLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        resultMainLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        return resultMainLayout;
    }

    private void checkCurrentWeather() {
        if (!textFieldCity.getValue().equals("")) {
            setUnits();
            CurrentWeather currentWeather = currentWeatherService.findWeatherByCityName(textFieldCity.getValue(),
                    unitComboBox.getValue().name);
            resultMainLayout.removeAll();
            if (currentWeather.getCoord() != null) {
                textFieldCity.setInvalid(false);
                setResultCurrentWeather(currentWeather);
            } else {
                textFieldCity.setInvalid(true);
                textFieldCity.setErrorMessage("Enter valid city name");
            }
        }
    }

    private void setResultCurrentWeather(CurrentWeather currentWeather) {
        H1 cityName = new H1(textFieldCity.getValue());

        H3 h3Coordinates = new H3("Coordinates");
        HorizontalLayout layoutCoordinates = new HorizontalLayout();
        Paragraph lat = new Paragraph();
        lat.addClassName("coordinates");
        Paragraph lon = new Paragraph();
        lon.addClassName("coordinates");
        layoutCoordinates.add(lat, lon);
        lat.setText("Latitude: " + currentWeather.getCoord().getLat());
        lon.setText("Longitude: " + currentWeather.getCoord().getLon());

        H3 h3Weather = new H3("Weather");
        Paragraph weather = new Paragraph();
        weather.setText(getWeatherString(currentWeather));

        H3 h3Temperature = new H3("Temperature");
        HorizontalLayout layoutTemp = new HorizontalLayout();
        Paragraph temp = new Paragraph("Temperature: " + currentWeather.getMain().getTemp() + " " + tempUnit);
        Paragraph tempLike = new Paragraph("Feels like: " + currentWeather.getMain().getTempFeelsLike() + " " + tempUnit);
        Paragraph tempMin = new Paragraph("Min: " + currentWeather.getMain().getTempMin() + " " + tempUnit);
        Paragraph tempMax = new Paragraph("Max: " + currentWeather.getMain().getTempMax() + " " + tempUnit);
        layoutTemp.add(temp, tempLike, tempMin, tempMax);

        H3 h3Wind = new H3("Wind");
        Paragraph windSpeed = new Paragraph("Speed: " + currentWeather.getWind().getSpeed() + " " + windUnit);

        H3 h3Sun = new H3("Sun");
        HorizontalLayout layoutSun = new HorizontalLayout();
        Paragraph sunrise = new Paragraph("Sunrise: " + convertTime(currentWeather.getSys().getSunrise()));
        Paragraph sunset = new Paragraph("Sunset: " + convertTime(currentWeather.getSys().getSunset()));
        layoutSun.add(sunrise, sunset);

        resultMainLayout.add(cityName, h3Coordinates, layoutCoordinates, h3Weather, weather,
                h3Temperature, layoutTemp, h3Wind, windSpeed, h3Sun, layoutSun);
    }

    private void setUnits() {
        if (unitComboBox.getValue().name.equals("metric")) {
            windUnit = "meter/sec";
            tempUnit = "\u2103";
        } else if (unitComboBox.getValue().name.equals("imperial")) {
            windUnit = "miles/hour";
            tempUnit = "\u2109";
        } else {
            windUnit = "meter/sec";
            tempUnit = "\u212A";
        }
    }

    private String getWeatherString(CurrentWeather currentWeather) {
        List<Weather> weatherList = currentWeather.getWeather();
        StringBuilder weatherString = new StringBuilder();
        for (Weather w : weatherList) {
            weatherString.append(w).append("\n");
        }

        return weatherString.toString();
    }

    public String convertTime(long time) {
        Date date = new java.util.Date(time * 1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+2"));
        return sdf.format(date);
    }
}
