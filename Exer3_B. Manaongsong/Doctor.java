public class Doctor extends MedicalStaff {
    private String specialization;
    private String licenseNumber;
    private int yearsExperience;
    
    public Doctor(String name, String email, String phone, String address, 
                 String dateOfBirth, String gender, String staffId, 
                 String department, String hireDate, String specialization, 
                 String licenseNumber, int yearsExperience) {
        super(name, email, phone, address, dateOfBirth, gender, staffId, department, hireDate);
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.yearsExperience = yearsExperience;
    }
    
    public String examinePatient(Patient patient) {
        return "Dr. " + name + " is examining " + patient.name;
    }
    
    public String prescribeMedication(Patient patient, String medication) {
        return "Dr. " + name + " is prescribing " + medication + " to " + patient.name;
    }
    
    @Override
    public String displayInfo() {
        return super.displayInfo() + String.format(", Specialization: %s, License: %s, Experience: %d years", 
                            specialization, licenseNumber, yearsExperience);
    }
}
