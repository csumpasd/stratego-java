package helper;

import board.Board;

import java.io.*;

public class FileIO {

    public static Board read(File f) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);

        Board board = (Board) ois.readObject();
        ois.close();
        fis.close();

        return board;
    }

    public static void write(File f, Board b) throws IOException {
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(b);
        oos.close();
        fos.close();
    }
}
