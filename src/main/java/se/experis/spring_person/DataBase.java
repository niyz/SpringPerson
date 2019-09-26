package se.experis;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    public DataBase(){
    }

    public Connection dbConnect() throws SQLException{
        Connection conn = null;

        try {
            String dbUrl = "jdbc:sqlite::resource:Task17DB.db"; // add dbname.
            conn = DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }


    /**
     * 1 = dad
     * 2 = mom
     * 3 = son
     * 4 = daughter
     * 5 = brother
     * 6 = sister
     */


    public ArrayList<Person> dbSearch(String searchStr){
        ArrayList<Person> pList = new ArrayList<>();
        Connection conn = null;
        ArrayList<String> phoneList = null;
        ArrayList<String> mailList = new ArrayList<>();
        Address addr = null;
        Person p = null;
        try {
            if(isLong(searchStr)){
                // do the phone lookup thing
                conn = this.dbConnect();
                String sql = "SELECT personid FROM phone WHERE phone LIKE ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "%" + searchStr + "%");
                ResultSet result = pstmt.executeQuery();


                while(result.next()){
                    phoneList = this.getdbPhoneList(conn, result.getInt("personid"));
                    mailList = this.getdbEmailList(conn, result.getInt("personid"));

                    String addressidSQL = "SELECT adressid FROM person WHERE id = ?";
                    PreparedStatement addrstmt = conn.prepareStatement(addressidSQL);
                    addrstmt.setInt(1, result.getInt("personid"));
                    ResultSet addResult = addrstmt.executeQuery();
                    int addrId = 0;
                    while(addResult.next()){
                        addrId = addResult.getInt("adressid");
                    }


                    addr = this.getdbAddress(conn, addrId);

                    pList.add(p = getdbPerson(
                            conn,
                            result.getInt("personid"),
                            addr,
                            mailList,
                            phoneList
                    ));
                }
            } else {
                // do the person lookup thing
                conn = this.dbConnect();
                String sql = "SELECT id, adressid FROM person WHERE firstname LIKE ? OR lastname LIKE ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "%" + searchStr + "%");
                pstmt.setString(2, "%" + searchStr + "%");
                ResultSet result = pstmt.executeQuery();


                while(result.next()){
                    phoneList = this.getdbPhoneList(conn, result.getInt("id"));
                    mailList = this.getdbEmailList(conn, result.getInt("id"));
                    addr = this.getdbAddress(conn, result.getInt("adressid"));

                    pList.add(p = getdbPerson(
                            conn,
                            result.getInt("id"),
                            addr,
                            mailList,
                            phoneList
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException in db search");
            System.out.println(e.getMessage());
        } finally {
            try{
                if(conn != null){
                    conn.close();;
                }
            } catch (SQLException e){
                System.out.println("SQL Exeption when closing databas connection in db search");
                System.out.println(e.getMessage());
            }
        }

        return pList;
    }

    /**
     * dbSearch help method
     */
    private boolean isLong(String str){
        try{
            //Integer.parseInt(str);
            Long.parseLong(str);
        } catch (NumberFormatException e){
            //System.out.println("String is not an int. ");
            return false;
        }
        return true;
    }

    private Person getdbPerson(Connection conn, int id, Address address, ArrayList<String> maillist, ArrayList<String> phonelist){
        String sql = "SELECT personid, firstname, lastname FROM person WHERE id = ?";
        ArrayList<String> mailList = maillist;
        ArrayList<String> phoneList = phonelist;
        Address addr = address;
        Person p = null;
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();
            while(result.next()){
                p = new Person(
                        result.getString("firstname"),
                        result.getString("personid"),
                        result.getString("lastname"),
                        phoneList,
                        mailList,
                        addr
                        );
            }
        } catch (SQLException e) {
            System.out.println("SQLException getdbPerson");
            System.out.println(e.getMessage());
        }
        return p;
    }

    private Address getdbAddress(Connection conn, int addressId){
        String sql = "SELECT * FROM adress WHERE adressid = ?";
        Address addr = null;

        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, addressId);
            ResultSet result = pstmt.executeQuery();
            while (result.next()){
                addr = new Address(
                        result.getString("country"),
                        result.getString("city"),
                        result.getString("street"),
                        result.getString("streetnum"),
                        result.getString("postalcode")
                );
            }

        } catch (SQLException e){
            System.out.println("SQLException getdbAddress");
            System.out.println(e.getMessage());
        }
        return addr;
    }

    private ArrayList<String> getdbPhoneList(Connection conn, int id){
        String sql = "SELECT phone FROM phone where personid = ?";
        ArrayList<String> phoneList = new ArrayList<>();

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                phoneList.add(result.getString("phone"));
            }
        } catch (SQLException e){
            System.out.println("sqlexeption in getdbPhoneList");
            System.out.println(e.getMessage());
        }

        return phoneList;
    }

    private ArrayList<String> getdbEmailList(Connection conn, int id){
        String sql = "SELECT email FROM email where personid = ?";
        ArrayList<String> emailList = new ArrayList<>();

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();
            while (result.next()) {
                emailList.add(result.getString("email"));
            }
        } catch (SQLException e){
            System.out.println("sqlexeption in getdbEmailList");
            System.out.println(e.getMessage());
        }

        return emailList;
    }


    /**
     * Elliot
     */
    public void insertPerson(Person person){

        Connection conn = null;


        try {
            conn = dbConnect();
            conn.setAutoCommit(false);
            int adressID = getAddressIdOrAdd(conn,person.getAddress());
            String personInfo = "INSERT INTO person(personID,firstName,lastName,adressID) "
                    + "VALUES('"+person.getPersonID()+"','"+person.getName()+"','"+person.getLastName()+"','"+adressID+"')";
            executeInsertSQL(conn, personInfo);

            String getIDString = "SELECT id FROM person WHERE personID='" + person.getPersonID() + "'";

            String ID = selectAnID(conn, getIDString);

            for (String email : person.getEmailList()) {
                executeInsertSQL(conn, "INSERT INTO email(email,personID) VALUES('" + email + "','" + ID + "')");
            }
            for (String num : person.getPhoneIDList()) {
                executeInsertSQL(conn, "INSERT INTO phone(personID,phone) VALUES('" + ID + "','" + num + "')");
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addRelation(Person p1, Person p2, int typeOfRelation) throws SQLException{
        Connection conn = dbConnect();
        String id1 = selectAnID(conn,"SELECT id FROM person WHERE personID='"+p1.getPersonID()+"'");
        String id2 = selectAnID(conn,"SELECT id FROM person WHERE personID='"+p2.getPersonID()+"'");


        String addRelation = "INSERT INTO relationship(person1,person2,relation) VALUES('"+id1+"','"+id2+"','"+typeOfRelation+"')";

        executeInsertSQL(conn,addRelation);
    }

    public void deleteRelations(Person person) throws SQLException{
        Connection conn = dbConnect();
        String id = selectAnID(conn,"SELECT id FROM person WHERE personID='"+person.getPersonID()+"'");
        String deleteRelation = "DELETE FROM relationship WHERE person1='"+id+"' OR person2='"+id+"'";
        executeInsertSQL(conn,deleteRelation);
        conn.close();
    }

    public void updatePerson(Person oldPerson, Person newPerson){
        Connection conn = null;

        try {
            conn = dbConnect();
            conn.setAutoCommit(false);
            int addressID = getAddressIdOrAdd(conn,newPerson.getAddress());

            String selectPersonSQL = "SELECT id FROM person WHERE personID='"+oldPerson.getPersonID()+"'";

            String ID = selectAnID(conn,selectPersonSQL);

            String executeUpdateString = "UPDATE person SET personID='"+newPerson.getPersonID()+"', firstName='"+newPerson.getName()
                    +"', lastName='"+newPerson.getLastName()+"', adressID='"+addressID+"' WHERE id='"+ID+"'";


            System.out.println(executeUpdateString);

            executeInsertSQL(conn,executeUpdateString);


            ArrayList<String> selectAllId = selectAllId(conn,"SELECT id FROM phone WHERE personID='"+ID+"'");


            int oldSize = selectAllId.size();
            int newSize = newPerson.getPhoneIDList().size();
            for(int i=0;i<oldSize;i++) {
                executeInsertSQL(conn,"UPDATE phone SET phone='"+newPerson.getPhoneIDList().get(i)+"' WHERE id='"+selectAllId.get(i)+"'");
            }

            for(int i=oldSize;i<newSize;i++) {
                executeInsertSQL(conn,"INSERT INTO phone(personID,phone) VALUES('"+ID+"','"+newPerson.getPhoneIDList().get(i)+"')");
            }


            selectAllId = selectAllId(conn,"SELECT id FROM email WHERE personID='"+ID+"'");
            oldSize = selectAllId.size();
            newSize = newPerson.getEmailList().size();
            for(int i=0;i<oldSize;i++) {
                executeInsertSQL(conn,"UPDATE email SET email='"+newPerson.getEmailList().get(i)+"' WHERE id='"+selectAllId.get(i)+"'");
            }
            for(int i=oldSize;i<newSize;i++) {
                executeInsertSQL(conn,"INSERT INTO email(email,personID) VALUES('"+newPerson.getEmailList().get(i)+"','"+ID+"')");
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Help methods to insertPerson
     */

    private static ArrayList<String> selectAllId(Connection conn, String sql) {
        ArrayList<String> ids = new ArrayList<String>();
        //Connection conn = connect();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            //info=rs.getString("adressID");
            while (rs.next()) {
                ids.add(rs.getString("id"));
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return ids;
    }

    private static String selectAdressID(Connection conn, String sql) {
        String info = "";

        //Connection conn = connect();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            //info=rs.getString("adressID");
            while (rs.next()) {
                info=rs.getString("adressID");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return info;
    }

    private String selectAnID(Connection conn, String sql) {
        String info = "";
        //Connection conn = connect();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            //info=rs.getString("id");
            while (rs.next()) {
                info=rs.getString("id");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return info;
    }

    private static int getAddressIdOrAdd(Connection conn, Address address) throws SQLException{

        String getAddressIdString = "SELECT adressID FROM adress WHERE country='"+address.getCountry()+
                "' AND city='"+address.getCity()+"' AND street='"+address.getStreet()+
                "' AND streetNum='"+address.getStreetNum()+"' AND postalCode='"+address.getPostalCode()+"'";

        String addressID = selectAdressID(conn,getAddressIdString);
        if(addressID.equals("")) {
            String insertAddress = "INSERT INTO adress(country,city,street,streetNum,postalCode) VALUES('"+
                    address.getCountry()+"','"+address.getCity()+"','"+address.getStreet()+"','"+address.getStreetNum()+"','"+
                    address.getPostalCode()+"')";

            executeInsertSQL(conn,insertAddress);
            addressID = selectAdressID(conn,getAddressIdString);
        }
        int result = Integer.parseInt(addressID);
        return result;
    }

    private static void executeInsertSQL(Connection conn, String sql) throws SQLException {
        //Connection conn = connect();
        boolean autoCommit = false;
        autoCommit = conn.getAutoCommit();
            //System.out.println("autocommit: " + autoCommit);
        conn.setAutoCommit(false);
        Statement pstmt = conn.createStatement();
        pstmt.executeUpdate(sql);
        //conn.commit();

        /*boolean autoCommit = false;
        try{
            autoCommit = conn.getAutoCommit();
            //System.out.println("autocommit: " + autoCommit);
            conn.setAutoCommit(false);
            Statement pstmt = conn.createStatement();
            pstmt.executeUpdate(sql);
            conn.commit();
        }
        catch (SQLException e) {
            try {
                System.out.println("rolling back");
                conn.rollback();
            } catch (SQLException e1) {
                System.out.println("rollback error: " + e.getMessage());
                e1.printStackTrace();
            }
            System.out.println("sqlerror: " +  e.getMessage());
            e.printStackTrace();
        }
        finally {
            try {
                conn.setAutoCommit(autoCommit);
            } catch (SQLException e) {

                e.printStackTrace();
            }
        }*/
    }

    /**
     * simon
     */

    public void deletePerson(Person person) /*throws SQLException*/{
        try {
            deleteRelations(person);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Connection conn = null;

        try {
            conn = dbConnect();
            String selectPersonSQL = "SELECT id FROM person WHERE personID='"+person.getPersonID()+"'";
            //String selectPersonSQL = "SELECT id FROM person WHERE personID=197702136543";
            //System.out.println(selectPersonSQL);
            String ID = selectAnID(conn,selectPersonSQL);
            //System.out.println("id: " + ID);
            //System.out.println("PHONE: ");
            String sqlStatementPhone = "DELETE FROM phone WHERE personID='"+ID+"'";
            //selectAllId(conn,sqlStatement);
            //System.out.println("MAIL: ");

            String sqlStatementMail = "DELETE FROM email WHERE personID='"+ID+"'";
            String sqlStatementPerson = "DELETE FROM person WHERE id='"+ID+"'";
            conn.setAutoCommit(false);
            executeInsertSQL(conn, sqlStatementPhone);
            executeInsertSQL(conn, sqlStatementMail);
            executeInsertSQL(conn, sqlStatementPerson);
            conn.commit();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public static void main(String[] args) {
        DataBase d = new DataBase();

        ArrayList<String> email = new ArrayList<String>();
        email.add("Sven4aaaadasdasdsadasd20@hotmail.com");
        email.add("svenNyMdasdaasdasdadsil@hotmail.com");
        ArrayList<String> phoneNums = new ArrayList<String>();
        phoneNums.add("0701234567");
        phoneNums.add("10742222321");
        Address adress = new Address("Israel","Los Angeles","Storgatan","420","12345");

        Person oldPerson = new Person("Sven","199909098181","Urbansson",phoneNums,email,adress);
        Person newPerson = new Person("Sven","199909098181","svensson",phoneNums,email,adress);


        //d.insertPerson(oldPerson);
        //d.deletePerson(oldPerson);
        d.updatePerson(oldPerson,newPerson);
        /*try {
            //d.updatePerson(oldPerson,newPerson);
            //d.deletePerson(oldPerson);
        } catch (SQLException e) {
            e.printStackTrace();
        }*/


    }

}
