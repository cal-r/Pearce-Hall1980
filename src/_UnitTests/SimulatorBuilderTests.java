package _UnitTests;

import Helpers.SimulatorBuilder;
import Models.ConditionalStimulus;
import Models.Simulator;
import ViewModels.TrailTableModel;
import org.junit.Test;

/**
 * Created by Rokas on 14/11/2015.
 */
public class SimulatorBuilderTests extends junit.framework.TestCase {

    @Test
    public void testSimBuilder1() throws Exception{
        TrailTableModel tableModel = new TrailTableModel();
        tableModel.setValueAt("group name test", 0, 0);
        tableModel.setValueAt("2AB+", 0, 1);
        Simulator sim = SimulatorBuilder.build(tableModel);
        assertTrue(sim!=null);
        assertTrue(sim.getGroups().size() == 1);
        assertTrue(sim.getGroups().get(0).Name == "group name test");
        assertTrue(sim.getCsParameters().size()==6);
        assertTrue(sim.getCsParameters().get(0).CueName == 'A');
        assertTrue(sim.getCsParameters().get(1).CueName == 'B');
        assertTrue(sim.getGroups().get(0).phases.size()==1);
        assertTrue(sim.getGroups().get(0).phases.get(0).trails.size()==2);
    }

    @Test
    public void testSimBuilder2() throws Exception{
        TrailTableModel tableModel = new TrailTableModel();
        tableModel.setValueAt("group name test", 0, 0);
        tableModel.setValueAt("2AAB+", 0, 1);
        tableModel.addPhase();
        tableModel.setValueAt("5B-", 0, 2);
        Simulator sim = SimulatorBuilder.build(tableModel);
        assertTrue(sim!=null);
        assertTrue(sim.getGroups().size() == 1);
        assertTrue(sim.getGroups().get(0).Name == "group name test");
        assertTrue(sim.getCsParameters().size()==6);
        assertTrue(sim.getCsParameters().get(0).CueName == 'A');
        assertTrue(sim.getCsParameters().get(1).CueName == 'B');
        assertTrue(sim.getGroups().get(0).phases.size() == 2);
        assertTrue(sim.getGroups().get(0).phases.get(0).trails.size() == 2);
        assertTrue(sim.getGroups().get(0).phases.get(1).trails.size() == 5);
        ConditionalStimulus csB_fromP1 = sim.getGroups().get(0).phases.get(0).trails.get(0).cuesPresent.get(1);
        ConditionalStimulus csB_fromP2 = sim.getGroups().get(0).phases.get(0).trails.get(1).cuesPresent.get(0);
        assertTrue(csB_fromP1 == csB_fromP2); // has to be the same object!
    }

//    @Test
//    public void testSimBuilder3() throws Exception {
//
//    }
}
