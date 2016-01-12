import db.Account;
import db.DBManager;
import exc.AccountExistsException;

import java.sql.SQLException;
import java.util.List;

public class Frosty {

    private static final String DBHost = "192.168.1.100";
    private static final String DBUser = "frosty-bot";
    private static final String DBPass = "LeJuqANIlerogIs7yONusIgAHeTibi";
    private static final String DBName = "frosty";


    public static void main(String[] args) {
        try {
            DBManager dmgr = DBManager.connect(DBUser, DBPass, DBName, DBHost);

            List<Account> accs = Account.findAll();
            for(Account acc : accs) {
                System.out.print(acc.getId());
                System.out.print(':');
                System.out.print(acc.getMail());
                System.out.print(':');
                System.out.println(acc.getPassword());
            }

            /*
            try {
                Account acc = Account.create("sfdfdf", "fdfs");
            } catch (AccountExistsException e) {
                e.printStackTrace();
            }
            */

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
