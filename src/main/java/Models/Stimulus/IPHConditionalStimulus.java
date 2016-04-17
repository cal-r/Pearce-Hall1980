package Models.Stimulus;

/**
 * Created by Rokas on 17/04/2016.
 *
 * interface for P&H conditional stimulus (Ve, Vi, Alpha and Vnet)
 */
public interface IPHConditionalStimulus extends IConditionalStimulus{
    double getAlpha();
    double getAssociationExcitatory();
    double getAssociationInhibitory();
}
