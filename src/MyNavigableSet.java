import java.util.*;

/**
 A SortedSet extended with navigation methods reporting closest matches for given search targets. Methods lower, floor,
 ceiling, and higher return elements respectively less than, less than or equal, greater than or equal, and greater than
 a given element, returning null if there is no such element. A NavigableSet may be accessed and traversed in either ascending
 or descending order. The descendingSet method returns a view of the set with the senses of all relational and directional
 methods inverted. The performance of ascending operations and views is likely to be faster than that of descending ones.
 This interface additionally defines methods pollFirst and pollLast that return and remove the lowest and highest element,
 if one exists, else returning null. Methods subSet, headSet, and tailSet differ from the like-named SortedSet methods
 in accepting additional arguments describing whether lower and upper bounds are inclusive versus exclusive. Subsets of
 any NavigableSet must implement the NavigableSet interface.
 The return values of navigation methods may be ambiguous in implementations that permit null elements. However, even in
 this case the result can be disambiguated by checking contains(null). To avoid such issues, implementations of this interface
 are encouraged to not permit insertion of null elements. (Note that sorted sets of Comparable elements intrinsically do not permit null.)

 Methods subSet(E, E), headSet(E), and tailSet(E) are specified to return SortedSet to allow existing implementations of
 SortedSet to be compatibly retrofitted to implement NavigableSet, but extensions and implementations of this interface
 are encouraged to override these methods to return NavigableSet.
 */
public class MyNavigableSet<T> extends AbstractSet<T> implements NavigableSet<T>, SortedSet<T> {

    private int size;
    private ArrayList<T> data;
    Comparator<T> comparator;


    /**
     * Creates a SortedSet used by comparator
     * @param comparator using for sorting set
     * @param collection what we need to navigate
     */
    public MyNavigableSet(Comparator<T> comparator, Collection<T> collection) {
        this.comparator = comparator;
        size = collection.size();
        if (size > 0) {
            Iterator<T> iterator = collection.iterator();
            for (int i = 0; i < collection.size(); i++) {
                T element = iterator.next();
                int index = findIndex(element);
                if (index > 0 && comparator.compare(data.get(index), element) != 0) {
                    if (comparator.compare(data.get(index - 1), element) != 0) {
                        data.add(index, element);
                    }
                } else if (comparator.compare(data.get(index), element) != 0) {
                    data.add(index, element);
                }
            }
        }
    }

    /**
     * Returns the greatest element in this set strictly less than the
     * given element, or {@code null} if there is no such element.
     *
     * @param t the value to match
     * @return the greatest element less than {@code t},
     *         or {@code null} if there is no such element
     * @throws ClassCastException if the specified element cannot be
     *         compared with the elements currently in the set
     * @throws NullPointerException if the specified element is null
     *         and this set does not permit null elements
     */
    @Override
    public T lower(T t) {
        int i = findIndex(t);
        if (i == 0 || comparator.compare(data.get(i), t) > 0) {
            return null;
        }
        return data.get(i - 1);
    }

    /**
     * Returns the greatest element in this set less than or equal to
     * the given element, or {@code null} if there is no such element.
     *
     * @param t the value to match
     * @return the greatest element less than or equal to {@code t},
     *         or {@code null} if there is no such element
     * @throws ClassCastException if the specified element cannot be
     *         compared with the elements currently in the set
     * @throws NullPointerException if the specified element is null
     *         and this set does not permit null elements
     */
    @Override
    public T floor(T t) {
        int i = findIndex(t);
        if (comparator.compare(data.get(i), t) > 0) {
            if (size > 0) {
                return null;
            }
            return data.get(i - 1);

        }
        return data.get(i);
    }

    /**
     * Returns the least element in this set greater than or equal to
     * the given element, or {@code null} if there is no such element.
     *
     * @param t the value to match
     * @return the least element greater than or equal to {@code t},
     *         or {@code null} if there is no such element
     * @throws ClassCastException if the specified element cannot be
     *         compared with the elements currently in the set
     * @throws NullPointerException if the specified element is null
     *         and this set does not permit null elements
     */
    @Override
    public T ceiling(T t) {
        int i = findIndex(t);
        return data.get(i);

    }

    /**
     * Returns the least element in this set strictly greater than the
     * given element, or {@code null} if there is no such element.
     *
     * @param t the value to match
     * @return the least element greater than {@code t},
     *         or {@code null} if there is no such element
     * @throws ClassCastException if the specified element cannot be
     *         compared with the elements currently in the set
     * @throws NullPointerException if the specified element is null
     *         and this set does not permit null elements
     */
    @Override
    public T higher(T t) {
        int i = findIndex(t);
        if (comparator.compare(data.get(i), t) == 0) {
            if (size == 0) {
                return null;
            }
            return data.get(i - 1);
        }
        return data.get(i);
    }

    /**
     * Retrieves and removes the first (lowest) element,
     * or returns {@code null} if this set is empty.
     *
     * @return the first element, or {@code null} if this set is empty
     */
    @Override
    public T pollFirst() {
        if (data == null) {
            return null;
        }
        T c = data.get(0);
        for (int i = 0; i < size; i++) {
            data.set(i, data.get(i + 1));
        }
        data.set(size--, null);
        return c;
    }

