package utils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
public class FileManager {

        public static void createDailyFolders() {
            String baseDir = System.getProperty("user.dir");  // Base directory where your folders will be created
            String dateSuffix = new SimpleDateFormat("yyyyMMdd").format(new Date());  // Current date in YYYYMMDD format

            String screenshotsDir = baseDir + "/screenshots/screenshots_" + dateSuffix;
            String reportsDir = baseDir + "/reports/reports_" + dateSuffix;

            createDirectory(screenshotsDir);
            createDirectory(reportsDir);
        }

        private static void createDirectory(String path) {
            File directory = new File(path);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (created) {
                    System.out.println("Created directory: " + path);
                } else {
                    System.err.println("Failed to create directory: " + path);
                }
            }
        }
    }


