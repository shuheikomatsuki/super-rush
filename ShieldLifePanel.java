import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
//java.awt.Colorなし

public class ShieldLifePanel extends JPanel {
    Image image;

    public ShieldLifePanel() {
        try{
            image = ImageIO.read(getClass().getResourceAsStream("shieldmini.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
        setOpaque(false); // 背景を透明にする
    }

    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, getFocusCycleRootAncestor()); // 画像を表示
    }

    public void showShieldLife() {
        setVisible(true);
    }

    public void hideShieldLife() {
        setVisible(false);
    }
}