public class Consultation extends Appointment {
    private String reason;
    private int duration;
    
    public Consultation(String appointmentId, Patient patient, Doctor doctor, 
                       String date, String time, String reason, int duration) {
        super(appointmentId, patient, doctor, date, time);
        this.reason = reason;
        this.duration = duration;
    }
    
    @Override
    public String displayAppointment() {
        return super.displayAppointment() + String.format("\nReason: %s, Duration: %d minutes", 
                            reason, duration);
    }
}
