package adventofcode2024;

import java.io.PrintStream;

public class GridParser {
	public interface GridCellConsumer {
		void consume(Point p, Character c);
	}
	public interface GridCellProducer {
		Character charAt(Point p);
	}

	final int height;
	final int width;
	private String[] lines;

	public GridParser(String input) {
		lines = input.split("\n");
		this.height = lines.length;
		this.width = lines[0].length();
	}

	public void forEach(GridCellConsumer consumer) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				char c = lines[y].toCharArray()[x];
				consumer.consume(new Point(x,y), c);
			}
		}
	}

	public void printOn(PrintStream out, GridCellProducer p) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) 
				out.print(p.charAt(new Point(x,y)));
			out.println();
		}
	}

}
