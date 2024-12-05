package cat.urv.deim;

import cat.urv.deim.exceptions.ElementNoTrobat;
import java.util.ArrayList;

import java.util.Iterator;

public class HashMapIndirecte<K extends Comparable<K>, V extends Comparable<V>> implements IHashMap<K, V> {
    private ArrayList<Node> taula;
    private int dim;
    private int numElems;
    int numComunitats;

    public HashMapIndirecte(int dim) {
        this.dim = dim;
        this.taula = new ArrayList<Node>(dim);
        for(int i=0; i<dim; i++){
            taula.add(null);
        }
        this.numElems=0;
        this.numComunitats=0;
    }

    public class Node {
        public K clau;
        public V valor;
        public LlistaNoOrdenada<K> amistats;
        public int comunitat;

        public Node(K clau, V valor) {
            this.clau=clau;
            this.valor=valor;
            amistats = new LlistaNoOrdenada<>();
            this.comunitat = getPos(clau);
        }
    }

    public void inserir(K key, V value){
        int pos = getPos(key);
        Node nodeHash = new Node(key, value);
        Node actual = taula.get(pos);
        if(factorCarrega() > 0.75) redimensionament();
        if(actual != null) {
            if(!taula.get(pos).clau.equals(key)) {
                while(taula.get(pos) != null){

                redimensionament();
                pos = getPos(key);
            }
        }
        else {
            numElems--;
        }
        }
        taula.set(pos, nodeHash);
        numElems++;
        numComunitats++;
    }

    public void inserir(K key, V value, LlistaNoOrdenada<K> llista) {
        int pos =getPos(key);
        Node nodeHash =  new Node(key, value);
        nodeHash.amistats = llista;
        taula.set(pos, nodeHash);
    }

    public V consultar(K key) throws ElementNoTrobat {
        int pos = getPos(key);
        Node actual = taula.get(pos);
        if(actual==null) throw new ElementNoTrobat();
        return taula.get(pos).valor;
    }

    public void esborrar(K key) throws ElementNoTrobat {
        int pos = getPos(key);
        Node actual = taula.get(pos);
        if(actual == null) throw new ElementNoTrobat();
        taula.set(pos, null);
        numElems--;
    }

    public boolean buscar(K key) {
        int pos = getPos(key);
        Node actual = taula.get(pos);
        if(actual == null) return false;
        return true;
    }

    public boolean esBuida() {
        return numElems==0;
    }

    public int numElements() {
        return numElems;
    }

    public ILlistaGenerica<K> obtenirClaus() {
        ILlistaGenerica<K> claus = new LlistaNoOrdenada<K>();
        Node nodeAux;
        for (int i =0; i< midaTaula(); i++) {
            Node actual = taula.get(i);
            if (taula.get(i) != null) {
                nodeAux = actual;
                claus.inserir(nodeAux.clau);
            }
        }

       return claus;
    }

    public float factorCarrega() {
        return (float) numElems/dim;
    }

    public int midaTaula() {
        return dim;
    }

    public Iterator<V> iterator() {
        ArrayList<V> llistaValors = new ArrayList<V>();
        Node actual;

        for(int i=0; i< midaTaula();  i++) {
            actual=taula.get(i);
        while(actual!=null) {
            llistaValors.add(actual.valor);
        }
        }

        return new Iterator<V>() {
            int pos=0;

            public boolean hasNext() {
                return pos < llistaValors.size();
            }

            public V next() {
                return llistaValors.get(pos++);
            }
        };
    }


    private int getPos(K key) {
        return  key.hashCode() % this.dim;
    }

    public void redimensionament() {
        int nouDim = dim*2;
        HashMapIndirecte<K, V> novaTaula = new HashMapIndirecte<>(nouDim);
        for(int i=0; i<dim; i++) {
            if(taula.get(i)!=null) {
                Node actual = taula.get(i);
                novaTaula.inserir(actual.clau, actual.valor, actual.amistats);
            }
        }
        this.taula=novaTaula.taula;
        this.dim=novaTaula.dim;

        }

        public LlistaNoOrdenada<K> getAmistats(K key) {
            return taula.get(getPos(key)).amistats;
        }

        public void setComunitat(K key, int comunitat) throws ElementNoTrobat {
            int pos = getPos(key);
            if(taula.get(pos) == null) {
                throw new ElementNoTrobat();
            }
            taula.get(pos).comunitat = comunitat;
        }

        public int getComunitat(K key) throws ElementNoTrobat {
            int pos = getPos(key);
            if(taula.get(pos) == null) {
                throw new ElementNoTrobat();
            }
            return taula.get(pos).comunitat;
        }

        public int numComunitats() {
            return numComunitats;
        }
}

