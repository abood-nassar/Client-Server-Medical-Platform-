
import java.io.Serializable;
import java.util.*;

class Doctor implements User, Serializable {

    private String name;
    private String username;
    private String password;
    private String specialty;
    private int experience;
    private Queue_linkedlist<Ticket> tickets = new Queue_linkedlist<>();
    private List<Ticket> completed_tickets = Collections.synchronizedList(new ArrayList<>());
    private boolean online = false;

    public Doctor(String name, String username, String password, String specialty, int experience) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.specialty = specialty;
        this.experience = experience;
    }

    public Doctor(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;

    }

    @Override
    public void login() {
        this.online = true;
    }

    @Override
    public boolean isloggedIn() {
        return online;
    }

    @Override
    public boolean isloggedIn(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public String browseMyTickets() {
        String temp = "";
        int i = 1;
        if (tickets.isEmpty()) {
            return "There is no Tickets for this doctor";
        }
        for (Ticket ticket : tickets) {
            if (i > 5) {
                break;
            }
            temp += i + ")" + ticket.getDetails() + "\n";
            i++;
        }

        return temp + i + ")return to the doctor’s main menu" + "\n";
    }

    public Ticket nextTicket() {
        return tickets.dequeue();
    }

    public void removeTicket(int i) {
        tickets.readAndRemove(i);
    }

    public void respondToTicket(int ticket_num, String response) {
        Ticket temp = tickets.readAndRemove(ticket_num);
        temp.setResponse(response);
        completed_tickets.add(temp);
    }

    public void respondToTicket(Ticket ticket, String response) {
        ticket.setResponse(response);
        completed_tickets.add(ticket);
    }

    public void logout() {
        this.online = false;
    }

    public void addCompletedTikcets(Ticket ticket) {
        completed_tickets.add(ticket);

    }

    public void addTikcet(Ticket ticket) {
        tickets.enqueue(ticket);
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getName() {
        return name;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public int getExperience() {
        return experience;
    }

    public int getTicketsSize() {
        return tickets.size();
    }

    public String getUsername() {
        return username;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public List<Ticket> getCompleted_tickets() {
        List<Ticket> temp = new ArrayList<>();
        for (Ticket ticket : completed_tickets) {
            temp.add(ticket);
        }
        return temp;
    }

    public void removeTicket(Ticket ticket) {
        int i=0;
        for (Ticket ticket1 : tickets) {
            if(ticket.getTicketID()==ticket1.getTicketID()){
                this.tickets.readAndRemove(i);
            }
            i++;
        }
        
    }
}
