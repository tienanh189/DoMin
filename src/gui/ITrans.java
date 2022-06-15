package gui;

import logic.Square;

import java.io.IOException;

public interface ITrans {

    Square[][] getListSquare();
    void play(int x, int y) throws IOException;
    void target(int x, int y);
    void restart();
}