package utils;

import models.User;
import models.Reminder;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.time.LocalDate;

public class DatabaseHelper {
    // Simulasi database user
    private static final Map<String, User> userTable = new HashMap<>(); // key: email
    // Simulasi database reminder
    private static final Map<Integer, Reminder> reminderTable = new HashMap<>(); // key: id
    private static final AtomicInteger reminderIdCounter = new AtomicInteger(1);
    private static final String USER_XML_PATH = "users.xml";

    // USER CRUD
    public static void addUser(User user) {
        userTable.put(user.getEmail(), user);
    }

    public static User getUser(String email) {
        return userTable.get(email);
    }

    public static boolean userExists(String email) {
        return userTable.containsKey(email);
    }

    public static void removeUser(String email) {
        userTable.remove(email);
    }

    public static List<User> getAllUsers() {
        return new ArrayList<>(userTable.values());
    }

    public static void saveUsersToXML() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("users");
            doc.appendChild(rootElement);
            for (User user : userTable.values()) {
                Element userElement = doc.createElement("user");
                rootElement.appendChild(userElement);

                Element nama = doc.createElement("nama");
                nama.appendChild(doc.createTextNode(user.getNama()));
                userElement.appendChild(nama);

                Element email = doc.createElement("email");
                email.appendChild(doc.createTextNode(user.getEmail()));
                userElement.appendChild(email);

                Element password = doc.createElement("password");
                password.appendChild(doc.createTextNode(user.getPassword()));
                userElement.appendChild(password);

                Element tanggalLahir = doc.createElement("tanggalLahir");
                tanggalLahir.appendChild(doc.createTextNode(user.getTanggalLahir() != null ? user.getTanggalLahir().toString() : ""));
                userElement.appendChild(tanggalLahir);

                Element nomorHp = doc.createElement("nomorHp");
                nomorHp.appendChild(doc.createTextNode(user.getNomorHp()));
                userElement.appendChild(nomorHp);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(USER_XML_PATH));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void loadUsersFromXML() {
        try {
            File xmlFile = new File(USER_XML_PATH);
            if (!xmlFile.exists()) return;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("user");
            for (int i = 0; i < nList.getLength(); i++) {
                org.w3c.dom.Node nNode = nList.item(i);
                if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String nama = eElement.getElementsByTagName("nama").item(0).getTextContent();
                    String email = eElement.getElementsByTagName("email").item(0).getTextContent();
                    String password = eElement.getElementsByTagName("password").item(0).getTextContent();
                    String tanggalLahirStr = eElement.getElementsByTagName("tanggalLahir").item(0).getTextContent();
                    LocalDate tanggalLahir = tanggalLahirStr.isEmpty() ? null : LocalDate.parse(tanggalLahirStr);
                    String nomorHp = eElement.getElementsByTagName("nomorHp").item(0).getTextContent();
                    // Data tambahan dummy
                    String kodeNegara = "+62";
                    String bio = "";
                    String jenisKelamin = "Laki-laki";
                    User user = new User(email, password, nama, tanggalLahir, nomorHp, kodeNegara, bio, jenisKelamin);
                    userTable.put(email, user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // REMINDER CRUD
    public static Reminder addReminder(Reminder reminder) {
        int id = reminderIdCounter.getAndIncrement();
        reminder.setId(id);
        reminderTable.put(id, reminder);
        return reminder;
    }

    public static Reminder getReminder(int id) {
        return reminderTable.get(id);
    }

    public static void removeReminder(int id) {
        reminderTable.remove(id);
    }

    public static List<Reminder> getAllReminders() {
        return new ArrayList<>(reminderTable.values());
    }

    public static List<Reminder> getRemindersByStatus(String status) {
        List<Reminder> result = new ArrayList<>();
        for (Reminder r : reminderTable.values()) {
            if (r.getStatus().equalsIgnoreCase(status)) {
                result.add(r);
            }
        }
        return result;
    }
}
