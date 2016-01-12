package db;

import exc.AccountExistsException;
import exc.AccountNotFoundException;
import exc.IncorrectPasswordException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Account extends DBModel {

    protected static final String TableName = "accounts";

    protected static final String IdColumn = "id";
    protected static final String MailColumn = "mail";
    protected static final String PassColumn = "pass";
    protected static final String SHA1PassColumn = "sha1_pass";

    protected static final String SelectAccounts = "SELECT * FROM " + TableName;
    protected static final String FindAccountById = SelectAccounts + " WHERE " + IdColumn + " = ?;";
    protected static final String FindAccountByMail = SelectAccounts + " WHERE " + MailColumn + " LIKE ?;";
    protected static final String InsertAccount = "INSERT INTO " + TableName + " (" + MailColumn + ',' + PassColumn + ',' + SHA1PassColumn + ") VALUES (?,?,SHA1(?));";

    protected int id;
    protected String mail;
    protected String pass;

    protected Account(int id, String mail, String pass) {
        this.id = id;
        this.mail = mail;
        this.pass = pass;
    }

    public int getId() { return this.id; }
    public String getMail() { return this.mail; }
    public String getPassword() { return this.pass; }

    public static Account find(int id) throws AccountNotFoundException, SQLException {
        PreparedStatement st = DBManager.createStatement(FindAccountById);
        st.setInt(1, id);

        ResultSet r = st.executeQuery();

        if(!r.next())
            throw new AccountNotFoundException();

        r.first();

        return new Account(id, r.getString(MailColumn), r.getString(PassColumn));
    }

    public static Account find(String mail) throws AccountNotFoundException, SQLException {
        PreparedStatement st = DBManager.createStatement(FindAccountByMail);
        st.setString(1, mail);

        ResultSet r = st.executeQuery();

        if(!r.next())
            throw new AccountNotFoundException();

        r.first();

        return new Account(r.getInt(IdColumn), mail, r.getString(PassColumn));
    }

    public static Account find(String mail, String pass) throws AccountNotFoundException, SQLException, IncorrectPasswordException {
        PreparedStatement st = DBManager.createStatement(FindAccountByMail);
        st.setString(1, mail);

        ResultSet r = st.executeQuery();

        if(!r.next())
            throw new AccountNotFoundException();

        r.first();

        String spass = r.getString(PassColumn);
        if(spass == pass)
            return new Account(r.getInt(IdColumn), mail, pass);
        else
            throw new IncorrectPasswordException();
    }

    public static Account create(String mail, String pass) throws SQLException, AccountExistsException {
        try {
            Account ac = Account.find(mail);
            throw new AccountExistsException();
        } catch (AccountNotFoundException e) {
            PreparedStatement st = DBManager.createStatement(InsertAccount);
            st.setString(1, mail);
            st.setString(2, pass);
            st.setString(3, pass);

            int id = st.executeUpdate();

            return new Account(id, mail, pass);
        }
    }

    public static List<Account> findAll() throws SQLException {
        PreparedStatement st = DBManager.createStatement(SelectAccounts);

        ResultSet r = st.executeQuery();
        List<Account> tmp = new ArrayList<>();

        if(r.next()) {
            r.first();
            do {
                tmp.add(new Account(r.getInt(IdColumn), r.getString(MailColumn), r.getString(PassColumn)));
            } while (r.next());
        }

        return tmp;
    }

}
