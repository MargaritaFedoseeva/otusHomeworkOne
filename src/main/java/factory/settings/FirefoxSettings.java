package factory.settings;


import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxSettings implements IDriverSettings {
    @Override
    public FirefoxOptions settings() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("-start-debugger-server");
        return firefoxOptions;
    }
}
