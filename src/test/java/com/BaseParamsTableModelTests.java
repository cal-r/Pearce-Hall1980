package com;

import Models.Parameters.Parameter;
import ViewModels.TableModels.CSParamsTableModel;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 08/11/2015.
 */
public class BaseParamsTableModelTests extends junit.framework.TestCase {

private final String PARAM_NAME = "param name";

    @org.junit.Test
    public void testSetUpParams() throws Exception {
        CSParamsTableModel tableModel = new CSParamsTableModel();
        int numOfParams = 10;
        List<Parameter> params = createMockParams(numOfParams);
        tableModel.setUpParameters(params);
        for(int i=0;i<numOfParams;i++) {
            Parameter rowParam = params.get(i);
            assertTrue(tableModel.getValueAt(i, 0).equals(rowParam.getDisplayName()));
            assertTrue(tableModel.getValueAt(i, 1).equals(rowParam.getValue()));
        }
    }

    public void testUpdateParams() throws Exception{
        CSParamsTableModel tableModel = new CSParamsTableModel();
        int numOfParams = 10;
        List<Parameter> params = createMockParams(numOfParams);
        tableModel.setUpParameters(params);

        double newVal = Integer.MIN_VALUE;
        int rowId = 5;
        Parameter rowParam = params.get(rowId);
        tableModel.setValueAt(newVal, rowId, 1);
        verify(rowParam, times(1)).setValue(newVal);
    }

    private List<Parameter> createMockParams(int numOfParams){
        List<Parameter> params = new ArrayList<>();
        for(int i=0;i<numOfParams;i++) {
            Parameter paramMock = mock(Parameter.class);
            when(paramMock.getDisplayName()).thenReturn(PARAM_NAME + i);
            when(paramMock.getValue()).thenReturn((double) i);
            params.add(paramMock);
        }
        return params;
    }
}
