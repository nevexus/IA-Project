package agent;

import java.util.ArrayList;
import searchmethods.*;
import static utils.FileOperations.appendToTextFile;
import static utils.FileOperations.createNecessaryDirectories;
import static utils.FileOperations.createStatisiticsHeaderFile;
import static utils.FileOperations.fileExist;

public class Agent<E extends State> {

    protected E environment; //E - representa o estado
    protected ArrayList<SearchMethod> searchMethods;
    protected SearchMethod searchMethod;
    protected ArrayList<Heuristic> heuristics;
    protected Heuristic heuristic;
    protected Solution solution;

    public Agent(E environment) {
        this.environment = environment;
        searchMethods = new ArrayList<SearchMethod>();
        searchMethods.add(new BreadthFirstSearch());
        searchMethods.add(new UniformCostSearch());
        searchMethods.add(new DepthFirstSearch());
        searchMethods.add(new DepthLimitedSearch());
        searchMethods.add(new IterativeDeepeningSearch());
        searchMethods.add(new GreedyBestFirstSearch());
        searchMethods.add(new AStarSearch());
        searchMethods.add(new BeamSearch());
        searchMethods.add(new IDAStarSearch());
        searchMethod = searchMethods.get(0);
        heuristics = new ArrayList<Heuristic>();
    }

    public Solution solveProblem(Problem problem) {
        if (heuristic != null) {
            problem.setHeuristic(heuristic);
            heuristic.setProblem(problem);
        }
        solution = searchMethod.search(problem);
        return solution;
    }

    public void executeSolution() {    
        for(Action action : solution.getActions()){
            environment.executeAction(action);
        }
    }

    public boolean hasSolution() {
        return solution != null;
    }

    public void stop() {
        getSearchMethod().stop();
    }

    public boolean hasBeenStopped() {
        return getSearchMethod().hasBeenStopped();
    }

    public E getEnvironment() {
        return environment;
    }

    public void setEnvironment(E environment) {
        this.environment = environment;
    }

    public SearchMethod[] getSearchMethodsArray() {
        SearchMethod[] sm = new SearchMethod[searchMethods.size()];
        return searchMethods.toArray(sm);
    }

    public SearchMethod getSearchMethod() {
        return searchMethod;
    }

    public void setSearchMethod(SearchMethod searchMethod) {
        this.searchMethod = searchMethod;
    }

    public Heuristic[] getHeuristicsArray() {
        Heuristic[] sm = new Heuristic[heuristics.size()];
        return heuristics.toArray(sm);
    }

    public Heuristic getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    public String getSearchReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(searchMethod + "\n");
        if (solution == null) {
            sb.append("No solution found\n");
        } else {
            sb.append("Solution cost: " + Double.toString(solution.getCost()) + "\n");
        }
        sb.append("Num of expanded nodes: " + searchMethod.getStatistics().numExpandedNodes + "\n");
        sb.append("Max frontier size: " + searchMethod.getStatistics().maxFrontierSize + "\n");
        sb.append("Num of generated nodes: " + searchMethod.getStatistics().numGeneratedNodes+ "\n");

        return sb.toString();
    }
    
    public void saveSearchReportToFile(long time, String puzzleName) {
        String fileName = "Statistics\\Statistics_"+puzzleName+".xls";
        if (!fileExist(fileName)){
            if (createNecessaryDirectories(fileName))
                createStatisiticsHeaderFile(fileName);
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(searchMethod + "\t");
        if (solution == null) {
            sb.append("No solution found\t");
        } else {
            sb.append(Double.toString(solution.getCost()) + "\t");
        }
        sb.append(searchMethod.getStatistics().numExpandedNodes + "\t");
        sb.append(searchMethod.getStatistics().maxFrontierSize + "\t");
        sb.append(searchMethod.getStatistics().numGeneratedNodes+ "\t");
        sb.append(time+ "\t");
        if (searchMethod instanceof BeamSearch) {
            sb.append(((InformedSearch) searchMethod).getHeuristic().toString());
            sb.append("\t" + ((BeamSearch) searchMethod).getBeamSize());
        } else if (searchMethod instanceof DepthLimitedSearch) {
            sb.append("None");
            sb.append("\t" + ((DepthLimitedSearch) searchMethod).getLimit());
        } else if (searchMethod instanceof InformedSearch) {
            sb.append(((InformedSearch) searchMethod).getHeuristic().toString());
            sb.append("\tNone");
        } else {
            sb.append("None\tNone");
        }
        sb.append("\n");

        appendToTextFile(fileName, sb.toString());
    }
}
