/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package segment;

import com.jme3.app.SimpleApplication;
import com.jme3.input.controls.ActionListener;
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
    private float scale = 100.0f;
    private float plotscale = 10.0f;
    
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
        float localscale = scale;
        
        localscale = 5.0f;
        
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
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        rootNode.detachAllChildren();
        InitAxis();
        
        //Complex svalue = new Complex(0.75, 14.1347251417346937904572519835624702707842571156992431);
        Complex svalue = new Complex(0.75, 14040.1347251417346937904572519835624702707842571156992431);
        
        int sign = 1;

        Complex previouspoint = null;
        Complex previousplotx = new Complex(0,0);
        Complex previousploty = new Complex(0,0);
         
        for (int i = 1; i < 500; i++)
        {
            Complex number = new Complex(i, 0);
            number = number.pow(svalue);
            
            Complex divider = new Complex(sign, 0);
            
            Complex data = divider.divide(number);
           
            if (previouspoint != null)
            {
                Complex newplotx = new Complex(new Double(i).doubleValue()/plotscale,(data.getReal() - previouspoint.getReal())*plotscale);
                rootNode.attachChild(GenerateLine((float)previousplotx.getReal(), (float)previousplotx.getImaginary(), (float)newplotx.getReal(), (float)newplotx.getImaginary(), i, 0));
                previousplotx = newplotx;
                
                Complex newploty = new Complex(new Double(i).doubleValue()/plotscale,(data.getImaginary() - previouspoint.getImaginary())*plotscale);
                rootNode.attachChild(GenerateLine((float)previousploty.getReal(), (float)previousploty.getImaginary(), (float)newploty.getReal(), (float)newploty.getImaginary(), i, 1));
                previousploty = newploty;
                
            }
            previouspoint = data;
            sign *= -1;
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
