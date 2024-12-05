package cat.urv.deim;

import java.util.Iterator;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.PosicioForaRang;

public class LlistaNoOrdenada<E extends Comparable<E>> implements ILlistaGenerica<E> {

    private Node<E> fantasma;
    private int numElem;

    public LlistaNoOrdenada() {
        this.fantasma=new Node<>(null);
        numElem=0;
    }

        private class Node<E> {
            private E dada;
            private int amistat;
            private Node<E> seguent;


            public Node(E dada) {
                this.dada=dada;
                amistat=0;
                this.seguent=null;
            }

        }

        public void inserir(E e) {
            Node<E> actual = fantasma;
            Node<E> nouNode = new Node<>(e);
            while (actual.seguent != null) {
                actual = actual.seguent;
            }
            actual.seguent= nouNode;
            numElem++;
        }

        public void esborrar(E e) throws ElementNoTrobat {
            if (esBuida() || !existeix(e)) throw new ElementNoTrobat();
            Node<E> actual = fantasma.seguent;
            Node<E> anterior = fantasma.seguent;
            boolean trobat = false;
            while (!trobat){
            if (actual.dada.equals(e)) trobat = true;
            else{
                anterior = actual;
                actual = actual.seguent;
            }
            }
            if (trobat){
            if (actual.seguent != null) anterior.seguent = actual.seguent;
            else anterior.seguent = null;
            numElem--;
        }
        else throw new ElementNoTrobat();
        }

        public E consultar(int pos) throws PosicioForaRang {
            Node<E> actual = fantasma.seguent;
            if (pos <0 || pos > numElem) {
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
            if (esBuida()) throw new ElementNoTrobat();

            Node<E> actual = fantasma.seguent;
            int i = 0;

            while (actual != null && !actual.dada.equals(e) && i < numElem) {
                actual = actual.seguent;
                i++;
            }

            if (actual != null && actual.dada.equals(e)) {
                return i;
            } else {
                throw new ElementNoTrobat();
            }
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

        public int getPes(E key) throws ElementNoTrobat {
            if(esBuida()) throw new ElementNoTrobat();
            Node<E> actual = fantasma.seguent;
            while(actual.seguent  != null && !actual.dada.equals(key)) {
                actual=actual.seguent;
            }
            if(actual.dada.equals(key)) return actual.amistat;
            throw new ElementNoTrobat();
        }

        public void setPes(E key, int pes) {
            Node<E> actual = fantasma;
            while(actual.seguent != null && !actual.dada.equals(key)) {
                actual = actual.seguent;
            }
            if(actual.dada.equals(key)) actual.amistat=pes;
        }

}


