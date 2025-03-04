import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

///プレーヤークラス
public class PlayerPanel extends JPanel{
    // メンバ変数
    int x, y, width, height; // x=-1, 0, 1で位置決定
    Image image;
    
    // コンストラクタ
    public PlayerPanel(){
            x = 0; y = 0;
            width = 100; height = 100;
            try{
                image = ImageIO.read(getClass().getResourceAsStream("bike_back1(1).png"));
            }catch(IOException e){
                e.printStackTrace();
            }
            setOpaque(false);//背景を透過するやつ。
        }

        public void paintComponent(Graphics g){
            int offsetX = 250, offsetY = 700;
            super.paintComponent(g);
            if(image!=null){
                g.drawImage(image, x*200+offsetX, y*100+offsetY, getFocusCycleRootAncestor());
            }else{
                g.setColor(Color.CYAN);
                g.fillOval(x*200+offsetX, y*100+offsetY, width, height);
            }
        }
        
        public void updatePlayerPos(int playerPosX) {
            this.x = playerPosX;
            repaint();
        }
}

    

