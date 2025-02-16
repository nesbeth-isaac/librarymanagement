import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class Table extends Database {

    protected static final String SELECT_TABLE = "SELECT";
    protected static final String AND = "AND";
    protected static final String FROM = "FROM ";
    protected static final String WHERE = "WHERE";
    protected static final String INSERT = "INSERT INTO ";
    protected static final String VALUES = "VALUES ";
    protected static final String USER_TABLE_VALUES = "(?, ?, ?, ?)";
    protected static final String BOOK_TABLE_VALUES = "(?, ?, ?)";
    protected static final String RESERVATION_TABLE_VALUES = "(?, ?, ?, ?)";
    protected static final String FINE_TABLE_VALUES = "(?, ?, ?)";
    protected static final String END_OF_QUERY_MARK = ";";
    protected static final String UPDATE_TABLE = "UPDATE";
    protected static final String SET_VALUE = "SET";
    protected static final String USER_EQUALS = "Username='";
    protected static final String PASSWORD_EQUALS = "Password='";
    protected static final String TYPE_EQUALS = "Type='";
    protected static final String OPEN_QUOTE = "'";
    protected static final String CLOSE_QUOTE = "'";
    protected static final String SPACE = " ";
    protected static final String EQUALS = "=";
    protected static final String COMMA = ",";
    protected static final String DELETE = "DELETE";


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

    /**
     * Method to select results from established SQL table.
     * @param columnsToSelect accepts a String ArrayList of the columns that should be selected.
     * @param columnsForWHERE accepts a String ArrayList of the columns that should be part of the WHERE statement.
     * @param values accepts an Object ArrayList for the values that should be assigned to each value in the columnsForWhere
     *               ArrayList.
     * @return an ArrayList containing individual String ArrayLists of each row in the result.
     * @throws SQLException throws exception according to SQL Exception.
     */
    public ArrayList<ArrayList<String>> selectFromTable(ArrayList<String> columnsToSelect, ArrayList<String> columnsForWHERE,
                                                        ArrayList<Object> values) throws SQLException {
        String formattedSQL = formatSelectSQLStatement(columnsToSelect);
        formattedSQL = formattedSQL + SPACE + WHERE + SPACE + formatMultipleConditionStatements(columnsForWHERE, values,
                SQLStatements.WHERE, true);
        System.out.println(formattedSQL);
        Statement statement = getConnection().createStatement();
        ResultSet setOfResults = statement.executeQuery(formattedSQL);

        return generateSQLArrayList(setOfResults, columnsToSelect);

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

    /**
     * Updates SQL Table with new values.
     * @param columnstoUpdate ArrayList<String> of the columns you wish to update.
     * @param valuesToBeUpdated ArrayList<String> of the values for the corresponding columns you want to update.
     * @return a boolean. True if successfully executed and false if not.
     * @throws SQLException
     */
    public boolean updateStatement(ArrayList<String> columnstoUpdate, ArrayList<Object> valuesToBeUpdated) throws SQLException {
        try {
            PreparedStatement preparedStatement = null;

            String updateStatement = UPDATE_TABLE + SPACE + tableName + SPACE + SET_VALUE + SPACE +
                    formatMultipleConditionStatements(columnstoUpdate, valuesToBeUpdated, SQLStatements.SET, true);
            preparedStatement = getConnection().prepareStatement(updateStatement);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    /**
     * Updates certain SQL statements with new values based on a where condition.
     * @param columnstoUpdate
     * @param valuesToBeUpdated
     * @param columnsForCondition
     * @param valuesForCondition
     * @return
     * @throws SQLException
     */
    public boolean updateStatement(ArrayList<String> columnstoUpdate, ArrayList<Object> valuesToBeUpdated,
                                   ArrayList<String> columnsForCondition, ArrayList<Object> valuesForCondition) throws SQLException {
        try {
            PreparedStatement preparedStatement = null;

            String updateStatement = UPDATE_TABLE + SPACE + tableName + SPACE + SET_VALUE + SPACE +
                    formatMultipleConditionStatements(columnstoUpdate, valuesToBeUpdated, SQLStatements.SET, false)
                    + SPACE + WHERE + formatMultipleConditionStatements(columnsForCondition, valuesForCondition,
                    SQLStatements.WHERE, true);
            preparedStatement = getConnection().prepareStatement(updateStatement);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    /**
     * This method formats the WHERE and SET parts of statements for SQL queries.
     *
     *
     * @param columns accepts an ArrayList<String> of columns to include in either the 'SET' part of an UPDATE Statement
     *                or the 'WHERE' part of an UPDATE/ WHERE statement.
     * @param values accepts the corresponding values to be included in either the 'SET' part of an UPDATE Statement or
     *               the 'WHERE' part of an UPDATE/ WHERE statement.
     * @param statement accepts the enum SQLStatement values, and is used to determine if a SET or WHERE statement
     *                  should be produced.
     * @return a String either including the SET/WHERE statements.
     */
    private String formatMultipleConditionStatements(ArrayList<String> columns, ArrayList<Object> values,
                                                    SQLStatements statement, boolean isEndOfStatement) {
        String currentStatment = "";
        if (columns.size() == 1 && isEndOfStatement) {
            currentStatment = currentStatment + SPACE + columns.get(0) + SPACE + EQUALS +
                    formatValueAccordingTooObject(values.get(0)) + END_OF_QUERY_MARK;
        } else if (columns.size() ==1 && !isEndOfStatement) {
            currentStatment = currentStatment + SPACE + columns.get(0) + SPACE + EQUALS +
                    formatValueAccordingTooObject(values.get(0));
        }
        else {
            for (int i = 0; i < columns.size(); i++) {
                if (i == columns.size() - 1) {
                    currentStatment = currentStatment + SPACE + columns.get(i) + SPACE + EQUALS + OPEN_QUOTE +
                            String.valueOf((values.get(i))) + CLOSE_QUOTE + END_OF_QUERY_MARK;
                } else {
                    if (statement == SQLStatements.SET) {
                        currentStatment = currentStatment + SPACE + columns.get(i) + SPACE + EQUALS +
                                formatValueAccordingTooObject(values.get(i)) + COMMA;
                    } else if (statement == SQLStatements.WHERE) {
                        currentStatment = currentStatment + SPACE + WHERE + SPACE + columns.get(i) + SPACE + EQUALS +
                                formatValueAccordingTooObject(values.get(i))  + SPACE+ AND;
                    }
                }
            }

        }
        return currentStatment;

    }

    private String formatValueAccordingTooObject(Object currentObject){
        String currentString = "";
        if (currentObject instanceof String) {
             currentString = OPEN_QUOTE + currentObject + CLOSE_QUOTE;
        } else if (currentObject instanceof Integer) {
            currentString = currentObject.toString();
        }
        return currentString;
    }

    public boolean deleteFromTable(ArrayList<String> columnsForWhere, ArrayList<Object>valuesForWhere
    ) throws SQLException {
        try {
            PreparedStatement preparedStatement = null;
            String SQLStatement = DELETE + SPACE + FROM + tableName + SPACE + formatMultipleConditionStatements(
                    columnsForWhere, valuesForWhere, SQLStatements.WHERE, true
            );
            System.out.println(SQLStatement);

            preparedStatement = getConnection().prepareStatement(SQLStatement);
            preparedStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            return false;
        }

    }

}








