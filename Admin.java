import java.sql.SQLException;

public class Admin extends SignedInAccount {
    public Admin(String name, String password, AccountType accountType, Integer id) throws SQLException {
        super(name, password, accountType, id);
    }
}
