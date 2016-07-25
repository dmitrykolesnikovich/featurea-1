package com.badlogic.gdx.utils;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
public class Array<T> implements Iterable<T> {
	public T[] items;
	public int size;
	public boolean ordered;
	private ArrayIterator iterator;
	public Array () {
		this(true, 16);
	}
	public Array (int capacity) {
		this(true, capacity);
	}
	public Array (boolean ordered, int capacity) {
		this.ordered = ordered;
		items = (T[])new Object[capacity];
	}
	public Array (boolean ordered, int capacity, Class<T> arrayType) {
		this.ordered = ordered;
		items = (T[])java.lang.reflect.Array.newInstance(arrayType, capacity);
	}
	public Array (Class<T> arrayType) {
		this(false, 16, arrayType);
	}
	public Array (Array array) {
		this(array.ordered, array.size, (Class<T>)array.items.getClass().getComponentType());
		size = array.size;
		System.arraycopy(array.items, 0, items, 0, size);
	}
	public Array (T[] array) {
		this(true, array);
	}
	public Array (boolean ordered, T[] array) {
		this(ordered, array.length, (Class)array.getClass().getComponentType());
		size = array.length;
		System.arraycopy(array, 0, items, 0, size);
	}
	public void add (T value) {
		T[] items = this.items;
		if (size == items.length) items = resize(Math.max(8, (int)(size * 1.75f)));
		items[size++] = value;
	}
	public void addAll (Array array) {
		addAll(array, 0, array.size);
	}
	public void addAll (Array array, int offset, int length) {
		if (offset + length > array.size)
			throw new IllegalArgumentException("offset + length must be <= size: " + offset + " + " + length + " <= " + array.size);
		addAll((T[])array.items, offset, length);
	}
	public void addAll (T[] array) {
		addAll(array, 0, array.length);
	}
	public void addAll (T[] array, int offset, int length) {
		T[] items = this.items;
		int sizeNeeded = size + length - offset;
		if (sizeNeeded >= items.length) items = resize(Math.max(8, (int)(sizeNeeded * 1.75f)));
		System.arraycopy(array, offset, items, size, length);
		size += length;
	}
	public T get (int index) {
		if (index >= size) throw new IndexOutOfBoundsException(String.valueOf(index));
		return items[index];
	}
	public void set (int index, T value) {
		if (index >= size) throw new IndexOutOfBoundsException(String.valueOf(index));
		items[index] = value;
	}
	public void insert (int index, T value) {
		T[] items = this.items;
		if (size == items.length) items = resize(Math.max(8, (int)(size * 1.75f)));
		if (ordered)
			System.arraycopy(items, index, items, index + 1, size - index);
		else
			items[size] = items[index];
		size++;
		items[index] = value;
	}
	public boolean contains (T value, boolean identity) {
		T[] items = this.items;
		int i = size - 1;
		if (identity || value == null) {
			while (i >= 0)
				if (items[i--] == value) return true;
		} else {
			while (i >= 0)
				if (value.equals(items[i--])) return true;
		}
		return false;
	}
	public int indexOf (T value, boolean identity) {
		T[] items = this.items;
		if (identity || value == null) {
			for (int i = 0, n = size; i < n; i++)
				if (items[i] == value) return i;
		} else {
			for (int i = 0, n = size; i < n; i++)
				if (value.equals(items[i])) return i;
		}
		return -1;
	}
	public boolean removeValue (T value, boolean identity) {
		T[] items = this.items;
		if (identity || value == null) {
			for (int i = 0, n = size; i < n; i++) {
				if (items[i] == value) {
					removeIndex(i);
					return true;
				}
			}
		} else {
			for (int i = 0, n = size; i < n; i++) {
				if (value.equals(items[i])) {
					removeIndex(i);
					return true;
				}
			}
		}
		return false;
	}
	public T removeIndex (int index) {
		if (index >= size) throw new IndexOutOfBoundsException(String.valueOf(index));
		T[] items = this.items;
		T value = (T)items[index];
		size--;
		if (ordered)
			System.arraycopy(items, index + 1, items, index, size - index);
		else
			items[index] = items[size];
		items[size] = null;
		return value;
	}
	public T pop () {
		--size;
		T item = items[size];
		items[size] = null;
		return item;
	}
	public T peek () {
		return items[size - 1];
	}
	public void clear () {
		T[] items = this.items;
		for (int i = 0, n = size; i < n; i++)
			items[i] = null;
		size = 0;
	}
	public void shrink () {
		resize(size);
	}
	public T[] ensureCapacity (int additionalCapacity) {
		int sizeNeeded = size + additionalCapacity;
		if (sizeNeeded >= items.length) resize(Math.max(8, sizeNeeded));
		return items;
	}
	protected T[] resize (int newSize) {
		T[] items = this.items;
		T[] newItems = (T[])java.lang.reflect.Array.newInstance(items.getClass().getComponentType(), newSize);
		System.arraycopy(items, 0, newItems, 0, Math.min(items.length, newItems.length));
		this.items = newItems;
		return newItems;
	}
	public void sort () {
		Sort.instance().sort(items, 0, size);
	}
	public void sort (Comparator<T> comparator) {
		Sort.instance().sort(items, comparator, 0, size);
	}
	public void reverse () {
		for (int i = 0, lastIndex = size - 1, n = size / 2; i < n; i++) {
			int ii = lastIndex - i;
			T temp = items[i];
			items[i] = items[ii];
			items[ii] = temp;
		}
	}
	public void shuffle () {
		for (int i = size - 1; i >= 0; i--) {
			int ii = MathUtils.random(i);
			T temp = items[i];
			items[i] = items[ii];
			items[ii] = temp;
		}
	}
	public Iterator<T> iterator () {
		if (iterator == null) iterator = new ArrayIterator(this);
		iterator.index = 0;
		return iterator;
	}
	public void truncate (int newSize) {
		if (size <= newSize) return;
		for (int i = newSize; i < size; i++)
			items[i] = null;
		size = newSize;
	}
	public T random () {
		if (size == 0) return null;
		return items[MathUtils.random(0, size - 1)];
	}
	public T[] toArray () {
		return (T[])toArray(items.getClass().getComponentType());
	}
	public <V> V[] toArray (Class<V> type) {
		V[] result = (V[])java.lang.reflect.Array.newInstance(type, size);
		System.arraycopy(items, 0, result, 0, size);
		return result;
	}
	public String toString () {
		if (size == 0) return "[]";
		T[] items = this.items;
		StringBuilder buffer = new StringBuilder(32);
		buffer.append('[');
		buffer.append(items[0]);
		for (int i = 1; i < size; i++) {
			buffer.append(", ");
			buffer.append(items[i]);
		}
		buffer.append(']');
		return buffer.toString();
	}
	public String toString (String separator) {
		if (size == 0) return "";
		T[] items = this.items;
		StringBuilder buffer = new StringBuilder(32);
		buffer.append(items[0]);
		for (int i = 1; i < size; i++) {
			buffer.append(separator);
			buffer.append(items[i]);
		}
		return buffer.toString();
	}
	static public class ArrayIterator<T> implements Iterator<T> {
		private final Array<T> array;
		int index;
		public ArrayIterator (Array<T> array) {
			this.array = array;
		}
		public boolean hasNext () {
			return index < array.size;
		}
		public T next () {
			if (index >= array.size) throw new NoSuchElementException(String.valueOf(index));
			return array.items[index++];
		}
		public void remove () {
			index--;
			array.removeIndex(index);
		}
		public void reset () {
			index = 0;
		}
	}
	static public class ArrayIterable<T> implements Iterable<T> {
		private ArrayIterator<T> iterator;
		public ArrayIterable (Array<T> array) {
			iterator = new ArrayIterator<T>(array);
		}
		@Override
		public Iterator<T> iterator () {
			iterator.reset();
			return iterator;
		}
	}
}
