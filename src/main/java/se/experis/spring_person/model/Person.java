package se.experis.spring_person.model;

import java.util.ArrayList;

public class Person {
    private String name;
    private String personID;
    private String lastName;
    private ArrayList<String>  phoneIDList;
    private ArrayList<String> emailList;
    private Address address;
    private ArrayList<String> relationList;
    private String relation = "";

    Person(String name, String personID, String lastName, ArrayList<String> phoneIDList, ArrayList<String> emailList, Address address){
        this.name = name;
        this.personID = personID;
        this.lastName = lastName;
        this.phoneIDList = phoneIDList;
        this.emailList = emailList;
        this.address = address;
        this.relationList = new ArrayList<>();
    }

    // only works with console application. or if all person variables has been manually set before we call it
    public void personToString(){
        System.out.print("\nName: " + name + "\nPerson ID: " + personID + "\nSurname: " + lastName + "\nPhone number: " + phoneIDList.get(0).toString());
        for (int i = 1; i < phoneIDList.size(); i++) {
            System.out.print("\n\t\t\t  " + phoneIDList.get(i).toString());
        }
        address.printAddress();
    }

    public String getName(){

        return this.name;
    }
    public String getPersonID()
    {
        return this.personID;
    }
    public String getLastName(){

        return this.lastName;
    }
    public ArrayList<String> getPhoneIDList()
    {
        return this.phoneIDList;
    }

    public Address getAddress()
    {
        return this.address;
    }
    public ArrayList<String> getEmailList(){
    	return this.emailList;
    }




    public void setName(String name) {
        this.name = name;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneIDList(ArrayList<String> phoneIDList) {
        this.phoneIDList = phoneIDList;
    }

    public void setEmailList(ArrayList<String> emailList) {
        this.emailList = emailList;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public ArrayList<String> getRelationList(){
        return this.relationList;
    }

    public void setRelationList(ArrayList<String> relation) {
        this.relationList = relation;
    }
}
