package tp;



import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;





public class flow{
    public static void main(String args[]){
	Graph G = new SingleGraph("G");

	//Creation des noeuds

	G.addNode("Usine1");
	G.addNode("Usine2");

	G.addNode("s");

	G.addNode("PF1");
	G.addNode("PF4");
	G.addNode("PF2");
	G.addNode("PF3");
	G.addNode("PF5");

	G.addNode("t");

	G.addNode("Client1");
	G.addNode("Client2");
	G.addNode("Client3");


	//Creation des arcs
	G.addEdge("s-Usine1", "s", "Usine1", true);
	G.addEdge("s-Usine2", "s", "Usine2", true);

	G.addEdge("Usine1-PF1", "Usine1", "PF1",true);
	G.addEdge("Usine1-PF2", "Usine1", "PF2",true);
	G.addEdge("Usine1-PF3", "Usine1", "PF3",true);

	G.addEdge("Usine2-PF2", "Usine2", "PF2",true);
	G.addEdge("Usine2-PF3", "Usine2", "PF3",true);

	G.addEdge("PF1-PF2", "PF1", "PF2",true);
	G.addEdge("PF1-PF4", "PF1", "PF4",true);


	G.addEdge("PF2-Client1", "PF2", "Client1",true);
	G.addEdge("PF2-Client2", "PF2", "Client2",true);
	G.addEdge("PF2-Client3", "PF2", "Client3",true);

	G.addEdge("PF3-PF5", "PF3", "PF5",true);

	G.addEdge("PF4-Client1", "PF4", "Client1",true);
	G.addEdge("PF4-Client2", "PF4", "Client2",true);

	G.addEdge("PF5-Client2", "PF5", "Client2",true);
	G.addEdge("PF5-Client3", "PF5", "Client3",true);


	G.addEdge("Client1-t", "Client1", "t",true);
	G.addEdge("Client2-t", "Client2", "t",true);
	G.addEdge("Client3-t", "Client3", "t",true);


	FordFulkerson ff = new FordFulkerson();

	ff.init(G);

	ff.setCapacity("s","Usine1",35);
	ff.setCapacity("s","Usine2",25);

	ff.setCapacity("Usine1","PF1",20);
	ff.setCapacity("Usine1","PF2",15);
	ff.setCapacity("Usine1","PF3",12);

	ff.setCapacity("Usine2","PF2",6);
	ff.setCapacity("Usine2","PF3",22);

	ff.setCapacity("PF1","PF4",15);
	ff.setCapacity("PF1","PF2",10);

	ff.setCapacity("PF2","Client1",10);
	ff.setCapacity("PF2","Client2",15);
	ff.setCapacity("PF2","Client3",15);

	ff.setCapacity("PF3","PF5",22);

	ff.setCapacity("PF4","Client1",7);
	ff.setCapacity("PF4","Client2",10);

	ff.setCapacity("PF5","Client2",10);
	ff.setCapacity("PF5","Client3",10);


	ff.setCapacity("Client1","t",15);
	ff.setCapacity("Client2","t",15);
	ff.setCapacity("Client3","t",20);

	ff.compute();


	Graph graph_aa = new SingleGraph("G_arcs_arrieres");

	graph_aa.addNode("s");
	graph_aa.addNode("1");
	graph_aa.addNode("2");
	graph_aa.addNode("t");

	graph_aa.addEdge("s-1", "s", "1", true);
	graph_aa.addEdge("s-2", "s", "2", true);
	graph_aa.addEdge("1-2", "1", "2", true);
	graph_aa.addEdge("1-t", "1", "t", true);	
	graph_aa.addEdge("2-t", "2", "t", true);	

	FordFulkerson ff_aa = new FordFulkerson();

	ff_aa.init(graph_aa);
	
	ff_aa.setCapacity("s","1",1);
	ff_aa.setCapacity("s","2",1);
	ff_aa.setCapacity("1","2",1);
	ff_aa.setCapacity("2","t",1);
	ff_aa.setCapacity("1","t",1);

	ff_aa.compute();
	
    }

}
