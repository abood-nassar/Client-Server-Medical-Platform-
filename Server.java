
import java.io.*;
import java.net.*;
import java.util.*;

class ClientServing extends Thread {

    private int temp = 0;
    private static boolean username_flag;
    private static ArrayList<Doctor> Doctors = loadDoctors();
    private static List<Ticket> completed_generalTickets = Collections.synchronizedList(new ArrayList<>());
    private static Queue_linkedlist<Ticket> generalTickets = new Queue_linkedlist<>();

    Socket nextClient;

    public ClientServing(Socket nextClient) {
        this.nextClient = nextClient;
    }

    @Override
    public void run() {
        try {
            DataInputStream ddin = new DataInputStream(nextClient.getInputStream());
            DataOutputStream ddout = new DataOutputStream(nextClient.getOutputStream());
            //to check if him doctor or patient
            int choice = ddin.readInt();
            if (choice == 1) {
                // read from client to check if doctor need Login or Register
                int type = ddin.readInt();
                //check if need login or Register if type==1 will be Login // if type == 2 will be Register
                //For Login
                if (type == 1) {
                    String Username = ddin.readUTF();
                    String Password = ddin.readUTF();

                    Doctor doctor = login(Username, Password);
                    if (doctor != null) {
                        ddout.writeUTF("logged in successfully");
                        ddout.flush();
                        int doc_chioce = ddin.readInt(); //selection from main menu
                        boolean doc_flag = true;
                        while (doc_flag) {
                            switch (doc_chioce) {
                                case 1:
                                    while (true) {
                                        ddout.writeUTF(doctor.browseMyTickets());
                                        ddout.flush();
                                        if (doctor.getTicketsSize() == 0) {
                                            break;
                                        }
                                        ddout.writeInt(doctor.getTicketsSize());
                                        ddout.flush();
                                        int tic_to_respond = ddin.readInt();
                                        if (doctor.getTicketsSize() + 1 == tic_to_respond) {
                                            break;
                                        }
                                        String Response = ddin.readUTF();
                                        doctor.respondToTicket(tic_to_respond - 1, Response);
                                    }
                                    break;
                                case 2: // Browse general tickets
                                    while (true) {
                                        synchronized (generalTickets) {
                                            if (generalTickets.isEmpty()) {
                                                ddout.writeUTF("No general tickets available");
                                                ddout.flush();
                                                break;
                                            }

                                            String allTickets = browseGeneralTickets();
                                            ddout.writeUTF(allTickets);
                                            ddout.flush();

                                            ddout.writeInt(generalTickets.size());
                                            if (generalTickets.size() == 0) {
                                                break;
                                            }
                                            int gen_tic_to_respond = ddin.readInt();
                                            if ((gen_tic_to_respond - temp) == generalTickets.size() + 1) {
                                                break;
                                            }

                                            Ticket ticket = generalTickets.readAndRemove((gen_tic_to_respond - temp) - 1);
                                            String genResponse = ddin.readUTF();
                                            ticket.setResponse(genResponse);
                                            ticket.setAssignedDoctor(doctor);
                                            completed_generalTickets.add(ticket);
                                        }
                                    }
                                    break;

                                case 3: // Next ticket
                                    if (doctor.getTicketsSize() == 0) {
                                        ddout.writeUTF("There are no tickets in your queue");
                                        ddout.flush();
                                        break;
                                    }

                                    Ticket nextTicket = doctor.nextTicket();
                                    ddout.writeUTF(nextTicket.getDetails());
                                    ddout.flush();

                                    String nextResponse = ddin.readUTF();
                                    nextTicket.setAssignedDoctor(doctor);
                                    doctor.respondToTicket(nextTicket, nextResponse);

                                    break;

                                case 4: // Log-off
                                    doctor.logout();
                                    doc_flag = false; // Exit the loop
                                    break;
                                default:
                                    break;
                            }

                            if (doc_flag) {
                                doc_chioce = ddin.readInt(); // Prompt for the next choice
                            }
                        }

                    } else if (doctor == null && username_flag == false) {
                        ddout.writeUTF("no user with the entered name");
                        ddout.flush();
                    } else {
                        ddout.writeUTF("wrong password");
                        ddout.flush();
                    }
                } //For Registration
                else if (type == 2) {
                    // Read inputs from the client
                    String name = ddin.readUTF();
                    System.out.println("Server received Name: " + name);

                    String username = ddin.readUTF();
                    System.out.println("Server received Username: " + username);

                    String password = ddin.readUTF();
                    System.out.println("Server received Password: " + password);

                    String message = "Registration approved";
                    ddout.writeUTF(message);
                    ddout.flush();

                    int experience = ddin.readInt();
                    System.out.println("Server received Experience: " + experience);

                    String specialty = ddin.readUTF();
                    System.out.println("Server received Specialty: " + specialty);

                    // Save the doctor details
                    Doctor doctor = new Doctor(name, username, password);
                    doctor.setExperience(experience);
                    doctor.setSpecialty(specialty);
                    registerDoctor(doctor);

                    System.out.println("Registration process completed.");
                }

            } else if (choice == 2) {
                boolean patient_flag = true;
                while (patient_flag) {
                    int patient_choice = ddin.readInt();

                    switch (patient_choice) {
                        case 1: // Browse online doctors
                            int browse_choice = ddin.readInt();
                            ArrayList<Doctor> doctorsList;
                            if (browse_choice == 1) {
                                doctorsList = browseAllOnlineDoctors();
                            } else {
                                String specialty = ddin.readUTF();
                                doctorsList = browseDoctorsBySpecialty(specialty);
                            }
                            ddout.writeInt(doctorsList.size());
                            ddout.flush();
                            if (doctorsList.isEmpty()) {
                                break;
                            }
                            String temp = "";
                            int i = 1;
                            for (Doctor doctor : doctorsList) {

                                temp += i + ")doctor's name: " + doctor.getName() + " -Specialty:(" + doctor.getSpecialty() + ")\n";
                                i++;
                            }
                            ddout.writeUTF(temp);
                            ddout.flush();

                            int doctor_choice = ddin.readInt();
                            if (doctor_choice == doctorsList.size() + 1) {
                                break;
                            }
                            Doctor responseDoctor = doctorsList.get(doctor_choice - 1);

                            int doctorTicketsCount = responseDoctor.getTicketsSize();
                            ddout.writeInt(doctorTicketsCount);
                            ddout.flush();

                            if (doctorTicketsCount >= 5) {
                                break;
                            }

                            String name = ddin.readUTF();
                            int age = ddin.readInt();
                            String question = ddin.readUTF();
                            boolean flag = true;
                            Ticket ticket = new Ticket(name, age, question);

                            responseDoctor.addTikcet(ticket);

                            long startTime = System.currentTimeMillis();
                            while (flag) {

                                long elapsedTime = System.currentTimeMillis() - startTime;
                                if (!responseDoctor.isloggedIn()) {
                                    ddout.writeUTF("Sorry, doctor left before answering you!");
                                    ddout.flush();
                                    responseDoctor.removeTicket(ticket);
                                    break;
                                }
                                if (elapsedTime >= 15000) {
                                    int choice22 = ddin.readInt();
                                    System.out.println("choice from clinet" + choice22);
                                    if (choice22 == 2) {
                                        synchronized (ticket) {
                                            for (int j = 0; j < responseDoctor.getTicketsSize(); j++) {
                                                Ticket ticket223 = ticket; // Get ticket at index i
                                                System.out.println(ticket223.getDetails());
                                                System.out.println(ticket.getDetails());
                                                if (ticket223.getTicketID() == ticket.getTicketID()) {
                                                    responseDoctor.removeTicket(j);  // Removes the ticket at index i
                                                    this.temp++;
                                                    ddout.writeUTF("Your Ticket is Deleted Successfuly");
                                                    ddout.flush();
                                                    flag = false;
                                                    break;

                                                }

                                            }
                                        }
//                                            
                                    } 
                                    break;
                                }

                                for (Ticket ticket1 : responseDoctor.getCompleted_tickets()) {
                                    if (ticket1.getTicketID() == ticket.getTicketID() && !ticket1.getResponse().isEmpty()) {
                                        ddout.writeUTF(ticket1.getResponse());
                                        flag = false;
                                        break;
                                    }
                                }
                            }
                            break;

                        case 2: // Submit a general ticket
                            String name1 = ddin.readUTF();
                            System.out.println(name1);
                            int age1 = ddin.readInt();
                            String question1 = ddin.readUTF();
                            System.out.println(age1);

                            boolean flg = true;
                            Ticket generalTicket = new Ticket(name1, age1, question1);
                            synchronized (generalTickets) {
                                assignGeneralTicket(generalTicket);
                            }
                            long startTime2 = System.currentTimeMillis();
                            while (flg) {

                                long elapsedTime = System.currentTimeMillis() - startTime2;
                                if (elapsedTime >= 15000) {
                                    try {
                                        int clientChoice2 = ddin.readUnsignedByte();
                                        System.out.println("Choice from client: " + clientChoice2);

                                        if (clientChoice2 == 2) {

                                            for (int j = 0; j < generalTickets.size(); j++) {
                                                Ticket ticket22 = generalTickets.get(j); // Get ticket at index j
                                                System.out.println(ticket22.getDetails());
                                                System.out.println(generalTicket.getDetails());

                                                if (ticket22.getTicketID() == generalTicket.getTicketID()) {
                                                    generalTickets.readAndRemove(j); // Removes the ticket at index j
                                                    this.temp++;
                                                    ddout.writeUTF("Your Ticket is Deleted Successfully");
                                                    ddout.flush();
                                                    flg = false;
                                                    break;
                                                }
                                            }

                                        } 
                                    } catch (IOException e) {
                                        System.out.println("Error while handling timeout response: " + e.getMessage());
                                    }
                                    break;
                                }

                                synchronized (completed_generalTickets) {
                                    for (Ticket ticket1 : completed_generalTickets) {
                                        if (ticket1.getTicketID() == generalTicket.getTicketID() && !ticket1.getResponse().isEmpty()) {
                                            try {
                                                ddout.writeUTF(ticket1.getAssignedDoctor().getName());
                                                ddout.writeUTF(ticket1.getAssignedDoctor().getSpecialty());
                                                ddout.writeUTF(ticket1.getResponse());
                                                flg = false;
                                            } catch (IOException e) {
                                                System.out.println("Error sending ticket response: " + e.getMessage());
                                            }
                                            break;
                                        }
                                    }
                                }
                            }

                            break;

                        case 3: // Exit
                            patient_flag = false;
                            break;

                        default:
                            break;
                    }
                }
            }

        } catch (IOException ioe) {
            System.err.println(ioe);
        }

    }

