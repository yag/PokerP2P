package core;

public class Pair<T1, T2> {
	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}
	public T1 getFirst() {
		return first;
	}
	public void setFirst(T1 first) {
		this.first = first;
	}
	public T2 getSecond() {
		return second;
	}
	public void setSecond(T2 second) {
		this.second = second;
	}
	T1 first;
	T2 second;
}
