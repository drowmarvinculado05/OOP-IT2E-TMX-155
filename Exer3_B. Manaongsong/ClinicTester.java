public class ClinicTester {
    public static void main(String[] args) {
        // Create medical staff
        Doctor doctor = new Doctor("Ronie Manaongsong", "ronie@humanclinic.com", 
                                  "555-1234", "123 Oak St", "1980-05-15", "Male", 
                                  "DOC001", "Cardiology", "2010-03-20", 
                                  "Cardiologist", "MD12345", 15);
        
        Nurse nurse = new Nurse("Dec Bucong", "dec@humanclinic.com", 
                               "555-5678", "456 Pine St", "1985-08-22", "Male", 
                               "NUR001", "Cardiology", "2015-06-10", 
                               "Day", true);
        
        // Create patients
        String[] johnAllergies = {"Penicillin", "Shellfish"};
        Patient patient1 = new Patient("Jonny Sins", "Jonny sins@email.com", "555-9012", 
                                      "789 Elm St", "1975-12-10", "Male", 
                                      "PAT001", "O+", johnAllergies, 
                                      "Jane Doe (555-0000)", "Blue Cross");
        
        String[] maryAllergies = {};
        Patient patient2 = new Patient("Mia eyy", "miaeyy@email.com", "555-3456", 
                                      "321 Maple St", "1988-07-03", "Female", 
                                      "PAT002", "A-", maryAllergies, 
                                      "John Smith (555-1111)", "Aetna");
        
        // Create medical records
        String[] johnSymptoms = {"Chest pain", "Shortness of breath"};
        Diagnosis diagnosis = new Diagnosis("REC001", patient1, doctor, 
                                           "2025-10-15", "Angina", johnSymptoms, "Moderate");
        
        String[] medications = {"Aspirin", "Nitroglycerin"};
        String[] procedures = {"Stress test", "Echocardiogram"};
        TreatmentPlan treatment = new TreatmentPlan("REC002", patient1, doctor, 
                                                   "2025-10-15", medications, procedures, 
                                                   "2025-11-15");
        
        // Create appointments
        Consultation consultation = new Consultation("APT001", patient1, doctor, 
                                                    "2023-10-20", "10:00 AM", 
                                                    "Follow-up for angina", 30);
        
        Procedure procedure = new Procedure("APT002", patient2, doctor, 
                                           "2025-10-25", "2:00 PM", 
                                           "Cardiac catheterization", true, 120);
        
        // Display information
        System.out.println("=== HUMAN CLINIC SYSTEM ===");
        
        System.out.println("\n=== MEDICAL STAFF ===");
        System.out.println(doctor.displayInfo());
        System.out.println(doctor.examinePatient(patient1));
        System.out.println(doctor.prescribeMedication(patient1, "Aspirin"));
        
        System.out.println("\n" + nurse.displayInfo());
        System.out.println(nurse.assistDoctor(doctor));
        System.out.println(nurse.administerMedication(patient1, "Nitroglycerin"));
        
        System.out.println("\n=== PATIENTS ===");
        System.out.println(patient1.displayInfo());
        System.out.println(patient1.listAllergies());
        
        System.out.println("\n" + patient2.displayInfo());
        System.out.println(patient2.listAllergies());
        
        System.out.println("\n=== MEDICAL RECORDS ===");
        System.out.println(diagnosis.displayRecord());
        
        System.out.println("\n" + treatment.displayRecord());
        
        System.out.println("\n=== APPOINTMENTS ===");
        System.out.println(consultation.displayAppointment());
        
        System.out.println("\n" + procedure.displayAppointment());
        
        System.out.println("\n=== ALL CLASSES INSTANTIATED SUCCESSFULLY ===");
    }
}
