package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by alasdair on 07.07.18.
 */
public class PersonalData {
    private String surname;
    private String firstName;
    private String dateOfBirth;
    private String city;
    private String zip;
    private String street;

    public PersonalData() {
    }

    public PersonalData(String surname, String firstName, String dateOfBirth, String city, String zip, String street) {
        this.surname = surname;
        this.firstName = firstName;
        this.dateOfBirth = dateOfBirth;
        this.city = city;
        this.zip = zip;
        this.street = street;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public JSONObject toJSON(){
        JSONObject object = new JSONObject();
        object.put("surname",surname);
        object.put("firstName",firstName);
        object.put("dateOfBirth",dateOfBirth);
        object.put("city",city);
        object.put("zip",zip);
        object.put("street",street);
        return object;
    }

    public void setDataFromJSON(JSONArray array, String adress){
        if (array != null)  {
            for (int block = 0; block < array.size(); block++) {
                JSONObject object = (JSONObject) array.get(block);
                JSONArray transactions = (JSONArray) object.get("transactions");
                for (int t = 0; t < transactions.size(); t++) {
                    JSONObject transaction = (JSONObject) transactions.get(t);
                    if (transaction.get("sender").equals(adress)) {
                        JSONObject data = (JSONObject) transaction.get("data");
                        if (data.get("type").equals("U")){
                            JSONObject personalInfo = (JSONObject) data.get("personalInformation");
                            this.firstName = personalInfo.get("firstName").toString();
                            this.surname = personalInfo.get("surname").toString();
                            this.dateOfBirth = personalInfo.get("dateOfBirth").toString();
                            this.city = personalInfo.get("city").toString();
                            this.zip = personalInfo.get("zip").toString();
                            this.street = personalInfo.get("street").toString();
                        }
                    }

                }
            }
        }
    }

    @Override
    public String toString() {
        return "Nachname: " + surname + "\n" +
                "Vorname: " + firstName + "\n" +
                "Geburtsdatum: " + dateOfBirth + "\n" +
                "Stadt: " + city + "\n" +
                "PLZ: " + zip + "\n" +
                "Straße: " + street + "\n";
    }
}
