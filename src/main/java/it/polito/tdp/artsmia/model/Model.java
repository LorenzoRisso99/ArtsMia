package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<ArtObject,DefaultWeightedEdge> grafo;
	
	private ArtsmiaDAO dao;
	
	private Map<Integer,ArtObject> idMap;
	
public Model() {
		
		dao = new ArtsmiaDAO();
		idMap = new HashMap<Integer,ArtObject>();
		
	}
	
	public void creaGrafo() {        //Lo chiamo ogni volta che devo creare in grafo con bottone fxml
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);         //posso ricrearlo piu volte
		
		//Aggiungo Vertici
		
		dao.listObjects(idMap);
		
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		//Aggiungo Archi
		
		//APPROCCIO 1   ----> Ho vertici e faccio doppio ciclo for per recuperarmi coppie di vertici e chiedere a databese se sono collegati 
		
//		for(ArtObject a1: this.grafo.vertexSet()) {
//			for(ArtObject a2: this.grafo.vertexSet()) {
//				
//				//Chiedo a database se ogni coppia sono collegati
//				if(!a1.equals(a2) && !this.grafo.containsEdge(a1,a2)) {         //Controllo che non ci sia gia arco esistente
//					//Chiedo a db se devo collegare a1 e a2
//					int peso= dao.getPeso(a1,a2);
//					if(peso > 0) {                                               //Se il peso è maggiore di 0 esiste arco
//						Graphs.addEdgeWithVertices(this.grafo, a1, a2, peso);
//					}
//				}
//				
//			}
//		}            //Non giunge a termine, lunghissimo e gira infinito
		
		
		//APPROCCIO 2   ----> 
		
		for(Adiacenza  a: this.dao.getAdiacenze(idMap)) {
			
			Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			
		}
		
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	public ArtObject getObject(int objectId) {
		return idMap.get(objectId);
	}
	
	//PUNTO D

	public int getComponenteConnessa(ArtObject vertice) {   //Visitiamo grafo e recuperiamo vertici visitati e li mettiamo nel set
		
		Set<ArtObject> visitati = new HashSet<>();
		
		DepthFirstIterator<ArtObject,DefaultWeightedEdge> it = 
				new DepthFirstIterator<ArtObject, DefaultWeightedEdge>(this.grafo, vertice);
		while (it.hasNext()) {
			visitati.add(it.next());
		}
		
		return visitati.size();    //restituiamo size del set
	}
	

}
