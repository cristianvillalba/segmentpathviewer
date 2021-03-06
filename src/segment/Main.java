package segment;

import com.jme3.app.SimpleApplication;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Quad;
import com.jme3.system.JmeSystem;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ColorSpace;
import com.jme3.texture.image.ImageRaster;
import com.jme3.util.BufferUtils;
import com.jme3.util.TempVars;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener{
    public float scale = 1.0f;
    private ArrayList<Pivot> allpivots;
    
    private Texture texture;
    private ImageRaster imageRaster;
    private Image imagefinal;
    public static int width = 1024;
    public static int height = 1024;
    
    private float lasti = 0.0f;
    private int cross = 1;
    private int segmentnumber = 15;
    private ArrayList<Float> allcross = new ArrayList<Float>();
    
    private ArrayList<Float> rotspeedconstant = new ArrayList<Float>();
    private ArrayList<Float> rotspeedfaster = new ArrayList<Float>();
    private ArrayList<Float> rotspeedslower = new ArrayList<Float>();
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {     
        allpivots = new ArrayList<Pivot>();
        
        initTexture();
        InitAxis();
        InitSegments();
        
        InitSpeeds();
        
        InitViewer();
        
        registerInput();
    }
    
    private void InitSpeeds()
    {
        rotspeedconstant.add(0.5f);
        rotspeedconstant.add(0.5f);
        rotspeedconstant.add(0.5f);
        
        rotspeedfaster.add(0.5f);
        rotspeedfaster.add(0.75f);
        rotspeedfaster.add(1.0f);
        
        rotspeedslower.add(0.5f);
        rotspeedslower.add(0.4f);
        rotspeedslower.add(0.3f);
    }
    
    private void InitViewer()
    {
        Material matl = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matl.setTexture("ColorMap",texture);
        //matl.setColor("Color", ColorRGBA.Blue);
        
        Geometry graphRect = new Geometry("GraphRect", new Quad(20, 20));
        graphRect.setMaterial(matl);
        graphRect.setLocalTranslation(-10, -10, 0);
        
        rootNode.attachChild(graphRect);
    }
    
    private void initTexture(){
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
        Image image = new Image(Image.Format.RGBA8, width, height, buffer, ColorSpace.sRGB);

        // assign the image to a texture...
        texture = new Texture2D(image);
        imagefinal = image;

        // set some pixel colors...
        imageRaster = ImageRaster.create(image);
    }
    
    public void registerInput()
    {
        inputManager.addMapping("save",new KeyTrigger(keyInput.KEY_1));
        inputManager.addMapping("print",new KeyTrigger(keyInput.KEY_2));
        inputManager.addListener(this, "save");
        inputManager.addListener(this, "print");
    }
    
    private Geometry GenerateLine(float r0, float i0, float r1, float i1, int index)
    {
        Vector3f origin = new Vector3f(r0*scale, i0*scale, 0f);
        Vector3f destination = new Vector3f(r1*scale, i1*scale, 0f);
        
        Line linexp = new Line(origin, destination);
        
        Geometry linexpos = new Geometry("etaline" + index, linexp);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        
        if (index != 666){
            mat1.setColor("Color", ColorRGBA.Green);
        }
        else
        {
            mat1.setColor("Color", ColorRGBA.Red);
        }
        linexpos.setMaterial(mat1);
        
        return linexpos;
    }

    private void InitAxis()
    {
        Line linexp = new Line(Vector3f.ZERO, Vector3f.UNIT_X.mult(500));
        
        Geometry linexpos = new Geometry("xaxispos", linexp);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.White);
        linexpos.setMaterial(mat1);
        
        Line linexn = new Line(Vector3f.ZERO, Vector3f.UNIT_X.mult(-500));
        
        Geometry linexneg = new Geometry("xaxisneg", linexn);
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("Color", ColorRGBA.White);
        linexneg.setMaterial(mat2);
        
        Line lineyp = new Line(Vector3f.ZERO, Vector3f.UNIT_Y.mult(500));
        
        Geometry lineypos = new Geometry("yaxispos", lineyp);
        Material mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat3.setColor("Color", ColorRGBA.White);
        lineypos.setMaterial(mat3);
        
        Line lineyn = new Line(Vector3f.ZERO, Vector3f.UNIT_Y.mult(-500));
        
        Geometry lineyneg = new Geometry("yaxisneg", lineyn);
        Material mat4 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat4.setColor("Color", ColorRGBA.White);
        lineyneg.setMaterial(mat4);
        
        Node realaxis = new Node();
        realaxis.attachChild(linexpos);
        realaxis.attachChild(linexneg);
        realaxis.attachChild(lineypos);
        realaxis.attachChild(lineyneg);
        
        rootNode.attachChild(realaxis);
    }
    
    
    
    private void InitSegments()
    {
        Node prevnode = rootNode;
        
        for (int i = 0; i < segmentnumber ; i++)
        {
            Pivot pivot = new Pivot(prevnode, assetManager, i, 0.0f); //last parameter is segment size
            prevnode = pivot.GetPivotEnd();
            
            allpivots.add(pivot);
        }
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        for(int i = 1; i < allpivots.size(); i++)
        {
            Pivot piv = allpivots.get(i);
            
            Node pivotNode = piv.GetMainPivot();
           
                        
            //pivotNode.rotate(0.0f, 0.0f, -tpf*0.5f);//constant rotation on each segment
            //pivotNode.rotate(0.0f, 0.0f, -tpf*(0.5f+i*0.1f));//faster rotation on each segment
            //pivotNode.rotate(0.0f, 0.0f, -tpf*(0.5f-i*0.1f));//slower rotation on each segment
            
            //System.out.println("log: " + Math.log(i));
            pivotNode.rotate(0.0f, 0.0f, (float)(-tpf*1.0f*Math.log(i + 1))); //attempt to draw ETA based on segment rotation speed (with a ln formula)
            
            //hardcoded speeds
            //pivotNode.rotate(0.0f, 0.0f, -tpf*rotspeedconstant.get(i));
            //pivotNode.rotate(0.0f, 0.0f, -tpf*rotspeedfaster.get(i));
            //pivotNode.rotate(0.0f, 0.0f, -tpf*rotspeedslower.get(i));
            
            if (i == (allpivots.size() - 1)) //only draw last segment
            {
                Node pivotEnd = piv.GetPivotEnd();
                int x = (int)(pivotEnd.getWorldTranslation().x * 50);
                int y = (int)(pivotEnd.getWorldTranslation().y * 50);
                
                if (lasti*pivotEnd.getWorldTranslation().y < 0.0f)
                {
                    cross++;
                    System.out.println("cross :" + cross);
                    
                    allcross.add(pivotEnd.getWorldTranslation().x);
                }

                x += width / 2 ;
                y += height / 2 ;

                if (x >= 0 && x < width && y >= 0 && y < height)
                {
                    imageRaster.setPixel(x, y, ColorRGBA.Red);
                }
                
                lasti = pivotEnd.getWorldTranslation().y;
            }
            
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.contains("save") && isPressed)
        {
            File newfile = new File("patheta.png");
            try{
                savePng(newfile, imagefinal);
            }
            catch(Exception e)
            {
                
            }
        }
        
        if (name.contains("print") && isPressed)
        {
            for (int i = 0; i < allcross.size(); i++)
            {
                System.out.println(allcross.get(i));
            }
        }
    }
    
    public void savePng( File f, Image img ) throws IOException {
        OutputStream out = new FileOutputStream(f);
        try {            
            JmeSystem.writeImageFile(out, "png", img.getData(0), img.getWidth(), img.getHeight());  
        } finally {
            out.close();
        }             
    }
}

