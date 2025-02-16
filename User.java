import java.sql.SQLException;
import java.util.ArrayList;

public class User extends SignedInAccount{
    public User(String name, String password, AccountType accountType, Integer id) throws SQLException {
        super(name, password, accountType, id);
    }

    public boolean withdrawBook(BookCopies copiesOfBook) throws SQLException {
        if (copiesOfBook.getCopies() == 0) {
            return false;
        }

        ArrayList<String> columnsToUpdate = new ArrayList<String>();
        ArrayList<Object> valuesToUpdate = new ArrayList<Object>();
        ArrayList<String> columnsForWhere = new ArrayList<String>();
        ArrayList<Object> valuesForWhere = new ArrayList<Object>();

        columnsToUpdate.add("Copies");
        valuesToUpdate.add(copiesOfBook.getCopies() + 1);

        columnsForWhere.add("Bookcode");
        valuesForWhere.add(copiesOfBook.getBook().getBookCode());

        bookTable.updateStatement(columnsToUpdate, valuesToUpdate, columnsForWhere, valuesForWhere);

        copiesOfBook.setCopies(copiesOfBook.getCopies() - 1);

        return true;
    }
}
