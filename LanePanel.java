import javax.swing.JPanel;       // JPanelを使用
import java.awt.Graphics;       // paintComponentでGraphicsを使用
import java.awt.Graphics2D;     // Graphics2Dを使用
import java.awt.Color;          // 色の定義にColorを使用
import java.awt.GradientPaint;  // グラデーションの設定にGradientPaintを使用

///背景クラス
    public class LanePanel extends JPanel {
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            Color DarkGreen = new Color(0, 100, 0);
            
            // グラデーションの設定
            GradientPaint gradient = new GradientPaint(
                0, 0, Color.GREEN, // 開始位置と色
                0, getHeight(), DarkGreen// 終了位置と色
                );
    
                // グラデーションを適用して塗りつぶす
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }