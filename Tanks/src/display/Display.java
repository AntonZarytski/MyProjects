package display;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public abstract class Display {
    private static boolean created = false;
    private static JFrame window;
    private static Canvas content; // рисовать

    private static BufferedImage buffer;
    private static int[] bufferData;
    private static Graphics bufferGraphics;
    private static int clearColor;

    //temp
    private  static float delta = 0;
    //temp end
    public static void create(int width, int height, String tittle, int _clearColor){

        if(created)
            return;

        window = new JFrame(tittle);
        content = new Canvas (  );

        Dimension size = new Dimension(width, height);
        content.setPreferredSize(size);
        //content.setBackground(Color.PINK);
        window.setResizable(false);
        window.getContentPane().add(content);//возвр только внутре чать окна(меню и кнопки не перекрывают)
        window.pack();// изменить размер под размер content
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);

        buffer = new BufferedImage ( width, height, BufferedImage.TYPE_INT_ARGB );
        bufferData = ((DataBufferInt)buffer.getRaster ().getDataBuffer ()).getData ();//возвращает массив инт
        bufferGraphics = buffer.getGraphics ();// объект умеющий рисовать фишуры и тд
        clearColor = _clearColor;
        created=true;
    }
    public static void clear(){// очищает полностью поле
        Arrays.fill(bufferData, clearColor);
    }
    public static void render(){
        bufferGraphics.setColor ( new Color (0xff0000ff) );
        bufferGraphics.fillOval (((int)( 350+(Math.sin ( delta )*200))),250,100,100 );
        delta += 0.02f;
    }
    public static void swapBuffers(){
        Graphics g = content.getGraphics ();
        g.drawImage ( buffer, 0,0, null );
    }
}
