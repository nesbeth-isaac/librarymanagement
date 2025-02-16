import java.sql.SQLException;
import java.util.ArrayList;

public abstract class SignedInAccount extends AllAccounts{
    protected int id;
    protected String username;
    protected String password;
    protected AccountType accountType;
    protected boolean signInStatus;

    public SignedInAccount(String username, String password, AccountType accountType, Integer id) throws SQLException {
        super();
        this.username = username;
        this.password = password;
        this.accountType = accountType;
        signInStatus = false;

    }

    public void signIn(){
        signInStatus = true;
    }

    public void signOut(){
        signInStatus = false;
    }

    public boolean returnBook(int bookCodeToReturn, String usernameToReturn) throws SQLException {
        try {

            ArrayList<String> columnsToSelect = new ArrayList<>();
            columnsToSelect.add("Code");

            ArrayList<String> columnsForWhere = new ArrayList<>();
            columnsForWhere.add("Username");

            ArrayList<Object> columnsForWhereValues = new ArrayList<>();
            columnsForWhereValues.add(usernameToReturn);
            ArrayList<ArrayList<String>> userCode = userTable.selectFromTable(columnsToSelect,
                    columnsForWhere, columnsForWhereValues);

            String userCodeAsAString = userCode.get(0).get(0);

            if (!doesExist(userCode)) {
                return false;
            }

            columnsToSelect.clear();
            columnsForWhereValues.clear();
            columnsForWhere.clear();


            columnsToSelect.add("Bookcode");
            columnsToSelect.add("Copies");

            columnsForWhere.add("Bookcode");
            columnsForWhereValues.add(bookCodeToReturn);

            ArrayList<ArrayList<String>> reserveTableResults = bookTable.selectFromTable(columnsToSelect, columnsForWhere, columnsForWhereValues);

            ArrayList<String> firstResult = reserveTableResults.get(0);

            String copies = firstResult.get(1);

            int copiesasAnInt = Integer.parseInt(copies);
            copiesasAnInt++;

            copies = String.valueOf(copiesasAnInt);

            columnsToSelect.clear();
            columnsForWhere.clear();
            columnsForWhereValues.clear();

            ArrayList<String> columnsToUpdate = new ArrayList<>();
            ArrayList<Object> valuesToBeUpdated = new ArrayList<>();
            ArrayList<String> columnsForCondition = new ArrayList<>();
            ArrayList<Object> valuesForCondition = new ArrayList<>();

            columnsToUpdate.add("Copies");
            valuesToBeUpdated.add(copies);
            columnsForCondition.add("Bookcode");
            valuesForCondition.add(bookCodeToReturn);

            bookTable.updateStatement(columnsToUpdate, valuesToBeUpdated, columnsForCondition, valuesForCondition);

            columnsForWhere.add("Bookcode");
            columnsForWhere.add("Usercode");

            columnsForWhereValues.add(bookCodeToReturn);
            columnsForWhereValues.add(Integer.parseInt(userCode.getFirst().getFirst()));

            reserveTable.deleteFromTable(columnsForWhere, columnsForWhereValues);

            return true;




        } catch (SQLException e) {
            return false;
        }

    }

    public boolean doesExist(ArrayList<ArrayList<String>> resultsToCheck) throws SQLException{
       if (resultsToCheck.size() == 0){
           return false;
       } else {
           return true;
       }
    }

    public ArrayList<ArrayList<String>> returnBookTable() throws SQLException {
        ArrayList<String> bookColumns = new ArrayList<>();
        bookColumns.add("Bookcode");
        bookColumns.add("Copies");
        bookColumns.add("Bookname");
        return bookTable.selectFromTable(bookColumns);
    }


}
