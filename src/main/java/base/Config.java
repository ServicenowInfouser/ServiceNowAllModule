
package base;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();
    static {
        try (InputStream is = Config.class.getClassLoader()
                                          .getResourceAsStream("config.properties")) {
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }
    public static String get(String key) { return props.getProperty(key); }
    
    public static boolean headless() { 
        String h = System.getProperty("headless", props.getProperty("headless", "true"));
        return "true".equalsIgnoreCase(h);
    }
    
    public static String baseUrl() { return props.getProperty("baseUrl"); }
    public static String username() { return props.getProperty("username"); }
    public static String password() { return props.getProperty("password"); }
    
    public static int explicitWaitSeconds() { 
        return Integer.parseInt(props.getProperty("explicitWaitSeconds", "100"));
    }
}
