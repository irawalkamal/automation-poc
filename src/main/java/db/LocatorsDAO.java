package db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LocatorsDAO {
        public static Map<String, String> getLocators(String className) {
            Map<String, String> locators = new HashMap<>();
            String query = "SELECT * FROM get_locators(?)";

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, className);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String elementName = resultSet.getString("element_name");
                    String locatorType = resultSet.getString("locator_type");
                    String locatorValue = resultSet.getString("locator_value");
                    String displayName = resultSet.getString("display_name");
                    locators.put(elementName, locatorType + "=" + locatorValue);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return locators;
        }
}
