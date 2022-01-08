package test;

import api.DWG;
import api.EdgeData;
import api.NodeData;
import api.impNodeData;
import gameClient.Pokemon;
import gameClient.util.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PokemonTest {

    @Test
    void get_edge() {
        DWG w = new DWG();
        w.addNode(new impNodeData());
        for (int i = 1; i < 100; i++){
            NodeData n = new impNodeData();
            w.addNode(n);
            w.connect(i-1, i ,i);
        }
        Pokemon p  = new Pokemon(new Point3D(1,2,3),2,2,2,w.getEdge(0,1));
        assertEquals(p.get_edge().getSrc() , 0);
        assertEquals(p.get_edge().getDest() , 1);
    }

    @Test
    void get_value() {
        Pokemon p= new Pokemon(new Point3D(1,2,3) ,2,2,2 , new EdgeData(new impNodeData(), new impNodeData(),1));
        System.out.println(p.get_value()==2);
        assertEquals(p.get_value() , 2);

    }
}