    private static void registerDoctor(Doctor doctor) {
        // Check if username already exists
        for (Doctor existingDoctor : Doctors) {
            if (existingDoctor.getUsername().equals(doctor.getUsername())) {
                System.out.println("Username already exists. Registration failed.");
                return;
            }
        }
        Doctors.add(doctor);
        saveDoctors(); // Save to file
        System.out.println("Doctor registered successfully: " + doctor.getUsername());
    }

    private synchronized static Doctor login(String username, String password) {
        username_flag = false;
        for (Doctor doctor : Doctors) {
            if (doctor.getUsername().equals(username)) {
                username_flag = true;
                break;
            }
        }
        for (Doctor doctor : Doctors) {
            if (doctor.isloggedIn(username, password)) {
                doctor.login();
                return doctor;
            }
        }
        return null;
    }

    private synchronized void assignGeneralTicket(Ticket ticket) {
        generalTickets.enqueue(ticket);

    }

    public static synchronized String browseGeneralTickets() {
        String temp = "";
        int i = 1;
        if (generalTickets.isEmpty()) {
            return "There is no general Tickets";
        }
        for (Ticket ticket : generalTickets) {
            temp += i + ")" + ticket.getDetails() + "\n";
            i++;
        }
        return temp + i + ")return to the doctor’s main menu" + "\n";
    }

