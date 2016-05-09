import java.io.File;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;


public class Runner {

	private static enum RelTypes implements RelationshipType
	{
	    KNOWS
	}

	private static final String DB_PATH = "/home/barla/workspace/NEO4J";
	
	private static void registerShutdownHook( final GraphDatabaseService graphDb )
	{
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running application).
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	            graphDb.shutdown();
	        }
	    } );
	}
	
	public static void main(String[] args) {
		
		GraphDatabaseService graphDb;
		Node firstNode;
		Node secondNode;
		Relationship relationship;
		
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( new File(DB_PATH) );
		registerShutdownHook( graphDb );
		
		try ( Transaction tx = graphDb.beginTx() )
		{
		    // Database operations go here
		
		firstNode = graphDb.createNode();
		firstNode.setProperty( "message", "Hello, " );
		secondNode = graphDb.createNode();
		secondNode.setProperty( "message", "World!" );

		relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
		relationship.setProperty( "message", "brave Neo4j " );
		
		System.out.print( firstNode.getProperty( "message" ) );
		System.out.print( relationship.getProperty( "message" ) );
		System.out.print( secondNode.getProperty( "message" ) );
		
		// let's remove the data
		firstNode.getSingleRelationship( RelTypes.KNOWS, Direction.OUTGOING ).delete();
		firstNode.delete();
		secondNode.delete();
	    tx.success();
		}

		
		graphDb.shutdown();

	}

}
