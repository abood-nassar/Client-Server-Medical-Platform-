import java.util.*;

// Patient Class
class Patient {
    private String name;
    private int age;

    public Patient(String name, int age) {
        this.name = name;
        this.age = age;
    }


    public void submitTicket(Ticket ticket) {
        // Logic to submit ticket to a doctor
    }

    public List<Doctor> browseDoctors(String specialty) {
        // Logic to browse doctors by specialty
        return new ArrayList<>();
    }

    public void submitGeneralTicket(Ticket ticket) {
        // Logic to submit general ticket
    }

    public void withdrawTicket(Ticket ticket) {
        // Logic to withdraw a ticket
    }
}
