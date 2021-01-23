package tp;


import org.graphstream.algorithm.flow.FlowAlgorithmBase;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Iterator;

public class FordFulkerson extends FlowAlgorithmBase {




    protected LinkedList<Node> chaineAmeliorante(){

	LinkedList<Node> res = new LinkedList<Node>();
	LinkedList<Node> Q = new LinkedList<Node>();
	HashSet<String> visite = new HashSet<String>();
	Node i;
	Boolean arc_arriere;


	//Marquer s
	visite.add( this.getFlowSourceId() );

	//Enfiler(Q,s)
	Q.add( this.flowGraph.getNode( this.getFlowSourceId() ) );

	do{
	    //Defiler(Q)
	    i = Q.pollFirst();
	    arc_arriere = true;

	    //Pour chaque j de N+
	    for(Node j : this.successeurs(i)){

		//Tel que capacite > flux
		if (capaciteDisponible(i,j) > 0 && !visite.contains(j.getId())){
		    //Marquer j
		    visite.add(j.getId());
		    //Enfiler(Q,j)
		    Q.add(j);
		    j.setAttribute("origine",i);
		    arc_arriere = false;
		}
	    }//Fin Pour

	    // Arcs arrières
	    for(Node j : this.predecesseurs(i)){

		//Tel que capacite > flux
		if( this.getFlow(j,i) > 0 && !visite.contains(j.getId())){
		    //Marquer j
		    visite.add(j.getId());
		    //Enfiler(Q,j)
		    Q.add(j);
		    System.out.println("ajout de la possibilité d'un arc arriere : "+j);
		    j.setAttribute("origine",i);


		}

	    }

	} while (!Q.isEmpty() && !visite.contains(this.getFlowSinkId() ));

	if( visite.contains(this.getFlowSinkId()) )
	    return obtenirChaine();
	else
	    return new LinkedList<Node>();
    }



    protected LinkedList<Node> obtenirChaine(){
	Node x = this.flowGraph.getNode( this.getFlowSinkId() );
	Node y;
	LinkedList<Node> res = new LinkedList<Node>();
	do{
	    y = (Node)x.getAttribute("origine");
	    res.addFirst(x);
	    x = y;
	} while(x != null );
	//this.retirerOrigineNoeuds();
	System.out.print("\naugPath : ");
	for(Node noeud : res){
	    System.out.print(noeud.getId()+", ");
	}
	System.out.println("]"); //Je sais il y a une virgule en trop mais flemme
	return res;
    }




    protected double capaciteDisponible(Node n1,Node n2){
	return this.getCapacity(n1,n2) - this.getFlow(n1,n2);
    }


    protected double calculDelta(LinkedList<Node> mu){
	double delta = Double.MAX_VALUE;

	Iterator<Node> iterateur = mu.iterator();
	Node a,b = iterateur.next();
	while(iterateur.hasNext()){
	    a = b;
	    b = iterateur.next();

	    // gerer les arcs avants
	    if (this.successeurs(a).contains(b)){
		delta = Math.min( delta,this.capaciteDisponible(a,b));
	    } else {

		// gerer les arcs arrièdelta
		if (this.successeurs(b).contains(a)){
		    delta = Math.min( delta, this.getFlow(b,a));
		}
	    }

	}
	return delta;
    }


    protected void majFlux(LinkedList<Node> mu, double delta){
	Iterator<Node> iterateur = mu.iterator();
	Node a,b = iterateur.next();
	while(iterateur.hasNext() ){
	    a = b;
	    b = iterateur.next();
	    //gerer les arcs avants
	    if (this.successeurs(a).contains(b)){
		this.setFlow( a,b,this.getFlow(a,b) + delta );
	    }else{

		// gerer les arcs arrières
		if (this.successeurs(b).contains(a)){
		    this.setFlow( a,b,this.getFlow(a,b) - delta );
		}
	    }
	}

    }


    protected double max_recherche() {

	double max1 = 0;
	double max2 = 0;
	for(Node j : this.predecesseurs(this.flowGraph.getNode(this.getFlowSinkId()))){
	    max1 +=  this.getCapacity(j, this.flowGraph.getNode(this.getFlowSinkId()));
	}
	for(Node j : this.successeurs(this.flowGraph.getNode(this.getFlowSourceId()))){
	    max2 +=  this.getCapacity(this.flowGraph.getNode(this.getFlowSourceId()),j);
	}
	return Math.min(max1,max2);
    }



    public void compute(){
	System.out.println("\nAlgorithme de Ford-Fulkerson appliqué au graphe "+this.flowGraph.getId() );
	System.out.println("source : "+this.getFlowSourceId() );
	System.out.println("puit : "+this.getFlowSinkId() );
	this.maximumFlow = 0;
	double max_rechercher = max_recherche();
	System.out.println("Le graphe a une capacité possible max de :"+max_rechercher);


	LinkedList<Node> mu = this.chaineAmeliorante();

	double delta;

	while( !mu.isEmpty() ){
	    delta = this.calculDelta(mu);
	    System.out.println("min residual capacity : "+delta);
	    this.majFlux(mu,delta);
	    this.maximumFlow += delta;

	    System.out.println("\nfinal flow : "+this.maximumFlow );
	    if (this.maximumFlow == max_rechercher){
		break;
	    }
	    mu = this.chaineAmeliorante();

	}
	this.afficherGraph();
    }





    protected String trouverIDSource(Graph graph){
	Node source = graph.getNode(0);
	ArrayList<Node> preds = predecesseurs(source);
	while(preds.size() != 0){
	    source = preds.get(0);
	    preds = predecesseurs(source);
	}
	return source.getId();

    }



    protected String trouverIDPuit(Graph graph){
	Node puit = graph.getNode(graph.getNodeCount()-1);
	ArrayList<Node> succs = successeurs(puit);
	while(succs.size() != 0){
	    puit = succs.get(0);
	    succs = successeurs(puit);
	}
	return puit.getId();

    }

    public void init(Graph graph){
	this.flowGraph = graph;
	this.checkArrays();

	this.setSourceId(trouverIDSource(graph));
	this.setSinkId(trouverIDPuit(graph));

    }


    public static ArrayList<Node> predecesseurs(Node sommet){
	int degreEntrant = sommet.getInDegree();
	ArrayList<Node> res = new ArrayList<Node>(degreEntrant);

	for(int i=0;i<degreEntrant;i++){
	    res.add(sommet.getEnteringEdge(i).getSourceNode());
	}
	return res;
    }


    public static ArrayList<Node> successeurs(Node sommet){
	int degreSortant = sommet.getOutDegree();
	ArrayList<Node> res = new ArrayList<Node>(degreSortant);

	for(int i=0;i<degreSortant;i++){
	    res.add(sommet.getLeavingEdge(i).getTargetNode());
	}
	return res;
    }


    public void afficherGraph(){
        System.setProperty("org.graphstream.ui", "swing");

        for(Node n:this.flowGraph) {
            n.setAttribute("ui.label", n.getId());
        }


        this.flowGraph.edges().forEach(e -> {
		e.setAttribute("ui.label", getFlow(e.getNode0(), e.getNode1()));
            });
        this.flowGraph.setAttribute("ui.stylesheet", style);


        this.flowGraph.display();

    }

    private String style =
	"graph {"+
	"	fill-color: white;}"+


	"node {"+
	"	size: 15px, 25px;"+
	"	shape: box;"+
	"	fill-color: #6495ED;"+
	"	stroke-mode: plain;"+
	"	stroke-color: yellow;}"+

	"node:clicked {"+
    	"fill-color: red;"+
	"}";



}