    /**
     * Retrieves and removes the last (highest) element,
     * or returns {@code null} if this set is empty.
     *
     * @return the last element, or {@code null} if this set is empty
     */
    @Override
    public T pollLast() {
        if (data == null) {
            return null;
        }
        T c = data.get(size);
        data.set(size--, null);
        return c;
    }

    /**
     * Returns an iterator over the elements in this set, in ascending order.
     *
     * @return an iterator over the elements in this set, in ascending order
     */
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

    /**
     * Returns a reverse order view of the elements contained in this set.
     * The descending set is backed by this set, so changes to the set are
     * reflected in the descending set, and vice-versa.  If either set is
     * modified while an iteration over either set is in progress (except
     * through the iterator's own {@code remove} operation), the results of
     * the iteration are undefined.
     *
     * <p>The returned set has an ordering equivalent to
     * <tt>{@link Collections#reverseOrder(Comparator) Collections.reverseOrder}(comparator())</tt>.
     * The expression {@code s.descendingSet().descendingSet()} returns a
     * view of {@code s} essentially equivalent to {@code s}.
     *
     * @return a reverse order view of this set
     */
    @Override
    public MyNavigableSet<T> descendingSet() {
        for (int i = 0; i <= size / 2; i++) {
            T c = data.get(i);
            data.set(i, data.get(size - i));
            data.set(size - i, c);
        }
        return new MyNavigableSet<>(comparator, data);
    }

    /**
     * Returns an iterator over the elements in this set, in descending order.
     * Equivalent in effect to {@code descendingSet().iterator()}.
     *
     * @return an iterator over the elements in this set, in descending order
     */
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

