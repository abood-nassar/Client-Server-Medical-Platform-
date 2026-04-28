
import java.io.Serializable;


class Ticket implements Serializable {

    private static int idCounter = 0;
    private int ticketID;
    private String patientName;
    private int patientAge;
    private String question;
    private Doctor assignedDoctor;
    private String response;

    public Ticket(String patientName, int patientAge, String question) {
        this.ticketID = idCounter++;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.question = question;
    }

    public String getDetails() {
        return "Ticket ID: " + ticketID + ", Patient: " + patientName + ", Age: " + patientAge + ", Question: " + question;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public boolean getResponsedto() {
        return response != null;
    }

    public void setAssignedDoctor(Doctor doctor) {
        this.assignedDoctor = doctor;
    }

    public Doctor getAssignedDoctor() {
        return this.assignedDoctor;
    }

    public int getTicketID() {
        return ticketID;
    }
    
}
