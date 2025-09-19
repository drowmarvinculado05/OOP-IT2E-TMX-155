public class Person {
    protected String name;
    protected String email;
    protected String phone;
    protected String address;
    protected String dateOfBirth;
    protected String gender;
    
    public Person(String name, String email, String phone, String address, 
                 String dateOfBirth, String gender) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }
    
    public String displayInfo() {
        return String.format("%s: %s, %s, %s, DOB: %s, Gender: %s", 
                            name, email, phone, address, dateOfBirth, gender);
    }
    
    public String contact() {
        return "Contacting " + name + " at " + email + " or " + phone;
    }
}
