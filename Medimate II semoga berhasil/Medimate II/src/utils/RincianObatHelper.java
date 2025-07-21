package utils;

import models.RincianObat;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import utils.Session;
import models.User;

public class RincianObatHelper {
    private static final String FILE_PATH = "rincian_obat.xml";

    public static List<RincianObat> loadAll() {
        List<RincianObat> list = new ArrayList<>();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return list;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            NodeList nList = doc.getElementsByTagName("rincian");
            User currentUser = Session.getCurrentUser();
            String currentEmail = currentUser != null ? currentUser.getEmail() : null;
            for (int i = 0; i < nList.getLength(); i++) {
                Element e = (Element) nList.item(i);
                String email = e.getElementsByTagName("email").item(0) != null ? e.getElementsByTagName("email").item(0).getTextContent() : null;
                if (currentEmail == null || (email != null && email.equals(currentEmail))) {
                    RincianObat r = new RincianObat();
                    r.setNamaObat(e.getElementsByTagName("namaObat").item(0).getTextContent());
                    r.setKategori(e.getElementsByTagName("kategori").item(0).getTextContent());
                    r.setDosis(e.getElementsByTagName("dosis").item(0).getTextContent());
                    r.setFrekuensi(e.getElementsByTagName("frekuensi").item(0).getTextContent());
                    r.setTanggalMulai(LocalDate.parse(e.getElementsByTagName("tanggalMulai").item(0).getTextContent()));
                    r.setTanggalSelesai(LocalDate.parse(e.getElementsByTagName("tanggalSelesai").item(0).getTextContent()));
                    r.setWaktuMinum(e.getElementsByTagName("waktuMinum").item(0).getTextContent());
                    r.setReminder(e.getElementsByTagName("reminder").item(0).getTextContent());
                    r.setCatatan(e.getElementsByTagName("catatan").item(0).getTextContent());
                    r.setEmail(email);
                    list.add(r);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void saveAll(List<RincianObat> list) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element root = doc.createElement("rincianobatlist");
            doc.appendChild(root);
            for (RincianObat r : list) {
                Element rincian = doc.createElement("rincian");
                root.appendChild(rincian);
                addChild(doc, rincian, "email", r.getEmail());
                addChild(doc, rincian, "namaObat", r.getNamaObat());
                addChild(doc, rincian, "kategori", r.getKategori());
                addChild(doc, rincian, "dosis", r.getDosis());
                addChild(doc, rincian, "frekuensi", r.getFrekuensi());
                addChild(doc, rincian, "tanggalMulai", r.getTanggalMulai().toString());
                addChild(doc, rincian, "tanggalSelesai", r.getTanggalSelesai().toString());
                addChild(doc, rincian, "waktuMinum", r.getWaktuMinum());
                addChild(doc, rincian, "reminder", r.getReminder());
                addChild(doc, rincian, "catatan", r.getCatatan());
            }
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(FILE_PATH));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private static void addChild(Document doc, Element parent, String tag, String value) {
        Element e = doc.createElement(tag);
        e.setTextContent(value);
        parent.appendChild(e);
    }
} 