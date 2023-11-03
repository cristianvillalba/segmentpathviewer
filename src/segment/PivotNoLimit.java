/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segment;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Cristian.Villalba
 */
public class PivotNoLimit {
    private Node pivot;
    private Node pivotEnd;
    
    public PivotNoLimit(Node father, AssetManager assetManager, int i, float beta)
    {
        //Box mesh = new Box(1.0f, 0.1f, 0.1f); //a segment with an extension of 2.0
        Box mesh = new Box(0.5f, 0.1f, 0.1f); //a segment with an extension of 1.0
        Geometry geo = new Geometry("line", mesh);
       
        Material mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat3.setColor("Color", ColorRGBA.Green);
        geo.setMaterial(mat3);
        //geo.setLocalTranslation(1.0f, 0.0f, 0.0f);//a segment with an extension of 2.0
        geo.setLocalTranslation(0.5f, 0.0f, 0.0f);//a segment with an extension of 1.0
        
        pivot = new Node();
        pivotEnd = new Node();
        //pivotEnd.setLocalTranslation(2.0f, 0.0f, 0.0f);//move the segment with length 2.0 to the end
        pivotEnd.setLocalTranslation(1.0f, 0.0f, 0.0f);//move the segment with length 1.0 to the end
        
        pivot.attachChild(geo);
        pivot.attachChild(pivotEnd);

        //use segment proportion equal to ETA function
        float base = 1.0f/(i + 1.0f);
        float segmentsize = (float )Math.pow(base, 0.5f);//this will emulate eta, on 0.5 will be the "critical line"
        pivot.setLocalScale(segmentsize);
        
        //this will also emulate eta, changing rotation will set the initial conditions of all the segments -1 +1 ... segments
        if (( i % 2) == 1)
        {
            pivot.rotate(0, 0, (float)Math.PI);
        }
        
        father.attachChild(pivot);
    }
    
    public Node GetPivotEnd()
    {
        return pivotEnd;
    }
    
    public Node GetMainPivot()
    {
        return pivot;
    }
}
