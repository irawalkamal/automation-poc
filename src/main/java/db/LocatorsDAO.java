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

        try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, className);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String testMethodName = resultSet.getString("test_method_name");
                String elementName = resultSet.getString("element_name");
                String locatorType = resultSet.getString("locator_type");
                String locatorValue = resultSet.getString("locator_value");
                String displayName = resultSet.getString("display_name");
                String defectNumber = resultSet.getString("defect_number");

                // Handle null values if necessary
                testMethodName = (resultSet.wasNull()) ? "" : testMethodName;
                locatorType = (resultSet.wasNull()) ? "" : locatorType;
                locatorValue = (resultSet.wasNull()) ? "" : locatorValue;
                displayName = (resultSet.wasNull()) ? "" : displayName;
                defectNumber = (resultSet.wasNull()) ? "" : defectNumber;
                locators.put(elementName, locatorType + "=" + locatorValue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locators;
    }

    public static String getTestMethodName(String testMethodName) {
        String query = "SELECT testmethodname FROM automationtestmethod WHERE testmethod = ?";

        String testName = null;

        try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, testMethodName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                testName = resultSet.getString("testmethodname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return testName;
    }

    public static String getDefectNumberForTestMethod(String testMethodName) {
        String defectNumber = null;
        String query = "SELECT defectnumber FROM automationtestmethod WHERE testmethod = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, testMethodName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                defectNumber = resultSet.getString("defectnumber");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return defectNumber;
    }

}

