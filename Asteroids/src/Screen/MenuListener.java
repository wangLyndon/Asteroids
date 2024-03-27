package Screen;

import ucd.comp2011j.engine.MenuCommands;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MenuListener implements MenuCommands, KeyListener {
    private boolean newGame;
    private boolean about;
    private boolean high;
    private boolean menu;
    private boolean exit;


    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'N' || e.getKeyChar() == 'n'){
            newGame = true;
        } else if (e.getKeyChar() == 'A' || e.getKeyChar() == 'a') {
            about = true;
        } else if (e.getKeyChar() == 'H' || e.getKeyChar() == 'h') {
            high = true;
        } else if (e.getKeyChar() == 'M' || e.getKeyChar() == 'm') {
            menu = true;
        } else if (e.getKeyChar() == 'X' || e.getKeyChar() == 'x') {
            exit = true;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public boolean hasPressedNewGame() {
        return newGame;
    }

    @Override
    public boolean hasPressedAboutScreen() {
        return about;
    }

    @Override
    public boolean hasPressedHighScoreScreen() {
        return high;
    }

    @Override
    public boolean hasPressedMenu() {
        return menu;
    }

    @Override
    public boolean hasPressedExit() {
        return exit;
    }

    @Override
    public void resetKeyPresses() {
        newGame = false;
        about = false;
        high = false;
        menu = false;
        exit = false;
    }
}
