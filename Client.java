
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    public static void main(String[] args) {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Wellcome to client/server medical platform");
            while (true) {
                Socket socket = new Socket(InetAddress.getLoopbackAddress(), 1985);
                DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                DataInputStream ddin = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                System.out.println("Are you patient or doctor : 1) Doctor / 2) Patient");
                int choice = input.nextInt();
                input.nextLine();
                dout.writeInt(choice);
                dout.flush();
                if (choice == 1) { //he's a doctor
                    System.out.println("what you need login or register : 1)Login / 2)Register");
                    int type = input.nextInt();
                    input.nextLine();
                    dout.writeInt(type);
                    dout.flush();
                    if (type == 1) {
                        System.out.println("Welcome To Login Page");
                        System.out.println("Enter your Username:");
                        //read username from doctor
                        String Username = input.nextLine();
                        //send username to server
                        dout.writeUTF(Username);
                        dout.flush();
                        // read Password from doctor
                        System.out.println("Enter your Password:");

                        String Password = input.nextLine();
                        // send password to server
                        dout.writeUTF(Password);
                        dout.flush();

                        String login_massege = ddin.readUTF();
                        boolean doc_flag = true;
                        System.out.println(login_massege);
                        if (login_massege.equals("logged in successfully")) {
                            while (doc_flag) {
                                System.out.println("Select one of the follwing options:");
                                System.out.println("1)Browse my tickets");
                                System.out.println("2)Browse general tickets");
                                System.out.println("3)Next ticket");
                                System.out.println("4)Log-off");
                                int log_chioce = input.nextInt();// read from console
                                input.nextLine();
                                if (log_chioce >= 1 && log_chioce <= 4) {
                                    dout.writeInt(log_chioce);
                                    dout.flush();
                                    switch (log_chioce) {
                                        case 1:
                                            boolean fg = true;
                                            while (fg) {
                                                String doc_tic = ddin.readUTF();
                                                System.out.println(doc_tic); // doctor's ticket
                                                if (doc_tic.equals("There is no Tickets for this doctor")) {
                                                    System.out.println("There is no Tickets for you doctor,we will take you back to the main menu");
                                                    break;
                                                }
                                                int doc_ticsize = ddin.readInt(); //number of tickets
                                                System.out.println("Select a Ticket or return to the main menu:");
                                                int doc_chioce = input.nextInt(); // read from console
                                                input.nextLine();
                                                while (true) {
                                                    if (doc_chioce >= 1 && doc_chioce <= doc_ticsize + 1) {
                                                        dout.writeInt(doc_chioce); //send ticket number
                                                        dout.flush();
                                                        if (doc_chioce == doc_ticsize + 1) {
                                                            fg = false;
                                                            break;
                                                        }
                                                        System.out.println("Enter your Response:");
                                                        dout.writeUTF(input.nextLine());
                                                        dout.flush();
                                                        break;
                                                    } else {
                                                        System.out.println("Invalid choice. Please select a number between 1 and " + (doc_ticsize + 1));
                                                        doc_chioce = input.nextInt(); // read from console
                                                        input.nextLine();
                                                    }
                                                }
                                            }
                                            break;
                                        case 2: // Browse General Tickets
                                            boolean flg = true;
                                            while (flg) {
                                                String generalTickets = ddin.readUTF();
                                                System.out.println(generalTickets);
                                                if (generalTickets.equals("No general tickets available")) {
                                                    System.out.println("There is no general Tickets,we will take you back to the main menu");
                                                    break;
                                                }
                                                int genTicketSize = ddin.readInt();

                                                if (genTicketSize == 0) {
                                                    System.out.println("There is no general Tickets,we will take you back to the main menu");
                                                    break;
                                                }
                                                System.out.println("Select a general Ticket or return to the main menu:");
                                                int genTicketChoice = input.nextInt();
                                                input.nextLine();
                                                while (true) {

                                                    if (genTicketChoice >= 1 && genTicketChoice <= genTicketSize + 1) {
                                                        dout.writeInt(genTicketChoice);
                                                        dout.flush();
                                                        if (genTicketChoice == genTicketSize + 1) {
                                                            flg = false;
                                                            break;
                                                        }

                                                        System.out.println("Enter your Response:");
                                                        String response = input.nextLine();
                                                        dout.writeUTF(response);
                                                        dout.flush();
                                                        break;
                                                    } else {
                                                        System.out.println("Invalid choice. Please select a number between 1 and " + (genTicketSize + 1));
                                                        genTicketChoice = input.nextInt();
                                                        input.nextLine();
                                                    }
                                                }
                                            }

                                            break;

                                        case 3: // Next Ticket
                                            String nextTicket = ddin.readUTF();
                                            System.out.println(nextTicket);

                                            if (!nextTicket.equals("There are no tickets in your queue")) {
                                                System.out.println("Enter your Response:");
                                                String nextResponse = input.nextLine();
                                                dout.writeUTF(nextResponse);
                                                dout.flush();
                                            } else {
                                                System.out.println("There are no tickets in your queue, we will take you back to the main menu");
                                            }
                                            break;

                                        case 4: // Log-off
                                            System.out.println("Logged off...");
                                            doc_flag = false;
                                            break;

                                        default:
                                            break;
                                    }

                                } else {
                                    System.out.println("Invalid choice. Please select a number between 1 and 4");
                                }
                            }

                        }

                    } // For Registration Doctor
                    else if (type == 2) {
                        System.out.println("Welcome To Registration Page");
                        System.out.println("Enter Your Name");
                        // read name of doctor
                        String name = input.nextLine();
                        //send name of doctor to server
                        dout.writeUTF(name);
                        dout.flush();

                        System.out.println("Enter Your Username");
                        // read name of doctor
                        String Username = input.nextLine();
                        //send name of doctor to server
                        dout.writeUTF(Username);
                        dout.flush();

                        System.out.println("Enter Your Password");
                        // read Passowrd of doctor
                        String Password = input.nextLine();
                        //send Password of doctor to server
                        dout.writeUTF(Password);
                        dout.flush();
                        String ms = ddin.readUTF();
                        System.out.println(ms);
                        if (ms.equals("Registration approved")) {
                            System.out.println("Enter Your Experiance");
                            //Read From Docctor 
                            int Experiance = input.nextInt();
                            input.nextLine();

                            //Send Experince Of Doctor To Server
                            dout.writeInt(Experiance);
                            dout.flush();

                            boolean flag = true;
                            String specialty = "";
                            while (flag) {
                                System.out.println("Select Your specialty which must be one of the following: ");
                                System.out.println("1) Dermatology - طب الجلد");
                                System.out.println("2) Cardiology - طب القلب");
                                System.out.println("3) Gynecology and Obstetrics - طب التوليد وأمراض النساء");
                                System.out.println("4) Neurology - طب الأعصاب");
                                System.out.println("5) Family Medicine - طب العائلة");
                                System.out.println("6) Gastroenterology - طب الجهاز الهضمي");
                                System.out.println("7) Ophthalmology - طب العيون");
                                System.out.println("8) Pediatrics - طب الأطفال");
                                System.out.println("9) Orthopedic - طب العظام");
                                System.out.println("10) Nephrology - طب الكلى");
                                System.out.println("11) Hematology - أمراض الدم");
                                System.out.println("12) General Surgery - الجراحة العامة");
                                System.out.println("13) Endocrinology - الغدد الصماء");
                                System.out.println("14) Medical Laboratory Scientist (MLS) - تحاليل طبية");
                                int choice_spec = input.nextInt();
                                input.nextLine();
                                switch (choice_spec) {
                                    case 1:
                                        flag = false;
                                        specialty = "Dermatology";
                                        break;
                                    case 2:
                                        flag = false;
                                        specialty = "Cardiology";
                                        break;
                                    case 3:
                                        flag = false;
                                        specialty = "Gynecology and Obstetrics";
                                        break;
                                    case 4:
                                        flag = false;
                                        specialty = "Neurology";
                                        break;
                                    case 5:
                                        flag = false;
                                        specialty = "Family Medicine";
                                        break;
                                    case 6:
                                        flag = false;
                                        specialty = "Gastroenterology";
                                        break;
                                    case 7:
                                        flag = false;
                                        specialty = "Ophthalmology";
                                        break;
                                    case 8:
                                        flag = false;
                                        specialty = "Pediatrics";
                                        break;
                                    case 9:
                                        flag = false;
                                        specialty = "Orthopedic";
                                        break;
                                    case 10:
                                        flag = false;
                                        specialty = "Nephrology";
                                        break;
                                    case 11:
                                        flag = false;
                                        specialty = "Hematology";
                                        break;
                                    case 12:
                                        flag = false;
                                        specialty = "General Surgery";
                                        break;
                                    case 13:
                                        flag = false;
                                        specialty = "Endocrinology";
                                        break;
                                    case 14:
                                        flag = false;
                                        specialty = "Medical Laboratory Scientist (MLS)";
                                        break;
                                    default:
                                        System.out.println("Invalid choice. Please select a number between 1 and 14");
                                        // Optionally, you can prompt the user to enter the choice again or handle the error as needed
                                        break;
                                }
                            }
                            //Send Experince Of Doctor To Server
                            dout.writeUTF(specialty);
                            dout.flush();
                        } else {
                            System.out.println("registration was rejected");
                        }

                    }
                } else if (choice == 2) {
                    boolean patient_flag = true;
                    while (patient_flag) {
                        System.out.println("Patient's Main Menu:");
                        System.out.println("1) Browse online doctors");
                        System.out.println("2) Submit a general ticket");
                        System.out.println("3) Exit");
                        int patient_choice = input.nextInt();
                        input.nextLine();
                        dout.writeInt(patient_choice);
                        dout.flush();

                        switch (patient_choice) {
                            case 1: // Browse online doctors
                                System.out.println("Do you want to show:\n(1) All doctors \n(2) Doctors by specialty");
                                int browse_choice = input.nextInt();
                                input.nextLine();
                                dout.writeInt(browse_choice);
                                dout.flush();

                                if (browse_choice == 2) {
                                    boolean flag = true;
                                    String specialty = "";
                                    while (flag) {
                                        System.out.println("Select the specialty of the doctors you want to show which must be one of the following: ");
                                        System.out.println("1) Dermatology - طب الجلد");
                                        System.out.println("2) Cardiology - طب القلب");
                                        System.out.println("3) Gynecology and Obstetrics - طب التوليد وأمراض النساء");
                                        System.out.println("4) Neurology - طب الأعصاب");
                                        System.out.println("5) Family Medicine - طب العائلة");
                                        System.out.println("6) Gastroenterology - طب الجهاز الهضمي");
                                        System.out.println("7) Ophthalmology - طب العيون");
                                        System.out.println("8) Pediatrics - طب الأطفال");
                                        System.out.println("9) Orthopedic - طب العظام");
                                        System.out.println("10) Nephrology - طب الكلى");
                                        System.out.println("11) Hematology - أمراض الدم");
                                        System.out.println("12) General Surgery - الجراحة العامة");
                                        System.out.println("13) Endocrinology - الغدد الصماء");
                                        System.out.println("14) Medical Laboratory Scientist (MLS) - تحاليل طبية");
                                        int choice_spec = input.nextInt();
                                        input.nextLine();
                                        switch (choice_spec) {
                                            case 1:
                                                flag = false;
                                                specialty = "Dermatology";
                                                break;
                                            case 2:
                                                flag = false;
                                                specialty = "Cardiology";
                                                break;
                                            case 3:
                                                flag = false;
                                                specialty = "Gynecology and Obstetrics";
                                                break;
                                            case 4:
                                                flag = false;
                                                specialty = "Neurology";
                                                break;
                                            case 5:
                                                flag = false;
                                                specialty = "Family Medicine";
                                                break;
                                            case 6:
                                                flag = false;
                                                specialty = "Gastroenterology";
                                                break;
                                            case 7:
                                                flag = false;
                                                specialty = "Ophthalmology";
                                                break;
                                            case 8:
                                                flag = false;
                                                specialty = "Pediatrics";
                                                break;
                                            case 9:
                                                flag = false;
                                                specialty = "Orthopedic";
                                                break;
                                            case 10:
                                                flag = false;
                                                specialty = "Nephrology";
                                                break;
                                            case 11:
                                                flag = false;
                                                specialty = "Hematology";
                                                break;
                                            case 12:
                                                flag = false;
                                                specialty = "General Surgery";
                                                break;
                                            case 13:
                                                flag = false;
                                                specialty = "Endocrinology";
                                                break;
                                            case 14:
                                                flag = false;
                                                specialty = "Medical Laboratory Scientist (MLS)";
                                                break;
                                            default:
                                                System.out.println("Invalid choice. Please select a number between 1 and 14");
                                                break;
                                        }
                                    }
                                    dout.writeUTF(specialty);
                                    dout.flush();
                                }

                                boolean flag = true;
                                while (flag) {
                                    int doctorsList_size = ddin.readInt(); // Receive doctors list
                                    if (doctorsList_size == 0) {
                                        System.out.println("There is no doctors available,we will take you back to the main menu");
                                        break;
                                    }
                                    String doctorsList = ddin.readUTF(); // Receive doctors list
                                    System.out.println("Enter doctor number to submit a ticket or " + (doctorsList_size + 1) + ") return to menu: ");
                                    System.out.println("Doctors:\n" + doctorsList);
                                    int doctorChoice = input.nextInt();
                                    input.nextLine();
                                    dout.writeInt(doctorChoice);
                                    dout.flush();

                                    if (doctorChoice >= 1 && doctorChoice <= doctorsList_size) {
                                        int doctorTicketsCount = ddin.readInt();
                                        if (doctorTicketsCount >= 5) {
                                            System.out.print("Sorry this doctor has reatched max number of ticket");
                                            break;
                                        }
                                        System.out.print("Enter your name: ");
                                        String name = input.nextLine();
                                        System.out.print("Enter your age: ");
                                        int age = input.nextInt();
                                        input.nextLine();
                                        System.out.print("Enter your question/consultation: ");
                                        String question = input.nextLine();

                                        dout.writeUTF(name);
                                        dout.flush();

                                        dout.writeInt(age);
                                        dout.flush();

                                        dout.writeUTF(question);
                                        dout.flush();
                                        boolean flg = true;
                                        System.out.println("Waiting for doctor's response...");
                                        while (flg) {

                                            try {
                                                socket.setSoTimeout(15000);
                                                String response = ddin.readUTF();
                                                if (response.equals("Sorry, doctor left before answering you!")) {
                                                    System.out.println(response);
                                                    flag = false;
                                                    break; // Exit to the main menu
                                                }
                                                if (response == null || response.isEmpty()) {
                                                    System.out.println("Error: Received an invalid response from the server.");
                                                } else {
                                                    System.out.println("Response received:");
                                                    System.out.println("Doctor's response: " + response);
                                                    flag = false;
                                                    break;
                                                }

                                            } catch (InterruptedIOException in) {
                                                boolean fla = true;
                                                System.out.println("no response is received for a ticket submitted, do you want to 1)wait or 2)withdraw the ticket?");
                                                while (fla) {
                                                    int res = input.nextInt();
                                                    switch (res) {
                                                        case 1:
                                                            fla = false;
                                                            System.out.println("Waiting for doctor's response...");
                                                            break;
                                                        case 2:
                                                            fla = false;
                                                            flg = false;
                                                            flag = false;
                                                            dout.writeInt(2);
                                                            dout.flush();
                                                            String resfromserver = ddin.readUTF();
                                                            System.out.println(resfromserver);
                                                            break;
                                                        default:
                                                            System.out.println("Invalid choice,Please enter 1 or 2");
                                                    }
                                                }

                                            }
                                        }

                                    } else if (doctorsList_size + 1 == doctorChoice) {
                                        break;
                                    }
                                }

                                break;

                            case 2: // Submit a general ticket
                                System.out.print("Enter your name: ");
                                String name = input.nextLine();
                                System.out.print("Enter your age: ");
                                int age = input.nextInt();
                                System.out.print("Enter your question/consultation: ");
                                input.nextLine();
                                String question = input.nextLine();

                                dout.writeUTF(name);
                                dout.flush();

                                dout.writeInt(age);
                                dout.flush();

                                dout.writeUTF(question);
                                dout.flush();

                                System.out.println("Waiting for a doctor's response...");
                                boolean flg = true;
                                while (flg) {
                                    try {
                                        socket.setSoTimeout(15000);
                                        String doctorName = ddin.readUTF();
                                        String Specialty = ddin.readUTF();
                                        String response = ddin.readUTF();
                                        if (response == null || response.isEmpty()) {
                                            System.out.println("Error: Received an invalid response from the server.");
                                        } else {
                                            String genResponse = "Doctor: Dr." + doctorName + ", Specialty: " + Specialty + ", " + "Response: " + response;
                                            System.out.println("Response received:");
                                            System.out.println(genResponse);
                                            break;
                                        }

                                    } catch (InterruptedIOException in) {
                                        boolean fla = true;
                                        System.out.println("no response is received for a ticket submitted, do you want to 1)wait or 2)withdraw the ticket?");
                                        while (fla) {

                                            int res = input.nextInt();
                                            switch (res) {
                                                case 1:
                                                    fla = false;
                                                    System.out.println("Waiting for doctor's response...");
                                                    break;
                                                case 2:
                                                    dout.write(2);
                                                    dout.flush();
                                                    System.out.println("deleteing your response!");
                                                    String from_server = ddin.readUTF();
                                                    System.out.println(from_server);
                                                    fla = false;
                                                    flg = false;
                                                    break;

                                                default:
                                                    System.out.println("Invalid choice,Please enter 1 or 2");
                                            }
                                        }

                                    }
                                }
                                break;

                            case 3: // Exit
                                System.out.println("Exiting...");
                                patient_flag = false;
                                break;

                            default:
                                System.out.println("Invalid choice. Please try again with nubmers from 1 to 3.");
                                break;
                        }
                    }
                }

            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
