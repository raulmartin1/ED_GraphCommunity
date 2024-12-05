package cat.urv.deim;

import java.util.Iterator;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.PosicioForaRang;

public class LlistaOrdenada<E extends Comparable<E>> implements ILlistaGenerica<E> {
    private Node<E> fantasma;
    private int numElem;

public LlistaOrdenada() {
    this.fantasma=new Node<>(null);
    numElem=0;
}

    private static class Node<E> {
        private E dada;
        private Node<E> seguent;

        public Node(E dada) {
            this.dada=dada;
            this.seguent=null;
        }

        public Persona getPersona() {
            return (Persona) dada;
        }
    }


    public void inserir(E e) {
        Node<E> nouNode = new Node<>(e);
        Node<E> actual = fantasma;

        if(esBuida()) actual.seguent=nouNode;
        else {
            actual=actual.seguent;
            Node<E> anterior = fantasma;

        while (actual != null && actual.getPersona().compareTo(nouNode.getPersona())<0) {
            anterior = actual;
            actual = actual.seguent;
        }
        nouNode.seguent = actual;
        anterior.seguent= nouNode;
        numElem++;
    }
    }

    public void esborrar(E e) throws ElementNoTrobat {
        if (esBuida() || !existeix(e)) throw new ElementNoTrobat();
        Node<E> actual = fantasma.seguent;
        Node<E> anterior = fantasma.seguent;
        while (!actual.dada.equals(e) && actual.seguent != null){
            anterior = actual;
            actual = actual.seguent;
        }
        if (actual.dada.equals(e)){
            anterior.seguent = actual.seguent;
            numElem--;
        }
        else throw new ElementNoTrobat();
    }

    public E consultar(int pos) throws PosicioForaRang {
        Node<E> actual = fantasma.seguent;
        if (pos <0) {
            throw new PosicioForaRang();
        }
        for (int i=0; actual != null && i<pos; i++) {
            actual=actual.seguent;
        }
        if(actual==null) {
            throw new PosicioForaRang();
        }
        return actual.dada;
    }

    public int buscar(E e) throws ElementNoTrobat {
        Node<E> actual = fantasma;
        int i=0;
        while(actual.seguent != null){
        if(actual.seguent.dada.equals(e)) {
            return i;
        }
        else{
            actual=actual.seguent;
            i++;
        }
    }
        throw new ElementNoTrobat();
    }

    public boolean existeix(E e) {
        try {
            buscar(e);
            return true;
        } catch (ElementNoTrobat e1) {
            return false;
        }
    }

    public boolean esBuida() {
        return fantasma.seguent == null;
    }

    public int numElements() {
    Node<E> actual=fantasma.seguent;
    int numElements=0;
    while(actual!=null) {
        numElements++;
        actual=actual.seguent;
    }
    return numElements;
    }

    public Iterator<E> iterator() {
        @SuppressWarnings("rawtypes")
        final Node primer = fantasma; // final y asi no se podra modificar la referencia

        return new Iterator<E>() {
            @SuppressWarnings("rawtypes")
            private Node actual = primer;

            public boolean hasNext() {
                return actual.seguent != null;
            }

            @SuppressWarnings("unchecked")
            public E next() {
                E element = (E) actual.seguent.dada;
                actual = actual.seguent;
                return element;
            }
        };
        }

}


