import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Table extends Database {

    private static final String SELECT_TABLE = "SELECT";
    private static final String FROM = "FROM ";
    private static final String WHERE = "WHERE";
    private static final String INSERT = "INSERT INTO ";
    private static final String VALUES = "VALUES ";
    private static final String USER_TABLE_VALUES = "(?, ?, ?, ?)";
    private static final String BOOK_TABLE_VALUES = "(?, ?, ?)";
    private static final String RESERVATION_TABLE_VALUES = "(?, ?, ?, ?)";
    private static final String FINE_TABLE_VALUES = "(?, ?, ?)";

    String tableName;


    public Table(String tableName) throws SQLException {
        super();
        this.tableName = tableName;
    }

    private String formatColumn(ArrayList<String> columns) {
        String columnsList = "";

        for (int i = 0; i < columns.size(); i++) {
            if (i == 0) {
                columnsList = columnsList + columns.get(i);
            } else {
                columnsList = columnsList + "," + columns.get(i);
            }
        }

        return columnsList;
    }

    private String formatColumns(ArrayList<Object> columns, String tableName) {
        String columnsList = "";

        for (int i = 0; i < columns.size(); i++) {
            if (i == 0) {
                columnsList = columnsList + columns.get(i);
            } else {
                columnsList = columnsList + "," + columns.get(i);
            }
        }

        return columnsList;
    }

    private String formatSelectSQLStatement(ArrayList<String> columns) {
        String columnsList = formatColumn(columns);
        String formattedSQL = SELECT_TABLE + " " + columnsList + " " + FROM + tableName;

        return formattedSQL;
    }

    private ArrayList<ArrayList<String>> generateSQLArrayList(ResultSet setOfResults, ArrayList<String> columns) throws SQLException {
        ArrayList<ArrayList<String>> listOfResults = new ArrayList<>();

        while (setOfResults.next()) {
            ArrayList<String> row = new ArrayList<>();

            for (int i = 0; i < columns.size(); i++) {
                row.add(setOfResults.getString(columns.get(i)));
            }
            listOfResults.add(row);
        }
        return listOfResults;
    }

    public ArrayList<ArrayList<String>> selectFromTable(ArrayList<String> columns) throws SQLException {
        String formattedSQL = formatSelectSQLStatement(columns);

        Statement statement = getConnection().createStatement();
        ResultSet setOfResults = statement.executeQuery(formattedSQL);


        return generateSQLArrayList(setOfResults, columns);
    }

    public ArrayList<ArrayList<String>> selectFromTable(ArrayList<String> columns, String whereStatement) throws SQLException {
        String formattedSQL = formatSelectSQLStatement(columns);
        formattedSQL = formattedSQL + WHERE + " " + whereStatement;
        Statement statement = getConnection().createStatement();
        ResultSet setOfResults = statement.executeQuery(formattedSQL);

        return generateSQLArrayList(setOfResults, columns);

    }

    private boolean verifyRowsUpdated(int rowsUpdated) {
        if (rowsUpdated > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean insertIntoUserTable(PreparedStatement preparedStatement, ArrayList<Object> values) throws SQLException {

        preparedStatement.setInt(1, (Integer) values.get(0));
        preparedStatement.setString(2, (String) values.get(1));
        preparedStatement.setString(3, (String) values.get(2));
        preparedStatement.setString(4, (String) values.get(3));

        int rowsUpdated = preparedStatement.executeUpdate();

        if (verifyRowsUpdated(rowsUpdated)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean insertIntoBookTable(PreparedStatement preparedStatement, ArrayList<Object> values) throws SQLException {

        preparedStatement.setInt(1, (Integer) values.get(0));
        preparedStatement.setString(2, (String) values.get(1));
        preparedStatement.setInt(3, (Integer) values.get(2));

        int rowsUpdated = preparedStatement.executeUpdate();

        if (verifyRowsUpdated(rowsUpdated)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean insertIntoReservationTable(PreparedStatement preparedStatement, ArrayList<Object> values) throws SQLException {

        preparedStatement.setInt(1, (Integer) values.get(0));
        preparedStatement.setInt(2, (Integer) values.get(1));
        preparedStatement.setInt(3, (Integer) values.get(2));

        int rowsUpdated = preparedStatement.executeUpdate();

        if (verifyRowsUpdated(rowsUpdated)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean insertIntoFinesTable(PreparedStatement preparedStatement, ArrayList<Object> values) throws SQLException {

        preparedStatement.setInt(1, (Integer) values.get(0));
        preparedStatement.setBigDecimal(2, (BigDecimal) values.get(1));
        preparedStatement.setInt(3, (Integer) values.get(2));

        int rowsUpdated = preparedStatement.executeUpdate();

        if (verifyRowsUpdated(rowsUpdated)) {
            return true;
        } else {
            return false;
        }
    }


    private String formatInsertStatement(String currentlyFormattedString, TableTypes tableType) {
        if (tableType == TableTypes.USER_TABLE) {
            currentlyFormattedString = currentlyFormattedString + USER_TABLE_VALUES;
        } else if (tableType == TableTypes.BOOK_TABLE) {
            currentlyFormattedString = currentlyFormattedString + BOOK_TABLE_VALUES;
        } else if (tableType == TableTypes.RESERVE_TABLE) {
            currentlyFormattedString = currentlyFormattedString + RESERVATION_TABLE_VALUES;
        } else if (tableType == TableTypes.FINE_TABLE) {
            currentlyFormattedString = currentlyFormattedString + FINE_TABLE_VALUES;
        }

        return currentlyFormattedString;
    }

    public boolean insertIntoTable(ArrayList<String> columns, ArrayList<Object> values, TableTypes tableTypes) throws SQLException {
        PreparedStatement preparedStatement = null;

        String formattedColumns = formatColumn(columns);


        String InsertStatement = INSERT + tableName + " (" + formattedColumns + ") " + VALUES;

        String reFormattedInsertStatement = formatInsertStatement(InsertStatement, tableTypes);

        preparedStatement = getConnection().prepareStatement(reFormattedInsertStatement);
        switch (tableTypes) {
            case USER_TABLE:
                if (insertIntoUserTable(preparedStatement, values)) {
                    return true;
                } else {
                    return false;
                }
            case BOOK_TABLE:
                if (insertIntoBookTable(preparedStatement, values)) {
                    return true;
                } else {
                    return false;
                }
            case RESERVE_TABLE:
                if (insertIntoReservationTable(preparedStatement, values)) {
                    return true;
                } else {
                    return false;
                }
            case FINE_TABLE:
                if (insertIntoReservationTable(preparedStatement, values)) {
                    return true;
                } else {
                    return false;
                }

        }
        return false;
    }
}








