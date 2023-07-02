import javax.swing.*;

class Window extends JFrame {
    // Makes the window that any of the pages will be displayed on.
    Window(String title, int w, int h) {
        this.setTitle(title);
        this.setSize(w, h);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
