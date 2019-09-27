package se.experis.spring_person.model;

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
            System.out.println("Error: dbConnect() : " + e.getMessage());
        }
        return conn;
    }


    public ArrayList<Person> dbSearch(String searchStr){
        ArrayList<Person> pList = new ArrayList<>();
        Connection conn = null;
        ArrayList<String> phoneList = null;
        ArrayList<String> mailList = new ArrayList<>();
        ArrayList<String> relList = null;
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
                    relList= this.getRelations(conn, result.getInt("personid"));

                    String addressidSQL = "SELECT adressid FROM person WHERE id = ?";
                    PreparedStatement addrstmt = conn.prepareStatement(addressidSQL);
                    addrstmt.setInt(1, result.getInt("personid"));
                    ResultSet addResult = addrstmt.executeQuery();
                    int addrId = 0;
                    while(addResult.next()){
                        addrId = addResult.getInt("adressid");
                    }


                    addr = this.getdbAddress(conn, addrId);
                    p = getdbPerson(
                            conn,
                            result.getInt("personid"),
                            addr,
                            mailList,
                            phoneList
                    );
                    p.setRelationList(relList);
                    pList.add(p);
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
                    relList= this.getRelations(conn, result.getInt("id"));
                    p = getdbPerson(
                            conn,
                            result.getInt("id"),
                            addr,
                            mailList,
                            phoneList
                    );
                    p.setRelationList(relList);
                    pList.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: dbSearch() : " + e.getMessage());
        } finally {
            try{
                if(conn != null){
                    conn.close();;
                }
            } catch (SQLException e){
                System.out.println("Error: dbSearch(); conn.close() : " + e.getMessage());
            }
        }

        return pList;
    }

    /**
     * dbSearch help method
     */
    private boolean isLong(String str){
        try{
            Long.parseLong(str);
        } catch (NumberFormatException e){
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
            System.out.println("Error: getdbPerson() : " + e.getMessage());
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
            System.out.println("Error: getdbAddress() : " + e.getMessage());
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
            System.out.println("Error: getdbPhoneList() : " + e.getMessage());
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
            System.out.println("Error: getdbEmailList() : " + e.getMessage());
        }

        return emailList;
    }

    private ArrayList<String> getRelations(Connection conn, int id){
        String sql = "SELECT person1,person2,relation FROM relationship where person1 = ? or person2 = ?";
        ArrayList<String> relationList = new ArrayList<>();
        String whoSql = "SELECT firstName,lastName FROM person where id=?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setInt(2, id);
            ResultSet result = pstmt.executeQuery();
            String relation ="";
            while (result.next()) {
                //relationList.add(result.getString("email"));
                if(result.getInt("person1") == id){
                    //System.out.println(result.getInt("person1"));
                    PreparedStatement pstmt2 = conn.prepareStatement(whoSql);
                    pstmt2.setInt(1, result.getInt("person2"));

                    ResultSet rs= pstmt2.executeQuery();
                    relation = result.getString("relation");
                    while(rs.next()){
                        relation += " to " + rs.getString("firstName") + " " + rs.getString("lastName");
                    }
                    relationList.add(relation);
                }
            }
        } catch (SQLException e){
            System.out.println("Error: getRelations() : " + e.getMessage());
        }

        return relationList;
    }


    public boolean insertPerson(Person person){
        boolean wasSuccessful = false;
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
            wasSuccessful = true;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Error: insertPerson(); conn.rollback() : " + ex.getMessage());
            }
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Error: insertPerson(); conn.close() : " + e.getMessage());
                }
            }
        }
        return wasSuccessful;
    }

    //changed to boolean for browser output and removed int so that user has to state what each person is with a string ex father/son

    /**
     * @param p1 needs person object that contains personID and relation set from json
     * @param p2 needs person object that contains personID and relation set from json
     * @return true if successful eles false
     */
    public boolean addRelation(Person p1, Person p2){
        boolean wasSuccessful = false;
        Connection conn = null;

        try {
            conn = dbConnect();
            conn.setAutoCommit(false);
            String id1 = selectAnID(conn,"SELECT id FROM person WHERE personID='"+p1.getPersonID()+"'");
            String id2 = selectAnID(conn,"SELECT id FROM person WHERE personID='"+p2.getPersonID()+"'");


            String addRelation = "INSERT INTO relationship(person1,person2,relation) VALUES('"+id1+"','"+id2+"','"+/*typeOfRelation*/p1.getRelation()+"')";
            String addRelation2 = "INSERT INTO relationship(person1,person2,relation) VALUES('"+id2+"','"+id1+"','"+/*typeOfRelation*/p2.getRelation()+"')";

            if(executeInsertSQL(conn,addRelation) && executeInsertSQL(conn, addRelation2)){
                conn.commit();
                wasSuccessful = true;
            }
        } catch (SQLException e) {
            try {
                if(conn != null){
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Error: addRelation(); conn.rollback() : " + ex.getMessage());
            }
        } finally {
            try{
                if(conn != null){
                    conn.close();
                }
            } catch (SQLException e){
                System.out.println("Error: addRelation(); conn.close()" + e.getMessage());
            }
        }
        return wasSuccessful;
    }

    //changed too boolean for browser output if just delete relations

    /**
     *
     * @param person object that must contain a set personID
     * @return true if successful else false
     */
    public boolean deleteRelations(Person person){
        boolean wasSuccessful = false;
        Connection conn = null;
        try {
            conn = dbConnect();
            conn.setAutoCommit(false);
            String id = selectAnID(conn,"SELECT id FROM person WHERE personID='"+person.getPersonID()+"'");
            String deleteRelation = "DELETE FROM relationship WHERE person1='"+id+"' OR person2='"+id+"'";
            if(executeInsertSQL(conn,deleteRelation)) {
                conn.commit();
                wasSuccessful = true;
            }
        } catch (SQLException e){
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex){
                System.out.println("Error: deleteRelation(); conn.rollback()  : " + e.getMessage());
            }
        } finally {
            try {
                if(conn!=null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error: deleteRelation(); conn.close() : " + e.getMessage());
            }

        }
        return wasSuccessful;
    }

    //changed to boolean for browser output..

    /**
     *
     * @param oldPerson object that has to have everything set
     * @param newPerson object that has to have values to update and values not to update set.
     * @return true if successful else false
     */
    public boolean updatePerson(Person oldPerson, Person newPerson){
        boolean wasSuccessful = false;
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

            wasSuccessful = executeInsertSQL(conn,executeUpdateString);


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
            wasSuccessful = true;
        } catch (SQLException e) {
            try {
                if(conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Error: updatePerson(); conn.rollback() : " + e.getMessage());
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error: updatePerson(); conn.close() : " + e.getMessage());
            }
        }
        return wasSuccessful;

    }

    /**
     * Help methods to insertPerson
     */

    private static ArrayList<String> selectAllId(Connection conn, String sql) {
        ArrayList<String> ids = new ArrayList<String>();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ids.add(rs.getString("id"));
            }

        } catch (SQLException e) {
            System.out.println("Error: selectAllId() : " + e.getMessage());
        }

        return ids;
    }

    private static String selectAdressID(Connection conn, String sql) {
        String info = "";

        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                info=rs.getString("adressID");
            }

        } catch (SQLException e) {
            System.out.println("Error: selectAdressID() : " + e.getMessage());
        }

        return info;
    }

    private String selectAnID(Connection conn, String sql) {
        String info = "";
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                info=rs.getString("id");
            }

        } catch (SQLException e) {
            System.out.println("Error: selectAnID() : " + e.getMessage());
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

    private static boolean executeInsertSQL(Connection conn, String sql) throws SQLException {
        boolean autoCommit = false;
        autoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);
        Statement pstmt = conn.createStatement();
        int successful = pstmt.executeUpdate(sql);
        if(successful > 0){
            return true;
        }
        return false;
    }

    /**
     *
     * @param person object with personID set
     * @return true if successful else false
     */
    public boolean deletePerson(Person person) {
        boolean wasSuccess = false;
        deleteRelations(person);

        Connection conn = null;

        try {
            conn = dbConnect();
            String selectPersonSQL = "SELECT id FROM person WHERE personID='"+person.getPersonID()+"'";

            String ID = selectAnID(conn,selectPersonSQL);

            String sqlStatementPhone = "DELETE FROM phone WHERE personID='"+ID+"'";


            String sqlStatementMail = "DELETE FROM email WHERE personID='"+ID+"'";
            String sqlStatementPerson = "DELETE FROM person WHERE id='"+ID+"'";
            conn.setAutoCommit(false);

            if(executeInsertSQL(conn, sqlStatementPhone)
                    && executeInsertSQL(conn, sqlStatementMail)
                    && executeInsertSQL(conn, sqlStatementPerson)) {
                conn.commit();
                wasSuccess = true;
            }

        } catch (SQLException e) {
            try {
                if(conn != null) {
                    conn.rollback();
                    wasSuccess = false;
                }
            } catch (SQLException ex) {
                System.out.println("Error: deletePerson(); conn.rollback() : " + e.getMessage());

            }
            e.printStackTrace();
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Error: deletePerson(); conn.close() : " + e.getMessage());
                }
            }
        }

        return wasSuccess;
    }

    /*public static void main(String[] args) {
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

        d.updatePerson(oldPerson,newPerson);

    }*/

}
