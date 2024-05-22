/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package segment;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Line;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author cvillalba
 */
public class SaveToFile extends SimpleApplication implements ActionListener{
    private float calcscale = 1.0f;
    private float plotscalex = 1.0f;
    private float plotscaley = 1.0f;
    private float speed = 0.01f;
    private boolean pause = false;
    private float currentTime = 0f;
    private float refreshTime = 0.02f;
    private float initialImaginary = 0.0f;
    private BitmapText hudTextReal;
    private BitmapText hudTextImg;
    
    public static void main(String[] args) {
        SaveToFile app = new SaveToFile();
        app.start();
    }
    
    private void WriteToFile(String name, String data){
        try {
            FileWriter myWriter = new FileWriter(name, true);
            myWriter.write(data);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
    
    public void ETAtoFile()
    {
        System.out.println("ETA save to file...");
        System.out.println("v 0.0.1");
        
        int sign = 1;
        StringBuilder sbr = new StringBuilder();
        StringBuilder sbi = new StringBuilder();
        
        Complex svalue = new Complex(0.75, 14.1347251417346937904572519835624702707842571156992431);
        
        Complex finalsum = new Complex(0,0);
        for (int i = 1; i < 3000; i++)
        {
            Complex number = new Complex(i, 0);
            number = number.pow(svalue);
            
            Complex divider = new Complex(sign, 0);
            
            Complex data = divider.divide(number);
            
            sbr.append(data.getReal() + ",");
            sbi.append(data.getImaginary()+ ",");
            
            finalsum = finalsum.add(data);
            sign *= -1;
        }
        
        this.WriteToFile("etasave.csv", sbr.toString() + "\n");
        this.WriteToFile("etasave.csv", sbi.toString() + "\n");
        
        this.WriteToFile("etasave.csv", "Final sum r:" + finalsum.getReal() + "\n");
       this.WriteToFile("etasave.csv", "Final sum i:" + finalsum.getImaginary() + "\n");
    }

    private void InitAxis()
    {
        Line linexp = new Line(Vector3f.ZERO, Vector3f.UNIT_X.mult(500));
        
        Geometry linexpos = new Geometry("xaxispos", linexp);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Gray);
        linexpos.setMaterial(mat1);
        
        Line linexn = new Line(Vector3f.ZERO, Vector3f.UNIT_X.mult(-500));
        
        Geometry linexneg = new Geometry("xaxisneg", linexn);
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("Color", ColorRGBA.Gray);
        linexneg.setMaterial(mat2);
        
        Line lineyp = new Line(Vector3f.ZERO, Vector3f.UNIT_Y.mult(500));
        
        Geometry lineypos = new Geometry("yaxispos", lineyp);
        Material mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat3.setColor("Color", ColorRGBA.Gray);
        lineypos.setMaterial(mat3);
        
        Line lineyn = new Line(Vector3f.ZERO, Vector3f.UNIT_Y.mult(-500));
        
        Geometry lineyneg = new Geometry("yaxisneg", lineyn);
        Material mat4 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat4.setColor("Color", ColorRGBA.Gray);
        lineyneg.setMaterial(mat4);
        
        Node realaxis = new Node();
        realaxis.attachChild(linexpos);
        realaxis.attachChild(linexneg);
        realaxis.attachChild(lineypos);
        realaxis.attachChild(lineyneg);
        
        rootNode.attachChild(realaxis);
    }
    
    private Geometry GenerateLine(float r0, float i0, float r1, float i1, int index, int color)
    {
        float localscale = calcscale;
        
        Vector3f origin = new Vector3f(r0*localscale, i0*localscale, 0f);
        Vector3f destination = new Vector3f(r1*localscale, i1*localscale, 0f);
        
        Line linexp = new Line(origin, destination);
        
        Geometry linexpos = new Geometry("etaline" + index, linexp);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        
        if (color == 0){
            mat1.setColor("Color", ColorRGBA.Green);
        }
        else
        {
            mat1.setColor("Color", ColorRGBA.Red);
        }
        linexpos.setMaterial(mat1);
        
        return linexpos;
    }
    
    @Override
    public void simpleInitApp() {
        InitAxis();
        
        InitDisplay();
        
        registerInput();
        
        getCamera().setLocation(new Vector3f(0,0,100));
        getFlyByCamera().setMoveSpeed(50.0f);
        
        CalculateGraph(0.75f, 14.1347251417346937904572519835624702707842571156992431f);
    }
    
    private void InitDisplay()
    {
        hudTextReal = new BitmapText(guiFont, false);
        hudTextReal.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        hudTextReal.setColor(ColorRGBA.Blue);                             // font color
        hudTextReal.setText("0");             // the text
        hudTextReal.setLocalTranslation(300, hudTextReal.getLineHeight(), 0); // position
        guiNode.attachChild(hudTextReal);
        
        hudTextImg = new BitmapText(guiFont, false);
        hudTextImg.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        hudTextImg.setColor(ColorRGBA.Blue);                             // font color
        hudTextImg.setText("1");             // the text
        hudTextImg.setLocalTranslation(350, hudTextImg.getLineHeight(), 0); // position
        guiNode.attachChild(hudTextImg);
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        currentTime += tpf;
        
        if (currentTime > refreshTime && !pause)
        {
            initialImaginary += speed;
            
            hudTextReal.setText("" + new Float(0.5f).toString());
            hudTextImg.setText("" + new Float(initialImaginary).toString());
            CalculateGraph(0.5f, initialImaginary);
            currentTime = 0;
        }
    }
    
    private void CalculateGraph(float r, float im) {
        rootNode.detachAllChildren();
        InitAxis();
        
        //Complex svalue = new Complex(0.75, 14.1347251417346937904572519835624702707842571156992431);
        //Complex svalue = new Complex(0.75, 14040.1347251417346937904572519835624702707842571156992431);
        Complex svalue = new Complex(r, im);
        
        
        int sign = 1;

        Complex previouspoint = null;
        Complex previousplotx = new Complex(0,0);
        Complex previousploty = new Complex(0,0);
         
        for (int i = 1; i < 1400; i++)
        {
            Complex number = new Complex(i, 0);
            number = number.pow(svalue);
            
            Complex divider = new Complex(sign, 0);
            
            Complex data = divider.divide(number);
           
            if (previouspoint != null)
            {
                Complex newplotx = new Complex(new Double(i).doubleValue()/plotscalex,(data.getReal() - previouspoint.getReal())*plotscaley);
                rootNode.attachChild(GenerateLine((float)previousplotx.getReal(), (float)previousplotx.getImaginary(), (float)newplotx.getReal(), (float)newplotx.getImaginary(), i, 0));
                previousplotx = newplotx;
                
                Complex newploty = new Complex(new Double(i).doubleValue()/plotscalex,(data.getImaginary() - previouspoint.getImaginary())*plotscaley);
                rootNode.attachChild(GenerateLine((float)previousploty.getReal(), (float)previousploty.getImaginary(), (float)newploty.getReal(), (float)newploty.getImaginary(), i, 1));
                previousploty = newploty;
                
            }
            previouspoint = data;
            sign *= -1;
        }
    }
    
    public void registerInput()
    {
        inputManager.addMapping("speedup",new KeyTrigger(keyInput.KEY_R));//save image
        inputManager.addMapping("speeddown",new KeyTrigger(keyInput.KEY_F));//print data into console
        inputManager.addMapping("scalexup", new KeyTrigger(keyInput.KEY_T));//save lenght of last pivot into file
        inputManager.addMapping("scalexdown", new KeyTrigger(keyInput.KEY_G));//clean image
        inputManager.addMapping("scaleyup", new KeyTrigger(keyInput.KEY_Y));//clean image
        inputManager.addMapping("scaleydown", new KeyTrigger(keyInput.KEY_H));//clean image
        inputManager.addMapping("pause", new KeyTrigger(keyInput.KEY_SPACE));//pause simulation
        inputManager.addListener(this, "speedup");
        inputManager.addListener(this, "speeddown");
        inputManager.addListener(this, "scalexup");
        inputManager.addListener(this, "scalexdown");
        inputManager.addListener(this, "scaleyup");
        inputManager.addListener(this, "scaleydown");
        inputManager.addListener(this, "pause");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("pause") && isPressed)
        {
            pause = !pause;
        }
        
        if (name.equals("speedup") && isPressed)
        {
            speed += 0.001f;
        }
        
        if (name.equals("speeddown") && isPressed)
        {
            speed -= 0.001f;
            
            if (speed < 0.0f)
                speed = 0.0f;
        }
        
        if (name.equals("scalexup") && isPressed)
        {
            plotscalex += 1.0f;
        }
        
        if (name.equals("scalexdown") && isPressed)
        {
            plotscalex -= 1.0f;
            
            if (plotscalex < 1.0f)
                plotscalex = 1.0f;
        }
        
        if (name.equals("scaleyup") && isPressed)
        {
            plotscaley += 1.0f;
        }
        
        if (name.equals("scaleydown") && isPressed)
        {
            plotscaley -= 1.0f;
            
             if (plotscaley < 1.0)
                plotscaley = 1.00f;
        }
    }
}
