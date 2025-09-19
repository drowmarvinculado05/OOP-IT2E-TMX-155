public class Appointment {
    protected String appointmentId;
    protected Patient patient;
    protected Doctor doctor;
    protected String date;
    protected String time;
    
    public Appointment(String appointmentId, Patient patient, Doctor doctor, 
                      String date, String time) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
    }
    
    public String displayAppointment() {
        return String.format("Appointment %s: %s with Dr. %s on %s at %s", 
                            appointmentId, patient.name, doctor.name, date, time);
    }
}
