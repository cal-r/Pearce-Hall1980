package com;

import Constants.DefaultValuesConstants;
import Helpers.ModelBuilding.SimulatorBuilder;
import Models.Stimulus.ConditionalStimulus;
import Models.Simulator;
import Models.Stimulus.ContextStimulus;
import ViewModels.TableModels.TrialTableModel;
import _from_RW_simulator.ContextConfig;
import org.junit.Test;

/**
 * Created by Rokas on 14/11/2015.
 */
public class SimulatorBuilderTests extends junit.framework.TestCase {

    @Test
    public void testSimBuilder1() throws Exception{
        TrialTableModel tableModel = new TrialTableModel(false);
        tableModel.setValueAt("group name test", 0, 0);
        tableModel.setValueAt("2AB+", 0, 1);
        Simulator sim = getSimulatorWithTestSettings();
        SimulatorBuilder.initSimulator(tableModel, sim);
        assertTrue(sim!=null);
        assertTrue(sim.getGroups().size() == 1);
        assertTrue(sim.getGroups().get(0).Name == "group name test");
        assertTrue(sim.getCsParameters().size()==2*3);
        assertTrue(sim.getCsParameters().get(0).CueName == 'A');
        assertTrue(sim.getCsParameters().get(1).CueName == 'B');
        assertTrue(sim.getGroups().get(0).groupPhases.size()==1);
        assertTrue(sim.getGroups().get(0).groupPhases.get(0).trials.size()==2);
        assertFalse(sim.getGroups().get(0).groupPhases.get(0).isRandom());
    }

    @Test
    public void testSimBuilder2() throws Exception{
        TrialTableModel tableModel = new TrialTableModel(false);
        tableModel.setValueAt("group name test", 0, 0);
        tableModel.setValueAt("2AAB+", 0, 1);
        tableModel.addPhase();
        tableModel.setValueAt("5B-", 0, 3);
        tableModel.setValueAt(true, 0, 4);
        Simulator sim = getSimulatorWithTestSettings();
        SimulatorBuilder.initSimulator(tableModel, sim);
        assertTrue(sim!=null);
        assertTrue(sim.getGroups().size() == 1);
        assertTrue(sim.getGroups().get(0).Name == "group name test");
        assertTrue(sim.getCsParameters().size() == 2 * 3);
        assertTrue(sim.getCsParameters().get(0).CueName == 'A');
        assertTrue(sim.getCsParameters().get(1).CueName == 'B');
        assertTrue(sim.getGroups().get(0).groupPhases.size() == 2);
        assertTrue(sim.getGroups().get(0).groupPhases.get(0).trials.size() == 2);
        assertFalse(sim.getGroups().get(0).groupPhases.get(0).isRandom());
        assertTrue(sim.getGroups().get(0).groupPhases.get(1).trials.size() == 5);
        assertTrue(sim.getGroups().get(0).groupPhases.get(1).isRandom());
        ConditionalStimulus csB_fromP1 = (ConditionalStimulus)sim.getGroups().get(0).groupPhases.get(0).trials.get(0).cuesPresent.get(1);
        ConditionalStimulus csB_fromP2 = (ConditionalStimulus)sim.getGroups().get(0).groupPhases.get(1).trials.get(0).cuesPresent.get(0);
        assertTrue(csB_fromP1 == csB_fromP2); // has to be the same object!
    }

