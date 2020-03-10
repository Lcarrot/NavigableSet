import java.util.*;

public class MyNavigableSet<T> extends AbstractSet<T> implements NavigableSet<T> {

    private int size;
    private ArrayList<T> data;
    Comparator<T> comparator;

    public MyNavigableSet(Comparator<T> comparator, Collection<T> collection) {
        this.comparator = comparator;
        size = collection.size();
        Iterator<T> iterator = collection.iterator();
        for (int i = 0; i < collection.size(); i++) {
            data.add(iterator.next());
            boolean flag = true;
            int leftCorner = 0;
            int rightCorner = i;
            while (flag) {
                int index = (leftCorner + rightCorner)/2;
                if (leftCorner == rightCorner || comparator.compare(data.get(i), data.get(index - 1)) > 0 && comparator.compare(data.get(index), data.get(i)) > 0) {
                    T c = data.get(i);
                    for (int k = leftCorner; k < i; k++) {
                        data.set(k+1, data.get(k));
                    }
                    data.set(leftCorner, c);
                    break;
                }
                if (comparator.compare(data.get(index - 1), data.get(i)) > 0 && comparator.compare(data.get(index), data.get(i)) > 0) {
                    rightCorner = index;
                }
                else {
                    leftCorner = index;
                }
            }
        }
    }

    @Override
    public T lower(T t) {
        int[] arr = findIndexes(t);
        assert arr != null;
        int leftCorner = arr[0];
        int rightCorner = arr[1];
        boolean flag = true;
        int index = 0;
        while (flag) {
            if (leftCorner == rightCorner) {
                if (comparator.compare(t, data.get(leftCorner)) > 0) {
                    return data.get(leftCorner);
                }
                flag = false;
            }
            index = (leftCorner + rightCorner) / 2;
            if (comparator.compare(t, data.get(index)) > 0 && comparator.compare(t, data.get(index + 1)) < 0) {
                return data.get(index);
            }
        }
        return null;
    }

    @Override
    public T floor(T t) {
        int[] arr = findIndexes(t);
        assert arr != null;
        int leftCorner = arr[0];
        int rightCorner = arr[1];
        boolean flag = true;
        int index;
        while (flag) {
            if (leftCorner == rightCorner) {
                if (comparator.compare(t, data.get(leftCorner)) > 0) {
                    return data.get(leftCorner);
                }
                flag = false;
            }
            index = (leftCorner + rightCorner) / 2;
            if (comparator.compare(t, data.get(index)) >= 0 && comparator.compare(t, data.get(index + 1)) <= 0) {
                return data.get(index);
            }
        }
        return null;
    }

    @Override
    public T ceiling(T t) {
        int[] arr = findIndexes(t);
        assert arr != null;
        int leftCorner = arr[0];
        int rightCorner = arr[1];
        boolean flag = true;
        int index = 0;
        while (flag) {
            if (leftCorner == rightCorner) {
                if (comparator.compare(t, data.get(leftCorner)) > 0) {
                    return data.get(leftCorner);
                }
                flag = false;
            }
            index = (leftCorner + rightCorner) / 2;
            if (comparator.compare(t, data.get(index)) <= 0 && comparator.compare(t, data.get(index - 1)) >= 0) {
                flag = false;
                return data.get(index);
            }
        }
        return null;
    }

    @Override
    public T higher(T t) {
        int[] arr = findIndexes(t);
        assert arr != null;
        int leftCorner = arr[0];
        int rightCorner = arr[1];
        boolean flag = true;
        int index = 0;
        while (flag) {
            if (leftCorner == rightCorner) {
                if (comparator.compare(t, data.get(leftCorner)) > 0) {
                    return data.get(leftCorner);
                }
                flag = false;
            }
            index = (leftCorner + rightCorner) / 2;
            if (comparator.compare(t, data.get(index)) < 0 && comparator.compare(t, data.get(index - 1)) > 0) {
                return data.get(index);
            }
        }
        return null;
    }

    @Override
    public T pollFirst() {
        if (data == null) {
            return null;
        }
        T c = data.get(0);
        for (int i = 0; i < size; i++) {
            data.set(i, data.get(i + 1));
        }
        data.set(size--,null);
        return c;
    }


