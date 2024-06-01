package swagger.api.demo.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
   // private static final Logger logger = LoggerFactory.getLogger(PropertyReader.class);
    public static void loadEnvProperties() throws ApiException {
        String fileName = String.format("src/main/resources/env/%s.properties", getEnv());
        InputStream inputStream = null;
        try {
            File file = new File(fileName);
            inputStream = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(inputStream);
            if(!properties.isEmpty())
                for(Object key: properties.keySet())
                    System.setProperty(key.toString(), properties.getProperty(key.toString()));
        } catch(Exception ex) {
            throw new ApiException(String.format("Unable to read property file: {s}", fileName));
        } finally {
            if(inputStream!=null) {
                try {
                    inputStream.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void loadConfigProperties() throws ApiException {
        String fileName = "src/main/resources/config/config.properties";
        InputStream inputStream = null;
        try {
            File file = new File(fileName);
            inputStream = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(inputStream);
            if(!properties.isEmpty())
                for(Object key: properties.keySet())
                    System.setProperty(key.toString(), properties.getProperty(key.toString()));
        } catch(Exception ex) {
            throw new ApiException(String.format("Unable to read property file: {s}", fileName));
        } finally {
            if(inputStream!=null) {
                try {
                    inputStream.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static String getEnv() throws ApiException {
        String env=System.getProperty("env");
        if(env==null) {
            loadConfigProperties();
            env=System.getProperty("env");
            if(env==null)
                System.setProperty("env", "test");
        }
        return System.getProperty("env");
    }

}
