import java.sql.SQLException;
import java.util.ArrayList;

public class NotSignedInAccount extends AllAccounts{

    String enteredUsername;
    String enteredPassword;
    AccountType type;
    int id;

    public NotSignedInAccount(String enteredUsername, String enteredPassword, AccountType type) throws SQLException {
        super();
        this.enteredUsername = enteredUsername;
        this.enteredPassword = enteredPassword;
        this.type = type;
    }

    public boolean checkCredentials() throws SQLException {
        try {
             userTable = new Table("users");

            ArrayList<String> columns = new ArrayList<>();
            columns.add("Username");
            columns.add("Password");
            columns.add("Type");

            ArrayList<String> columnsForWhere = new ArrayList<>();
            ArrayList<Object> valuesForWhere = new ArrayList<Object>();

            columnsForWhere.add(USER);
            columnsForWhere.add(PASSWORD);
            columnsForWhere.add(TYPE);

            valuesForWhere.add(enteredUsername);
            valuesForWhere.add(enteredPassword);
            valuesForWhere.add(convertAccountTypeToString(type));

            ArrayList<ArrayList<String>> userResults = userTable.selectFromTable
                    (columns, columnsForWhere, valuesForWhere);

            if (userResults.size() == 0) {
                return false;
            } else {
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public SignedInAccount convertAccountToAccount() throws SQLException {
        if (type == AccountType.ADMIN) {
            return new Admin(enteredUsername, enteredPassword, type, getAccountID(
                    enteredUsername, enteredPassword, type
            ));
        } else if (type == AccountType.USER) {
            return new User(enteredUsername, enteredPassword, type, getAccountID(
                    enteredUsername, enteredPassword, type)
            );

        }
        return null;
    }
}
