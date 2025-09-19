public class TreatmentPlan extends MedicalRecord {
    private String[] medications;
    private String[] procedures;
    private String followUpDate;
    
    public TreatmentPlan(String recordId, Patient patient, Doctor doctor, 
                        String date, String[] medications, String[] procedures, 
                        String followUpDate) {
        super(recordId, patient, doctor, date);
        this.medications = medications;
        this.procedures = procedures;
        this.followUpDate = followUpDate;
    }
    
    @Override
    public String displayRecord() {
        return super.displayRecord() + String.format("\nMedications: %s\nProcedures: %s\nFollow-up: %s", 
                            String.join(", ", medications), 
                            String.join(", ", procedures), 
                            followUpDate);
    }
}
