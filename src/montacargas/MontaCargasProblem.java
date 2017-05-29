package montacargas;

import agent.Action;
import agent.Problem;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MontaCargasProblem extends Problem<MontaCargasState> {
    
    public MontaCargasProblem(MontaCargasState initalState) {
        super(initalState, new ArrayList<Action>());
        
    }

    public List<MontaCargasState> executeActions(MontaCargasState state) {
        List<MontaCargasState> successors = new LinkedList<MontaCargasState>();
        actions.clear();
        actions.addAll(state.getActions());
        for (Action a : actions) {
            if (a.isValid(state)) {
                
                MontaCargasState successor = (MontaCargasState) state.clone();
                a.execute(successor);
                successors.add(successor);
            }
        }
        return successors;
    }

    public boolean isGoal(MontaCargasState state) {
        //Só é preciso saber se o carro esta a frente da porta
        return state.getColumnCarro() == state.getNumColumns()-1;
    }
 
    @Override
    public double computePathCost(List<Action> path) {
        return path.size();
    }
}
