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
public class Pivot {
    private Node pivot;
    private Node pivotEnd;
    
    public Pivot(Node father, AssetManager assetManager, int i, float beta)
    {
        Box mesh = new Box(1.0f, 0.1f, 0.1f);
        Geometry geo = new Geometry("line", mesh);
       
        Material mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat3.setColor("Color", ColorRGBA.Green);
        geo.setMaterial(mat3);
        geo.setLocalTranslation(1.0f, 0.0f, 0.0f);
        
        pivot = new Node();
        pivotEnd = new Node();
        pivotEnd.setLocalTranslation(2.0f, 0.0f, 0.0f);
        
        pivot.attachChild(geo);
        pivot.attachChild(pivotEnd);

        pivot.setLocalScale(1.0f / (i*beta + 1));
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