    private synchronized static ArrayList<Doctor> browseAllOnlineDoctors() {
        ArrayList<Doctor> onlineDoctors = new ArrayList<>();
        for (Doctor doctor : Doctors) {
            System.out.println("Doctor: " + doctor.getName() + ", Online: " + doctor.isloggedIn());

            if (doctor.isloggedIn()) {
                onlineDoctors.add(doctor);
            }
        }
        return onlineDoctors;
    }

    private synchronized static ArrayList<Doctor> browseDoctorsBySpecialty(String specialty) {
        ArrayList<Doctor> filteredDoctors = new ArrayList<>();
        for (Doctor doctor : Doctors) {
            if (doctor.isloggedIn() && doctor.getSpecialty().equals(specialty)) {
                filteredDoctors.add(doctor);
            }
        }
        return filteredDoctors;
    }
// --- Serialization Methods ---

    private static synchronized void saveDoctors() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("doctors.ser"))) {
            oos.writeObject(Doctors);
        } catch (IOException e) {
            System.err.println("Error saving Doctors: " + e.getMessage());
        }
    }

    private static synchronized ArrayList<Doctor> loadDoctors() {
        ArrayList<Doctor> temp = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("doctors.ser"))) {
            temp = (ArrayList<Doctor>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            if (temp.isEmpty()) {
                System.out.println("No Doctors data found. Initializing new list.");
            } else {
                System.out.println("Doctors list loaded");
            }
        }
        return temp;

    }
}

class Server {

    public static void main(String[] args) {
        try {
            ServerSocket socket = new ServerSocket(1985);
            for (;;) {
                Socket nextclient = socket.accept();
                System.out.println("connection is establshed");
                ClientServing client = new ClientServing(nextclient);
                client.start();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
