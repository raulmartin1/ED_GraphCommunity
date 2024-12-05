package cat.urv.deim;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.PosicioForaRang;
import cat.urv.deim.exceptions.VertexNoTrobat;
import cat.urv.deim.exceptions.ArestaNoTrobada;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class GrafPersones {

    // Metodes per a guardar persones
    private Graf<Integer, Persona> graf;

    public GrafPersones(int dim){
        graf = new Graf<Integer, Persona>(dim);
    }

    public GrafPersones(int mida, String fit) {
        graf = new Graf<Integer, Persona>(mida);
        Persona persona;
        try {
            BufferedReader persones = new BufferedReader(new FileReader(fit));
            File fitxer = new File(fit);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fitxer);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("node");
            for(int i = 0; i < nodeList.getLength() ; i++) {
                Element node = (Element) nodeList.item(i);
                int id_persona = Integer.parseInt(node.getAttribute("id"));
                int edat = -1;
                String nom = null;
                String cognom = null;
                int alsada = -1;
                int pes = -1;

                persona = new Persona(id_persona, edat, nom, cognom, alsada, pes);
                graf.inserirVertex(id_persona, persona);
            }

            NodeList llistaArestes = document.getElementsByTagName("edge");
            for (int i = 0; i < llistaArestes.getLength(); i++) {

                Element aresta = (Element) llistaArestes.item(i);
                int k1= Integer.parseInt(aresta.getAttribute("source"));
                int k2= Integer.parseInt(aresta.getAttribute("target"));
                try {
                    graf.inserirAresta(k1, k2);
                }catch (VertexNoTrobat e){
                    e.printStackTrace();
                }
            }
            }catch(NumberFormatException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }

    public void escriureDades(String fit) {
        int arestaNum = 1000;
        try (FileWriter text = new FileWriter(fit)) {
            text.write("<graphml>n");
            text.write("  <graph id=\"Graph\" uidGraph=\"2\" uidEdge=\"10001\">\n");

        //Escriure el vertex
        ILlistaGenerica<Integer> idPersones = obtenirPersonesIDs();
        for(int i = 0; i< idPersones.numElements(); i++){
            int id = idPersones.consultar(i);
            try {
                Persona persona = consultarPersona(id);
                text.write(String.format("    <node id=\"%d\" mainText=\"%d\" upText=\"\" size=\"30\"/>\n", persona.getId_persona(), persona.getId_persona()));
            }catch (ElementNoTrobat e) {
                e.printStackTrace();;
            }
            }
        //Escriure la aresta
        for(int i= 0; i< idPersones.numElements(); i++){
            int id = idPersones.consultar(i);
            try {
                Persona persona = consultarPersona(id);
                ILlistaGenerica<Integer> amistats = obtenirAmistats(persona);
                for(int j=0; j< amistats.numElements(); j++){
                    int idAmistat = amistats.consultar(j);
                    text.write(String.format("    <edge source=\"%d\" target=\"%d\" isDirect=\"false\" weight=\"0\" useWeight=\"true\" id=\"%d\" text=\"\" upText=\"\" arrayStyleStart=\"\" arrayStyleFinish=\"\" model_width=\"4\" model_type=\"0\" model_curveValue=\"0.1\" model_default=\"true\" ></edge>\n", id, idAmistat, arestaNum));
                    arestaNum++;
                }
            }catch(ElementNoTrobat e){
                e.printStackTrace();;
            }
        }

        text.write("  </graph>\n");
        text.write("</graphml>\n");

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void inserirPersona(Persona p) {
        graf.inserirVertex(p.getId_persona(), p);
    }

    public Persona consultarPersona(int id) throws ElementNoTrobat {
        try {
            return graf.consultarVertex(id);
        }catch (VertexNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }

    public void esborrarPersona(int id) throws ElementNoTrobat {
        try {
            graf.esborrarVertex(id);
        }catch(VertexNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }

    public int numPersones() {
        return graf.numVertex();
    }

    public boolean esBuida() {
        return numPersones() == 0;
    }

    public ILlistaGenerica<Integer> obtenirPersonesIDs() {
        return graf.obtenirVertexIDs();
    }

    // Metodes per a guardar amistats

    public void inserirAmistat(Persona p1, Persona p2) throws ElementNoTrobat  {
        try {
            graf.inserirAresta(p1.getId_persona(), p2.getId_persona());
        }catch(VertexNoTrobat e){
            throw new ElementNoTrobat();
        }
    }



    public void esborrarAmistat(Persona p1, Persona p2) throws ElementNoTrobat  {
    try {
        graf.esborrarAresta(p1.getId_persona(), p2.getId_persona());
    }catch (VertexNoTrobat e){
        throw new ElementNoTrobat();
    }catch (ArestaNoTrobada e) {
        throw new ElementNoTrobat();
    }
    }

    public boolean existeixAmistat(Persona p1, Persona p2) throws ElementNoTrobat {
        try {
            return graf.existeixAresta(p1.getId_persona(), p2.getId_persona());
        } catch (VertexNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }

    public int numAmistats() {
        return graf.numArestes();
    }


    public int numAmistats(Persona p) throws ElementNoTrobat {
        try {
            return graf.numVeins(p.getId_persona());
        }catch(VertexNoTrobat e){
            throw new ElementNoTrobat();
        }
    }

    public boolean teAmistats(Persona p) {
        try {
            return graf.numVeins(p.getId_persona()) != 0;
        }catch (VertexNoTrobat e) {
            return false;
        }
    }


    public ILlistaGenerica<Integer> obtenirAmistats(Persona p) throws ElementNoTrobat {
        try{
            return graf.obtenirVeins(p.getId_persona());
        }catch(VertexNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }

    // Aquest metode busca totes les persones del grup d'amistats de p que tenen alguna connexio amb p
    // ja sigui directament, o be perque son amics d'amics, o amics de amics de amics, etc.
    // Retorna una llista amb els ID de les persones del grup
    public ILlistaGenerica<Integer> obtenirGrupAmistats(Persona p) throws ElementNoTrobat {
    return null;
    }

    // Aquest metode busca el grup d'amistats mes gran del graf, es a dir, el que te major nombre
    // de persones que estan connectades entre si. Retorna una llista amb els ID de les persones del grup
    public ILlistaGenerica<Integer> obtenirGrupAmistatsMesGran() {
    return null;
    }

    public double Modularitat(){
        return graf.Modularitat();
    }

    public void optimitzarModularitat() throws ElementNoTrobat, PosicioForaRang{
        graf.optimitzarModularitat();
    }
}
