import java.sql.SQLException;
import java.util.ArrayList;

public abstract class AllAccounts {

    protected static final String USER = "Username";
    protected static final String PASSWORD = "Password";
    protected static final String TYPE = "Type";
    protected static final String OPEN_QUOTE = "'";
    protected static final String CLOSE_QUOTE = "'";
    protected static final String SPACE = " ";
    protected Table userTable;
    protected Table reserveTable;
    protected Table fineTable;
    protected Table bookTable;

    public AllAccounts() throws SQLException {
        try {
            userTable = new Table("users");
            reserveTable = new Table("reserve");
            fineTable = new Table("fine");
            bookTable = new Table("book");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected String convertAccountTypeToString (AccountType accountType){
        switch(accountType) {
            case ADMIN -> {
                return "Admin";
            }
            case USER -> {
                return "User";
            }
            case NOT_SIGNED_IN -> {
                return "Not Signed In";
            }

        }

        return null;
    }

    protected Integer getAccountID(String username, String password, AccountType accountType) throws SQLException {
        try {
            Table userTable = new Table("users");


        ArrayList<String> columns = new ArrayList<>();
        columns.add("Code");

        ArrayList<String> columnsForWhere = new ArrayList<>();
        ArrayList<Object> valuesForWhere = new ArrayList<>();
        columnsForWhere.add(USER);
        columnsForWhere.add(PASSWORD);
        columnsForWhere.add(TYPE);

        valuesForWhere.add(username);
        valuesForWhere.add(password);
        valuesForWhere.add(convertAccountTypeToString(accountType));



        ArrayList<ArrayList<String>> userResults = userTable.selectFromTable(columns, columnsForWhere, valuesForWhere);

        if (userResults.size() > 0) {
            return 0;
        } else {
            String idAsString = userResults.get(0).get(0);
            int id = Integer.parseInt(idAsString);
            return id;
        }
    } catch (SQLException e) {
        }
        return null;
    }
}
