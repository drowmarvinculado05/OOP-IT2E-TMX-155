public class Nurse extends MedicalStaff {
    private String shift;
    private boolean isRegistered;
    
    public Nurse(String name, String email, String phone, String address, 
                String dateOfBirth, String gender, String staffId, 
                String department, String hireDate, String shift, boolean isRegistered) {
        super(name, email, phone, address, dateOfBirth, gender, staffId, department, hireDate);
        this.shift = shift;
        this.isRegistered = isRegistered;
    }
    
    public String assistDoctor(Doctor doctor) {
        return name + " is assisting Dr. " + doctor.name;
    }
    
    public String administerMedication(Patient patient, String medication) {
        return name + " is administering " + medication + " to " + patient.name;
    }
    
    @Override
    public String displayInfo() {
        return super.displayInfo() + String.format(", Shift: %s, Registered: %s", 
                            shift, isRegistered);
    }
}
