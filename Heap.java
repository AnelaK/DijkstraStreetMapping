/*Name: Anela Karamustafic
 *NETID: akaramus
 *URID: 31404167
 *Project 3
 *Partner: Henry Gardner
 *Lab Section: 6:15-7:30 pm
 */

import java.util.*;

public class Heap {
    HashMap<String, Integer> hashMap;
    int size;
    List<Integer> heap;
    List<String> intersectionId;
    List<Double> value;
    List<Integer> position;

    public Heap() {
        hashMap = new HashMap<>();
        size = 0;
        heap = new ArrayList<>();
        intersectionId = new ArrayList<>();
        value = new ArrayList<>();
        position = new ArrayList<>();
    }

    void swap(int i, int j) {
        int temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
        position.set(heap.get(i), i);
        position.set(heap.get(j), j);
    }

    void up(int index) {
        if(index == 0) {
            return;
        }
        int par = (index - 1) / 2;
        if(value.get(heap.get(par)) <= value.get(heap.get(index))) {
            return;
        }
        swap(index, par);
        up(par);
    }

    void down(int index) {
        int child = index * 2 + 1;
        if(child >= size) {
            return;
        }
        if(child < size - 1) {
            if(value.get(heap.get(child)) > value.get(heap.get(child + 1))) {
                child++;
            }
        }
        if(value.get(heap.get(index)) <= value.get(heap.get(child))) {
            return;
        }
        swap(child, index);
        down(child);
    }

    String removeTop() { 
        int x = heap.get(0);
        swap(0, size-1);
        size--;
        down(0);
        return intersectionId.get(x);
    }

    double getValue(String id) {
        return value.get(hashMap.get(id));
    }

    void updateValue(String id, double w) {
        value.set(hashMap.get(id), w);
        up(position.get(hashMap.get(id)));
    }

    boolean contatinIntersection(String id) {
        return hashMap.containsKey(id);
    }


    void addElement(String id, double w) {
        hashMap.put(id, intersectionId.size());

        if(heap.size() > size) {
            heap.set(size, intersectionId.size());
        }
        else {
            heap.add(intersectionId.size());
        }
        
        intersectionId.add(id);
        value.add(w);
        position.add(size);
        size++;
        up(size-1);
    }
}