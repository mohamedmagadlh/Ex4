package test;

import api.DWG;
import api.EdgeData;
import api.NodeData;
import api.impNodeData;
import gameClient.Agent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgentTest {

    @Test
    void isMoving() {
        DWG w = new DWG();
        w.addNode(new impNodeData());
        for (int i = 1; i < 100; i++) {
            NodeData n = new impNodeData();
            w.addNode(n);
            w.connect(i-1, i, i);
        }
        Agent agent = new Agent(w, 2);
        agent.set_curr_edge(new EdgeData(w.getNode(0) , w.getNode(1),1));
        assertTrue(agent.isMoving());

    }

    @Test
    void getId() {
        DWG w = new DWG();
        w.addNode(new impNodeData());
        for (int i = 1; i < 100; i++) {
            NodeData n = new impNodeData();
            w.addNode(n);
            w.connect(i-1, i, i);
        }
        Agent agent = new Agent(w, 2);
        assertEquals(agent.getId(),-1);

    }
}