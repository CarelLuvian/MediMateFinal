package utils;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import models.User;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class UserStore {

    private static HashMap<String, String> users = new HashMap<>();

    static {
        // Inisialisasi: Load data dari XML ke HashMap saat kelas pertama kali dipanggil
        List<User> userList = getAllUsers();
        for (User u : userList) {
            users.put(u.getEmail(), u.getPassword());
        }
    }

    public static void addUser(String email, String password) {
        users.put(email, password);
    }

    public static boolean userExists(String email) {
        return users.containsKey(email);
    }

    public static boolean validateUser(String email, String password) {
        return users.containsKey(email) && users.get(email).equals(password);
    }

    public static void printAllUsers() {
        System.out.println("== Daftar Pengguna ==");
        for (String email : users.keySet()) {
            System.out.println(email + " : " + users.get(email));
        }
    }

    // Membaca semua user dari users.xml
    public static List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try {
            File xmlFile = new File("users.xml");
            if (!xmlFile.exists()) return userList;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            NodeList userNodes = doc.getElementsByTagName("user");
            for (int i = 0; i < userNodes.getLength(); i++) {
                Element userElem = (Element) userNodes.item(i);
                String nama = userElem.getElementsByTagName("nama").item(0).getTextContent();
                String email = userElem.getElementsByTagName("email").item(0).getTextContent();
                String password = userElem.getElementsByTagName("password").item(0).getTextContent();
                String tanggalLahir = userElem.getElementsByTagName("tanggalLahir").item(0).getTextContent();
                String nomorHp = userElem.getElementsByTagName("nomorHp").item(0).getTextContent();
                User user = new User(email, password, nama, LocalDate.parse(tanggalLahir), nomorHp, "", "", "");
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    // Update user di users.xml
    public static boolean updateUser(User updatedUser) {
        try {
            File xmlFile = new File("users.xml");
            if (!xmlFile.exists()) return false;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            NodeList userList = doc.getElementsByTagName("user");
            for (int i = 0; i < userList.getLength(); i++) {
                Element userElem = (Element) userList.item(i);
                String email = userElem.getElementsByTagName("email").item(0).getTextContent();
                if (email.equals(updatedUser.getEmail())) {
                    userElem.getElementsByTagName("nama").item(0).setTextContent(updatedUser.getNama());
                    userElem.getElementsByTagName("nomorHp").item(0).setTextContent(updatedUser.getNomorHp());
                    userElem.getElementsByTagName("tanggalLahir").item(0).setTextContent(updatedUser.getTanggalLahir().toString());
                    userElem.getElementsByTagName("password").item(0).setTextContent(updatedUser.getPassword());
                    break;
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

            // Sync juga ke HashMap
            users.put(updatedUser.getEmail(), updatedUser.getPassword());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Menambah user baru ke users.xml
    public static boolean addUserToXML(User newUser) {
        try {
            File xmlFile = new File("users.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc;

            Element root;
            if (!xmlFile.exists()) {
                doc = dBuilder.newDocument();
                root = doc.createElement("users");
                doc.appendChild(root);
            } else {
                doc = dBuilder.parse(xmlFile);
                root = doc.getDocumentElement();
            }

            Element userElem = doc.createElement("user");

            Element namaElem = doc.createElement("nama");
            namaElem.setTextContent(newUser.getNama());
            userElem.appendChild(namaElem);

            Element emailElem = doc.createElement("email");
            emailElem.setTextContent(newUser.getEmail());
            userElem.appendChild(emailElem);

            Element passwordElem = doc.createElement("password");
            passwordElem.setTextContent(newUser.getPassword());
            userElem.appendChild(passwordElem);

            Element tanggalLahirElem = doc.createElement("tanggalLahir");
            tanggalLahirElem.setTextContent(newUser.getTanggalLahir().toString());
            userElem.appendChild(tanggalLahirElem);

            Element nomorHpElem = doc.createElement("nomorHp");
            nomorHpElem.setTextContent(newUser.getNomorHp());
            userElem.appendChild(nomorHpElem);

            root.appendChild(userElem);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

            // Sync juga ke HashMap
            users.put(newUser.getEmail(), newUser.getPassword());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
