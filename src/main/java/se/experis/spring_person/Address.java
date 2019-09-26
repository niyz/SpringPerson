package se.experis;

public class Address {
    private String country;
    private String city;
    private String street;
    private String houseNum;
    private String postalCode;

    Address(String country, String city, String street, String houseNum, String postalCode) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseNum = houseNum;
        this.postalCode = postalCode;
    }

    public void printAddress() {
        System.out.println("\nCountry: " + country + "\nCity: " + city + "\nStreet: " + street +
                "\nStreet number: " + houseNum + "\nPostal code: " + postalCode);
    }

    public String getCountry() {
    	return this.country;
    }
    public String getCity() {
    	return this.city;
    }
    public String getStreet() {
    	return this.street;
    }
    public String getStreetNum() {
    	return this.houseNum;
    }
    public String getPostalCode() {
    	return this.postalCode;
    }


}