    @Test
      public void testSimBuilder3() throws Exception {
        TrialTableModel tableModel = new TrialTableModel(false);
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
        Simulator sim = getSimulatorWithTestSettings();
        SimulatorBuilder.initSimulator(tableModel, sim);

        assertTrue(sim != null);
        assertTrue(sim.getGroups().size() == 2);
        assertTrue(sim.getGroups().get(0).Name == "group name test");
        assertTrue(sim.getGroups().get(1).Name == "group2 name test");
        assertTrue(sim.getCsParameters().size()==3*3);
        assertTrue(sim.getCsParameters().get(0).CueName == 'A');
        assertTrue(sim.getCsParameters().get(1).CueName == 'B');
        //group 1
        assertTrue(sim.getGroups().get(0).groupPhases.size() == 2);
        assertTrue(sim.getGroups().get(0).groupPhases.get(0).trials.size() == 2);
        assertFalse(sim.getGroups().get(0).groupPhases.get(0).isRandom());
        assertTrue(sim.getGroups().get(0).groupPhases.get(1).trials.size() == 5);
        assertFalse(sim.getGroups().get(0).groupPhases.get(1).isRandom());
        //group 2
        assertTrue(sim.getGroups().get(1).groupPhases.get(0).trials.size() == 1);
        assertTrue(sim.getGroups().get(1).groupPhases.get(0).isRandom());
        assertTrue(sim.getGroups().get(1).groupPhases.get(1).trials.size() == 5);
        assertFalse(sim.getGroups().get(1).groupPhases.get(1).isRandom());

        ConditionalStimulus csB_fromP1G1 = (ConditionalStimulus)sim.getGroups().get(0).groupPhases.get(0).trials.get(0).cuesPresent.get(1);
        ConditionalStimulus csB_fromP2G1 = (ConditionalStimulus)sim.getGroups().get(0).groupPhases.get(1).trials.get(1).cuesPresent.get(0);
        ConditionalStimulus csB_fromP1G2 = (ConditionalStimulus)sim.getGroups().get(1).groupPhases.get(0).trials.get(0).cuesPresent.get(1);
        ConditionalStimulus csB_fromP2G2 = (ConditionalStimulus)sim.getGroups().get(1).groupPhases.get(1).trials.get(1).cuesPresent.get(0);

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

    @Test
    public void testSimBuilder4() throws Exception {
        //test context
        Simulator sim = getSimulatorWithTestSettings();
        sim.getSettings().ContextSimulation = true;

        TrialTableModel tableModel = new TrialTableModel(true);
        //group 1
        tableModel.setValueAt("group name test", 0, 0);
        tableModel.setValueAt("2AB+", 0, 1); //phase description
        tableModel.setValueAt(false, 0, 2); //random
        tableModel.setValueAt(new ContextConfig(), 0, 3); //context config
        tableModel.setValueAt(5, 0, 4); //iti ratio


        SimulatorBuilder.initSimulator(tableModel, sim);

        assertTrue(sim != null);
        assertTrue(sim.getGroups().size() == 1);
        assertTrue(sim.getGroups().get(0).Name == "group name test");

        //group 1
        assertTrue(sim.getGroups().get(0).groupPhases.size() == 1);
        assertFalse(sim.getGroups().get(0).groupPhases.get(0).isRandom());
        assertTrue(sim.getGroups().get(0).groupPhases.get(0).trials.size() == 12);

        assertTrue(sim.getGroups().get(0).groupPhases.get(0).trials.get(0).cuesPresent.size() == 2);
        assertTrue(sim.getGroups().get(0).groupPhases.get(0).trials.get(0).cuesPresent.get(0) instanceof ConditionalStimulus);
        assertTrue(sim.getGroups().get(0).groupPhases.get(0).trials.get(0).cuesPresent.get(0).getName().equals("A"));
        assertTrue(sim.getGroups().get(0).groupPhases.get(0).trials.get(0).cuesPresent.get(1) instanceof ConditionalStimulus);
        assertTrue(sim.getGroups().get(0).groupPhases.get(0).trials.get(0).cuesPresent.get(1).getName().equals("B"));
        for(int i=1;i<6;i++) {
            assertTrue(sim.getGroups().get(0).groupPhases.get(0).trials.get(i).cuesPresent.size() == 1);
            assertTrue(sim.getGroups().get(0).groupPhases.get(0).trials.get(i).cuesPresent.get(0) instanceof ContextStimulus);
            assertTrue(sim.getGroups().get(0).groupPhases.get(0).trials.get(i).cuesPresent.get(0).getName().equals(ContextConfig.Context.PHI.toString()));
        }
    }

    private Simulator getSimulatorWithTestSettings(){
        Simulator testSimulator = new Simulator();
        testSimulator.getSettings().CompoundResults = false;
        testSimulator.getSettings().ContextSimulation = false;
        return testSimulator;
    }
}
