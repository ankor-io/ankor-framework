package at.irian.ankor.viewmodel;

import at.irian.ankor.ref.Ref;
import javafx.beans.property.ListPropertyBase;

import java.util.*;

public class ViewModelListBase<T> extends ViewModelBase {

    protected List<T> list;

    private int nonNullSize = 0;

    public ViewModelListBase(Ref viewModelRef, String name, List<T> list) {
        super(viewModelRef.append(name));
        this.list = new ArrayList<T>(list);
        this.nonNullSize = list.size();
    }

    private Ref listRef() {
        return thisRef().append("list");
    }

    private Ref listRef(int i) {
        return thisRef().append(String.format("list[%s]", i));
}

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean contains(Object o) {
        return list.contains(o);
    }

    public Iterator<T> iterator() {
        return list.iterator();
    }

    public Object[] toArray() {
        return list.toArray();
    }

    public <T1> T1[] toArray(T1[] a) {
        return list.toArray(a);
    }

    public boolean add(T t) {
        if (nonNullSize == list.size()) {
            list.add((T) new Object());
        }
        listRef(nonNullSize).setValue(t);
        if (t != null) {
            nonNullSize++;
        }
        return true;
    }

    public boolean remove(Object o) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean containsAll(Collection<?> c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean addAll(Collection<? extends T> c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean removeAll(Collection<?> c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean retainAll(Collection<?> c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void clear() {
        listRef().setValue(new ArrayList<T>());
    }

    public T get(int index) {
        return list.get(index);
    }

    public T set(int index, T element) {
        listRef(index).setValue(element);
        return list.set(index, element);
    }

    public void add(int index, T element) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public T remove(int index) {
        T toRemove = get(index);

        int i;
        for(i = index; i < list.size()-1; i++) {
            listRef(i).setValue(this.get(i+1));
        }
        listRef(i).setValue(null);

        nonNullSize--;

        return toRemove;
    }

    public int indexOf(Object o) {
        return list.indexOf(o);
    }
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}