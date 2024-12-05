package cat.urv.deim;

import cat.urv.deim.exceptions.VertexNoTrobat;
import cat.urv.deim.exceptions.ArestaNoTrobada;
import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.PosicioForaRang;


public class Graf <K extends Comparable<K>, V extends Comparable<V>> implements IGraf<K, V>{
    private HashMapIndirecte <K,V> taula;
    private int numArestes;

    public Graf(int dim) {
        taula = new HashMapIndirecte<>(dim);
        numArestes=0;
    }

public void inserirVertex(K key, V value){
    taula.inserir(key, value);
}

 public V consultarVertex(K key) throws VertexNoTrobat {
    try {
        return taula.consultar(key);
    } catch (ElementNoTrobat e) {
        throw new VertexNoTrobat();
    }
 }

 public void esborrarVertex(K key) throws VertexNoTrobat {
    if(!taula.buscar(key)) throw new VertexNoTrobat();
    LlistaNoOrdenada<K> amistats = taula.getAmistats(key);
    LlistaNoOrdenada<K> amistatsAux;

    for (int i=0; i< amistats.numElements(); i++){

        try {
            amistatsAux = taula.getAmistats(amistats.consultar(i));
            amistatsAux.esborrar(key);
            numArestes--;
        } catch (PosicioForaRang e){
            //throw new PosicioForaRang();
        } catch (ElementNoTrobat e) {
            //throw new ElementNoTrobat();
        }
        }
        try {
            taula.esborrar(key);
        } catch (ElementNoTrobat e){
            throw new VertexNoTrobat();
        }
 }

 public boolean esBuida() {
    return taula.numElements() == 0;
 }

 public int numVertex() {
    return taula.numElements();
 }

 public ILlistaGenerica<K> obtenirVertexIDs() {
    return taula.obtenirClaus();
 }


 public void inserirAresta(K v1, K v2) throws VertexNoTrobat {
    if(!taula.buscar(v1) || !taula.buscar(v2)) throw new VertexNoTrobat();
    LlistaNoOrdenada<K> llista = taula.getAmistats(v1);
    llista.inserir(v2);
    llista=taula.getAmistats(v2);
    llista.inserir(v1);
    numArestes++;
 }

 public boolean existeixAresta(K v1, K v2) throws VertexNoTrobat {
    if(!taula.buscar(v1) ||!taula.buscar(v2)) throw new VertexNoTrobat();
    LlistaNoOrdenada<K> llista = taula.getAmistats(v1);
    return llista.existeix(v2);
 }


 public void esborrarAresta(K v1, K v2) throws VertexNoTrobat, ArestaNoTrobada {
    if(!taula.buscar(v1) ||!taula.buscar(v2)) throw new VertexNoTrobat();
    LlistaNoOrdenada<K> llista = taula.getAmistats(v1);

    try {
        llista.esborrar(v2);
        llista = taula.getAmistats(v2);
        llista.esborrar(v1);
        numArestes--;
    }catch (ElementNoTrobat e){
        throw new ArestaNoTrobada();
    }

 }

 public int numArestes() {
    return numArestes;
 }


 public boolean vertexAillat(K v1) throws VertexNoTrobat {
    if(!taula.buscar(v1)) throw new VertexNoTrobat();
    LlistaNoOrdenada<K> llista = taula.getAmistats(v1);
    if(llista.esBuida()) return true;   //vertex no te veins
    return false;                       //vertex te veins
 }

 public int numVeins(K v1) throws VertexNoTrobat {
    if(!taula.buscar(v1)) throw new VertexNoTrobat();
    LlistaNoOrdenada<K> llista = taula.getAmistats(v1);
    return llista.numElements();
 }

 public ILlistaGenerica<K> obtenirVeins(K v1) throws VertexNoTrobat {
    return taula.getAmistats(v1);
 }

 //OPCIONALS
 public ILlistaGenerica<K> obtenirNodesConnectats(K v1) throws VertexNoTrobat {
 return null;
 }

 public ILlistaGenerica<K> obtenirComponentConnexaMesGran() {
    return null;
 }

 public double Modularitat(){
    double modularitat = 0.0;
    int m = numArestes();

    if (m == 0) {
        //System.out.println("No s'han trobat arestas en el graf, \nModularitat:");
        return 0.0;
    }

    try {
        ILlistaGenerica<K> vertexs = obtenirVertexIDs();

        for (int i =0; i< vertexs.numElements(); i++) {
        K v1 = vertexs.consultar(i);
        int comunitat_v1 = taula.getComunitat(v1);
        ILlistaGenerica<K> veins_v1 = taula.getAmistats(v1);
        for(int j = 0; j < veins_v1.numElements(); j++) {

            K v2 = veins_v1.consultar(j);
            int comunitat_v2 = taula.getComunitat(v2);

            if(comunitat_v1 == comunitat_v2) {
                int grau_v1 = numVeins(v1);
                int grau_v2 = numVeins(v2);
                modularitat += (1 - (grau_v1 * grau_v2) / (2.0 * m));
            }
        }

        }
    }catch (VertexNoTrobat e){
        e.printStackTrace();
    }catch (ElementNoTrobat e){
        e.printStackTrace();
    }catch (PosicioForaRang e) {
        e.printStackTrace();
    }

    return modularitat / (2.0 * m);
 }

 public void optimitzarModularitat() throws ElementNoTrobat, PosicioForaRang{
    ILlistaGenerica<K> claus = taula.obtenirClaus();
    boolean millorat = false;

    for (int i =0; i < claus.numElements(); i++){
        double modularitat = Modularitat();
        K clau1= claus.consultar(i);
        int com1 = taula.getComunitat(clau1);// comunitat actual

        for (int j = 0; j< taula.numComunitats(); j++) {
            taula.setComunitat(clau1, j);
            double novaMod = Modularitat();
            if (novaMod > modularitat) {
                millorat=true;
                com1= j;
                modularitat = novaMod;
            }
        }

        if(millorat){
            taula.setComunitat(clau1, com1);
        }

    }
 }

}
