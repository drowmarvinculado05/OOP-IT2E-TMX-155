public class Patient extends Person {
    private String patientId;
    private String bloodType;
    private String[] allergies;
    private String emergencyContact;
    private String insuranceProvider;
    
    public Patient(String name, String email, String phone, String address, 
                  String dateOfBirth, String gender, String patientId, 
                  String bloodType, String[] allergies, String emergencyContact, 
                  String insuranceProvider) {
        super(name, email, phone, address, dateOfBirth, gender);
        this.patientId = patientId;
        this.bloodType = bloodType;
        this.allergies = allergies;
        this.emergencyContact = emergencyContact;
        this.insuranceProvider = insuranceProvider;
    }
    
    public String listAllergies() {
        if (allergies.length == 0) {
            return name + " has no known allergies.";
        }
        return name + " has allergies to: " + String.join(", ", allergies);
    }
    
    @Override
    public String displayInfo() {
        return super.displayInfo() + String.format(", Patient ID: %s, Blood Type: %s, Emergency Contact: %s, Insurance: %s", 
                            patientId, bloodType, emergencyContact, insuranceProvider);
    }
}