    /**
     * Returns a view of the portion of this set whose elements range from
     * {@code fromElement} to {@code toElement}.  If {@code fromElement} and
     * {@code toElement} are equal, the returned set is empty unless {@code
     * fromInclusive} and {@code toInclusive} are both true.  The returned set
     * is backed by this set, so changes in the returned set are reflected in
     * this set, and vice-versa.  The returned set supports all optional set
     * operations that this set supports.
     *
     * <p>The returned set will throw an {@code IllegalArgumentException}
     * on an attempt to insert an element outside its range.
     *
     * @param fromElement low endpoint of the returned set
     * @param fromInclusive {@code true} if the low endpoint
     *        is to be included in the returned view
     * @param toElement high endpoint of the returned set
     * @param toInclusive {@code true} if the high endpoint
     *        is to be included in the returned view
     * @return a view of the portion of this set whose elements range from
     *         {@code fromElement}, inclusive, to {@code toElement}, exclusive
     * @throws ClassCastException if {@code fromElement} and
     *         {@code toElement} cannot be compared to one another using this
     *         set's comparator (or, if the set has no comparator, using
     *         natural ordering).  Implementations may, but are not required
     *         to, throw this exception if {@code fromElement} or
     *         {@code toElement} cannot be compared to elements currently in
     *         the set.
     * @throws NullPointerException if {@code fromElement} or
     *         {@code toElement} is null and this set does
     *         not permit null elements
     * @throws IllegalArgumentException if {@code fromElement} is
     *         greater than {@code toElement}; or if this set itself
     *         has a restricted range, and {@code fromElement} or
     *         {@code toElement} lies outside the bounds of the range.
     */
    @Override
    public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        int leftCorner = findIndex(fromElement);
        int rightCorner = findIndex(toElement);
        if (!fromInclusive) {

            leftCorner++;
        }
        if (!toInclusive) {
            rightCorner--;
        }
        ArrayList<T> arrayList = new ArrayList<>();
        for (int i = leftCorner; i <= rightCorner; i++) {
            arrayList.add(data.get(i));
        }
        return new MyNavigableSet<>(comparator, arrayList);
    }

    /**
     * Returns a view of the portion of this set whose elements are less than
     * (or equal to, if {@code inclusive} is true) {@code toElement}.  The
     * returned set is backed by this set, so changes in the returned set are
     * reflected in this set, and vice-versa.  The returned set supports all
     * optional set operations that this set supports.
     *
     * <p>The returned set will throw an {@code IllegalArgumentException}
     * on an attempt to insert an element outside its range.
     *
     * @param toElement high endpoint of the returned set
     * @param inclusive {@code true} if the high endpoint
     *        is to be included in the returned view
     * @return a view of the portion of this set whose elements are less than
     *         (or equal to, if {@code inclusive} is true) {@code toElement}
     * @throws ClassCastException if {@code toElement} is not compatible
     *         with this set's comparator (or, if the set has no comparator,
     *         if {@code toElement} does not implement {@link Comparable}).
     *         Implementations may, but are not required to, throw this
     *         exception if {@code toElement} cannot be compared to elements
     *         currently in the set.
     * @throws NullPointerException if {@code toElement} is null and
     *         this set does not permit null elements
     * @throws IllegalArgumentException if this set itself has a
     *         restricted range, and {@code toElement} lies outside the
     *         bounds of the range
     */
    @Override
    public NavigableSet<T> headSet(T toElement, boolean inclusive) {
        int rightCorner = findIndex(toElement);
        ArrayList<T> arrayList = new ArrayList<>();
        if (!inclusive) {
            rightCorner--;
        }
        for (int i = 0; i <= rightCorner; i++) {
            arrayList.add(data.get(i));
        }
        return new MyNavigableSet<>(comparator, arrayList);
    }

    /**
     * Returns a view of the portion of this set whose elements are greater
     * than (or equal to, if {@code inclusive} is true) {@code fromElement}.
     * The returned set is backed by this set, so changes in the returned set
     * are reflected in this set, and vice-versa.  The returned set supports
     * all optional set operations that this set supports.
     *
     * <p>The returned set will throw an {@code IllegalArgumentException}
     * on an attempt to insert an element outside its range.
     *
     * @param fromElement low endpoint of the returned set
     * @param inclusive {@code true} if the low endpoint
     *        is to be included in the returned view
     * @return a view of the portion of this set whose elements are greater
     *         than or equal to {@code fromElement}
     * @throws ClassCastException if {@code fromElement} is not compatible
     *         with this set's comparator (or, if the set has no comparator,
     *         if {@code fromElement} does not implement {@link Comparable}).
     *         Implementations may, but are not required to, throw this
     *         exception if {@code fromElement} cannot be compared to elements
     *         currently in the set.
     * @throws NullPointerException if {@code fromElement} is null
     *         and this set does not permit null elements
     * @throws IllegalArgumentException if this set itself has a
     *         restricted range, and {@code fromElement} lies outside the
     *         bounds of the range
     */
    @Override
    public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        int leftCorner = findIndex(fromElement);
        ArrayList<T> arrayList = new ArrayList<T>() {
            @Override
            public Iterator<T> iterator() {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }
        };
        if (!inclusive) {
            leftCorner++;
        }
        for (int i = leftCorner; i <= size; i++) {
            arrayList.add(data.get(i));
        }
        return new MyNavigableSet<>(comparator, arrayList);
    }

    /**
     * Returns the comparator used to order the elements in this set,
     * or <tt>null</tt> if this set uses the {@linkplain Comparable
     * natural ordering} of its elements.
     *
     * @return the comparator used to order the elements in this set,
     *         or <tt>null</tt> if this set uses the natural ordering
     *         of its elements
     */
    @Override
    public Comparator<? super T> comparator() {
        return comparator;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Equivalent to {@code subSet(fromElement, true, toElement, false)}.
     *
     * @throws ClassCastException       {@inheritDoc}
     * @throws NullPointerException     {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        int leftCorner = findIndex(fromElement);
        int rightCorner = findIndex(toElement);
        ArrayList<T> arrayList = new ArrayList<>();
        for (int i = leftCorner; i <= rightCorner; i++) {
            arrayList.add(data.get(i));
        }
        return new MyNavigableSet<>(comparator, arrayList);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Equivalent to {@code headSet(toElement, false)}.
     *
     * @throws ClassCastException       {@inheritDoc}
     * @throws NullPointerException     {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public SortedSet<T> headSet(T toElement) {
        int rightCorner = findIndex(toElement);
        ArrayList<T> arrayList = new ArrayList<>();
        for (int i = 0; i <= rightCorner; i++) {
            arrayList.add(data.get(i));
        }
        return new MyNavigableSet<>(comparator, arrayList);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Equivalent to {@code tailSet(fromElement, true)}.
     *
     * @throws ClassCastException       {@inheritDoc}
     * @throws NullPointerException     {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        int leftCorner = findIndex(fromElement);
        ArrayList<T> arrayList = new ArrayList<>();
        for (int i = leftCorner; i <= size; i++) {
            arrayList.add(data.get(i));
        }
        return new MyNavigableSet<>(comparator, arrayList);
    }

    /**
     * Returns the first (lowest) element currently in this set.
     *
     * @return the first (lowest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public T first() {
        return data.get(0);
    }

    /**
     * Returns the last (highest) element currently in this set.
     *
     * @return the last (highest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public T last() {
        return data.get(size);
    }

    /**
     * Returns the number of elements in this set (its cardinality).  If this
     * set contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this set (its cardinality)
     */
    @Override
    public int size() {
        return size;
    }

    private int findIndex(T element) {
        int left = 0;
        int right = size;
        int index;
        while (left != right) {
            index = (left + right) / 2;
            if (comparator.compare(data.get(index - 1), element) < 0 && comparator.compare(element, data.get(index - 1)) > 0) {
                return index;
            }
            if (comparator.compare(data.get(index - 1), element) > 0 && comparator.compare(element, data.get(index - 1)) > 0) {
                left = index;
            }
            if (comparator.compare(data.get(index - 1), element) < 0 && comparator.compare(element, data.get(index - 1)) < 0) {
                right = index;
            }
        }
        if (comparator.compare(data.get(right), element) > 0) {
            return right;
        }
        return right - 1;
    }
}


