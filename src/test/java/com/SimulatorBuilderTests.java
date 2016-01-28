package com;

import Helpers.SimulatorBuilder;
import Models.ConditionalStimulus;
import Models.Simulator;
import ViewModels.TrialTableModel;
import org.junit.Test;

/**
 * Created by Rokas on 14/11/2015.
 */
public class SimulatorBuilderTests extends junit.framework.TestCase {

    @Test
    public void testSimBuilder1() throws Exception{
        TrialTableModel tableModel = new TrialTableModel();
        tableModel.setValueAt("group name test", 0, 0);
        tableModel.setValueAt("2AB+", 0, 1);
        Simulator sim = SimulatorBuilder.build(tableModel);
        assertTrue(sim!=null);
        assertTrue(sim.getGroups().size() == 1);
        assertTrue(sim.getGroups().get(0).Name == "group name test");
        assertTrue(sim.getCsParameters().size()==2*3);
        assertTrue(sim.getCsParameters().get(0).CueName == 'A');
        assertTrue(sim.getCsParameters().get(1).CueName == 'B');
        assertTrue(sim.getGroups().get(0).phases.size()==1);
        assertTrue(sim.getGroups().get(0).phases.get(0).trials.size()==2);
        assertFalse(sim.getGroups().get(0).phases.get(0).isRandom());
    }

    @Test
    public void testSimBuilder2() throws Exception{
        TrialTableModel tableModel = new TrialTableModel();
        tableModel.setValueAt("group name test", 0, 0);
        tableModel.setValueAt("2AAB+", 0, 1);
        tableModel.addPhase();
        tableModel.setValueAt("5B-", 0, 3);
        tableModel.setValueAt(true, 0, 4);
        Simulator sim = SimulatorBuilder.build(tableModel);
        assertTrue(sim!=null);
        assertTrue(sim.getGroups().size() == 1);
        assertTrue(sim.getGroups().get(0).Name == "group name test");
        assertTrue(sim.getCsParameters().size()==2*3);
        assertTrue(sim.getCsParameters().get(0).CueName == 'A');
        assertTrue(sim.getCsParameters().get(1).CueName == 'B');
        assertTrue(sim.getGroups().get(0).phases.size() == 2);
        assertTrue(sim.getGroups().get(0).phases.get(0).trials.size() == 2);
        assertFalse(sim.getGroups().get(0).phases.get(0).isRandom());
        assertTrue(sim.getGroups().get(0).phases.get(1).trials.size() == 5);
        assertTrue(sim.getGroups().get(0).phases.get(1).isRandom());
        ConditionalStimulus csB_fromP1 = sim.getGroups().get(0).phases.get(0).trials.get(0).cuesPresent.get(1);
        ConditionalStimulus csB_fromP2 = sim.getGroups().get(0).phases.get(1).trials.get(0).cuesPresent.get(0);
        assertTrue(csB_fromP1 == csB_fromP2); // has to be the same object!
    }

    @Test
    public void testSimBuilder3() throws Exception {
        TrialTableModel tableModel = new TrialTableModel();
        tableModel.addGroup();
        tableModel.addPhase();
        //group 1
        tableModel.setValueAt("group name test", 0, 0);
        tableModel.setValueAt("2AAB+", 0, 1);
        tableModel.setValueAt("5B-", 0, 3);
        //group 2
        tableModel.setValueAt("group2 name test", 1, 0);
        tableModel.setValueAt("ABC+", 1, 1);
        tableModel.setValueAt(true, 1, 2);
        tableModel.setValueAt("5B-", 1, 3);
        Simulator sim = SimulatorBuilder.build(tableModel);

        assertTrue(sim != null);
        assertTrue(sim.getGroups().size() == 2);
        assertTrue(sim.getGroups().get(0).Name == "group name test");
        assertTrue(sim.getGroups().get(1).Name == "group2 name test");
        assertTrue(sim.getCsParameters().size()==3*3);
        assertTrue(sim.getCsParameters().get(0).CueName == 'A');
        assertTrue(sim.getCsParameters().get(1).CueName == 'B');
        //group 1
        assertTrue(sim.getGroups().get(0).phases.size() == 2);
        assertTrue(sim.getGroups().get(0).phases.get(0).trials.size() == 2);
        assertFalse(sim.getGroups().get(0).phases.get(0).isRandom());
        assertTrue(sim.getGroups().get(0).phases.get(1).trials.size() == 5);
        assertFalse(sim.getGroups().get(0).phases.get(1).isRandom());
        //group 2
        assertTrue(sim.getGroups().get(1).phases.get(0).trials.size() == 1);
        assertTrue(sim.getGroups().get(1).phases.get(0).isRandom());
        assertTrue(sim.getGroups().get(1).phases.get(1).trials.size() == 5);
        assertFalse(sim.getGroups().get(1).phases.get(1).isRandom());

        ConditionalStimulus csB_fromP1G1 = sim.getGroups().get(0).phases.get(0).trials.get(0).cuesPresent.get(1);
        ConditionalStimulus csB_fromP2G1 = sim.getGroups().get(0).phases.get(1).trials.get(1).cuesPresent.get(0);
        ConditionalStimulus csB_fromP1G2 = sim.getGroups().get(1).phases.get(0).trials.get(0).cuesPresent.get(1);
        ConditionalStimulus csB_fromP2G2 = sim.getGroups().get(1).phases.get(1).trials.get(1).cuesPresent.get(0);

        //cs is shared among phases but not among groups
        //csparam is shared among groups
        assertTrue(csB_fromP1G1 == csB_fromP2G1);
        assertTrue(csB_fromP1G2 == csB_fromP2G2);

        assertTrue(csB_fromP1G1 != csB_fromP1G2);
        assertTrue(csB_fromP2G1 != csB_fromP2G2);
        assertTrue(csB_fromP1G1.InitialAlphaParameter == csB_fromP1G1.InitialAlphaParameter);
        assertTrue(csB_fromP1G1.SalienceExcitatoryParameter == csB_fromP1G1.SalienceExcitatoryParameter);
        assertTrue(csB_fromP1G1.SalienceInhibitoryParameter == csB_fromP1G1.SalienceInhibitoryParameter);
    }
}
