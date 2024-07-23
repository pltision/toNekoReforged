import net.minecraft.util.Mth;
import org.joml.Matrix2f;
import org.joml.Vector2f;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TailTest extends Panel {
    public static void main(String[] args) {
        Frame frame=new Frame("尾巴!");
        frame.setBounds(500,500,800,500);
        TailTest testPanel=new TailTest();
        frame.add(testPanel);
        frame.add(new ASlider(testPanel),BorderLayout.SOUTH);
        frame.add(new BSlider(testPanel),BorderLayout.NORTH);
        frame.setVisible(true);
    }
    float a=0;//用来控制尾巴震荡 (控制旋转角度的周期增减
    float b=1;//尝试拉直尾巴


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int dx=250,dy=250;
        List<Vector2f> points=new ArrayList<>();
        Vector2f it=new Vector2f();
        Vector2f add=new Vector2f(25,0);
        add.mul(new Matrix2f().rotate(( Mth.sin(a)*Mth.PI/12+Mth.PI*(1/3f) )*b ));
        for(int i=0;i<=10;i++){
            points.add(new Vector2f(it));
            it.add(add);
            add.mul(new Matrix2f().rotate(-Mth.cos(i*Mth.PI/10*(1+Mth.cos(a)/8))*Mth.PI/4*b));
        }
        for(int i=0;i<10;i++){
            g.drawLine((int) (points.get(i).x+dx), (int) (points.get(i).y+dy), (int) (points.get(i+1).x+dx), (int) (points.get(i+1).y+dy));
        }
    }

}

class ASlider extends JSlider{
    TailTest testPanel;
    public ASlider(TailTest testPanel){
        super(0, (int) (4*Math.PI*200));
        this.testPanel=testPanel;
        addChangeListener(e -> {
            testPanel.a=getValue()/200f;
            testPanel.repaint();
        });
    }
}

class BSlider extends JSlider{
    TailTest testPanel;
    public BSlider(TailTest testPanel){
        super(0, 200,200);
        this.testPanel=testPanel;
        addChangeListener(e -> {
            testPanel.b=getValue()/200f;
            testPanel.repaint();
        });
    }
}
