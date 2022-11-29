package helper;

import board.Board;

import java.io.*;

/**
 * Handles reading from and writing to file
 */
public class FileIO {

    /**
     * Reads board from file
     * @param f File to read from
     * @return The board from file
     * @throws IOException If it can't read the board for some reason
     * @throws ClassNotFoundException If it can't find the Board class
     */
    public static Board read(File f) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);

        Board board = (Board) ois.readObject();
        ois.close();
        fis.close();

        return board;
    }

    /**
     * Writes the board to file
     * @param f File to write to
     * @param b The board to write
     * @throws IOException If it can't write for some reason
     */
    public static void write(File f, Board b) throws IOException {
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(b);
        oos.close();
        fos.close();
    }


}
