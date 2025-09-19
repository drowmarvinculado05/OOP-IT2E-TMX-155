public class MedicalRecord {
    protected String recordId;
    protected Patient patient;
    protected Doctor doctor;
    protected String date;
    
    public MedicalRecord(String recordId, Patient patient, Doctor doctor, String date) {
        this.recordId = recordId;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
    }
    
    public String displayRecord() {
        return String.format("Record %s for %s by Dr. %s on %s", 
                            recordId, patient.name, doctor.name, date);
    }
}
