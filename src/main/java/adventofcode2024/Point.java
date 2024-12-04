package adventofcode2024;

import java.util.Objects;

public class Point {

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
	private int x;
	private int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	Point n() {return new Point(x, y-1);}
	Point ne() {return new Point(x+1, y-1);}
	Point e() {return new Point(x+1, y);}
	Point se() {return new Point(x+1, y+1);}
	Point s() {return new Point(x, y+1);}
	Point sw() {return new Point(x-1, y+1);}
	Point w() {return new Point(x-1, y);}
	Point nw() {return new Point(x-1, y-1);}

	static Point toN(Point p) {return p.n();}
	static Point toNe(Point p) {return p.ne();}
	static Point toE(Point p) {return p.e();}
	static Point toSe(Point p) {return p.se();}
	static Point toS(Point p) {return p.s();}
	static Point toSw(Point p) {return p.sw();}
	static Point toW(Point p) {return p.w();}
	static Point toNw(Point p) {return p.nw();}
}