    @Override
    public T pollLast() {
        if (data == null) {
            return null;
        }
        T c = data.get(size);
        data.set(size--,null);
        return c;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int cursor = 0;
            @Override
            public boolean hasNext() {
                if (cursor >= size) {
                    return false;
                }
                return true;
            }

            @Override
            public T next() {
                return data.get(cursor++);
            }
        };
    }

    @Override
    public MyNavigableSet<T> descendingSet() {
        for (int i = 0; i <= size/2; i++) {
            T c = data.get(i);
            data.set(i, data.get(size - i));
            data.set(size - i, c);
        }
            return new MyNavigableSet<T>(comparator , data);
    }

    @Override
    public Iterator<T> descendingIterator() {
        return new Iterator<T>() {
            private int cursor;
            @Override
            public boolean hasNext() {
                if (cursor >= 0) {
                    return true;
                }
                return false;
            }

            @Override
            public T next() {
                return data.get(cursor--);
            }
        };
    }

    @Override
    public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        int leftCorner = 0;
        int newLeftCorner = -1;
        int rightCorner = size;
        int newRightCorner = -1;
        boolean flag = true;
        for (int i =0; i <= size && flag; i++) {
            int index = (leftCorner + rightCorner)/2;
            if (data.get(index).equals(fromElement)) {
                newLeftCorner = index;
                leftCorner = index;
                if (newRightCorner != -1) {
                    flag = false;
                }
            }
            if (data.get(index).equals(toElement)) {
                newRightCorner = index;
                rightCorner = index;
                if (newLeftCorner != -1) {
                    flag = false;
                }
            }
            if (flag) {
                if (leftCorner != -1) {
                    if (comparator.compare(data.get(index), fromElement) > 0 && comparator.compare(fromElement, data.get(index)) > 0) {
                        leftCorner = index;
                    }
                    else if (comparator.compare(data.get(index), fromElement) < 0 && comparator.compare(fromElement, data.get(index)) < 0) {
                       rightCorner = index;
                    }
                    else {
                        throw new NoSuchElementException();
                    }
                }
                else {
                    if (comparator.compare(data.get(index), toElement) > 0 && comparator.compare(toElement, data.get(index)) > 0) {
                        leftCorner = index;
                    }
                    else if (comparator.compare(data.get(index), toElement) < 0 && comparator.compare(toElement, data.get(index)) < 0) {
                        rightCorner = index;
                    }
                    else {
                        throw new NoSuchElementException();
                    }
                }
            }
        }
        if (!fromInclusive) {
            newLeftCorner++;
        }
        if (!toInclusive) {
            newRightCorner--;
        }
        ArrayList<T> arrayList = new ArrayList<>();
        for (int i = newLeftCorner; i <= newRightCorner; i++) {
           arrayList.add(data.get(i));
        }
        return new MyNavigableSet<T>(comparator, arrayList);
    }

    @Override
    public NavigableSet<T> headSet(T toElement, boolean inclusive) {
        int leftCorner = 0;
        int rightCorner = size;
        boolean flag = true;
        for (int i =0; i <= size && flag; i++) {
            int index = (leftCorner + rightCorner) / 2;
            if (data.get(index).equals(toElement)) {
                rightCorner = index;
                flag = false;
            }
            if (comparator.compare(data.get(index), toElement) > 0 && comparator.compare(toElement, data.get(index)) > 0) {
                leftCorner = index;
            }
            else if (comparator.compare(data.get(index), toElement) < 0 && comparator.compare(toElement, data.get(index)) < 0) {
                rightCorner = index;
            }
            else {
                throw new NoSuchElementException();
            }
        }
        ArrayList<T> arrayList = new ArrayList<>();
        if (!inclusive) {
            rightCorner--;
        }
        for (int i =0 ; i <= rightCorner; i++) {
            arrayList.add(data.get(i));
        }
        return new MyNavigableSet<T>(comparator, arrayList);
    }

    @Override
    public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        int leftCorner = 0;
        int rightCorner = size;
        boolean flag = true;
        for (int i =0; i <= size && flag; i++) {
            int index = (leftCorner + rightCorner) / 2;
            if (data.get(index).equals(fromElement)) {
                leftCorner = index;
                flag = false;
            }
            if (comparator.compare(data.get(index), fromElement) > 0 && comparator.compare(fromElement, data.get(index)) > 0) {
                leftCorner = index;
            }
            else if (comparator.compare(data.get(index), fromElement) < 0 && comparator.compare(fromElement, data.get(index)) < 0) {
                rightCorner = index;
            }
            else {
                throw new NoSuchElementException();
            }
        }
        ArrayList<T> arrayList = new ArrayList<>();
        if (!inclusive) {
            leftCorner++;
        }
        for (int i =leftCorner ; i <= size; i++) {
            arrayList.add(data.get(i));
        }
        return new MyNavigableSet<T>(comparator, arrayList);
    }

    @Override
    public Comparator<? super T> comparator() {
        return comparator;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        int leftCorner = 0;
        int newLeftCorner = -1;
        int rightCorner = size;
        int newRightCorner = -1;
        boolean flag = true;
        for (int i =0; i <= size && flag; i++) {
            int index = (leftCorner + rightCorner)/2;
            if (data.get(index).equals(fromElement)) {
                newLeftCorner = index;
                leftCorner = index;
                if (newRightCorner != -1) {
                    flag = false;
                }
            }
            if (data.get(index).equals(toElement)) {
                newRightCorner = index;
                rightCorner = index;
                if (newLeftCorner != -1) {
                    flag = false;
                }
            }
            if (flag) {
                if (leftCorner != -1) {
                    if (comparator.compare(data.get(index), fromElement) > 0 && comparator.compare(fromElement, data.get(index)) > 0) {
                        leftCorner = index;
                    }
                    else if (comparator.compare(data.get(index), fromElement) < 0 && comparator.compare(fromElement, data.get(index)) < 0) {
                        rightCorner = index;
                    }
                    else {
                        throw new NoSuchElementException();
                    }
                }
                else {
                    if (comparator.compare(data.get(index), toElement) > 0 && comparator.compare(toElement, data.get(index)) > 0) {
                        leftCorner = index;
                    }
                    else if (comparator.compare(data.get(index), toElement) < 0 && comparator.compare(toElement, data.get(index)) < 0) {
                        rightCorner = index;
                    }
                    else {
                        throw new NoSuchElementException();
                    }
                }
            }
        }
        ArrayList<T> arrayList = new ArrayList<>();
        for (int i = newLeftCorner; i <= newRightCorner; i++) {
            arrayList.add(data.get(i));
        }
        return new MyNavigableSet<T>(comparator, arrayList);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        int leftCorner = 0;
        int rightCorner = size;
        boolean flag = true;
        for (int i =0; i <= size && flag; i++) {
            int index = (leftCorner + rightCorner) / 2;
            if (data.get(index).equals(toElement)) {
                rightCorner = index;
                flag = false;
            }
            if (comparator.compare(data.get(index), toElement) > 0 && comparator.compare(toElement, data.get(index)) > 0) {
                leftCorner = index;
            }
            else if (comparator.compare(data.get(index), toElement) < 0 && comparator.compare(toElement, data.get(index)) < 0) {
                rightCorner = index;
            }
            else {
                throw new NoSuchElementException();
            }
        }
        ArrayList<T> arrayList = new ArrayList<>();
        for (int i =0 ; i <= rightCorner; i++) {
            arrayList.add(data.get(i));
        }
        return new MyNavigableSet<T>(comparator, arrayList);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        int leftCorner = 0;
        int rightCorner = size;
        boolean flag = true;
        for (int i =0; i <= size && flag; i++) {
            int index = (leftCorner + rightCorner) / 2;
            if (data.get(index).equals(fromElement)) {
                leftCorner = index;
                flag = false;
            }
            if (comparator.compare(data.get(index), fromElement) > 0 && comparator.compare(fromElement, data.get(index)) > 0) {
                leftCorner = index;
            }
            else if (comparator.compare(data.get(index), fromElement) < 0 && comparator.compare(fromElement, data.get(index)) < 0) {
                rightCorner = index;
            }
            else {
                throw new NoSuchElementException();
            }
        }
        ArrayList<T> arrayList = new ArrayList<>();
        for (int i =leftCorner ; i <= size; i++) {
            arrayList.add(data.get(i));
        }
        return new MyNavigableSet<T>(comparator, arrayList);
    }

    @Override
    public T first() {
        return data.get(0);
    }

    @Override
    public T last() {
        return data.get(size);
    }

    @Override
    public int size() {
        return size;
    }

    private int[] findIndexes(T t) {
        int leftCorner = 0;
        int rightCorner = size;
        boolean flag = true;
        int index = 0;
        while (flag) {
            if (leftCorner == rightCorner) {
                flag = false;
                return new int[]{leftCorner, rightCorner};
            }
            index = (leftCorner + rightCorner) / 2;
            if (comparator.compare(t, data.get(index)) >= 0 && comparator.compare(t, data.get(index + 1)) <= 0) {
                flag = false;
                return new int[]{leftCorner, rightCorner};
            }
            if (comparator.compare(t, data.get(index)) < 0 && comparator.compare(t, data.get(index + 1)) < 0) {
                rightCorner = index;
            }
            if (comparator.compare(t, data.get(index)) > 0 && comparator.compare(t, data.get(index + 1)) > 0) {
                leftCorner = index;
            }
        }
        return null;
    }
}


