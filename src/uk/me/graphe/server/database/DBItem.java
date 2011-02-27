package uk.me.graphe.server.database;

import java.util.Random;

import uk.me.graphe.shared.Graph;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class DBItem {

        @Id private int mGraphID = 0;
        
        @Embedded private Graph mGraph;
        
        /**
         * creates a database object from graph
         * 
         * @param instance
         *            the graph to be stored

         */
        public DBItem ( Graph instance) {
            mGraph = instance;
            mGraphID = genID();
        }
        
        private int genID() {
            Random generator = new Random(9103756);
            return generator.nextInt();
        }
        
        /**
         * gets the graph from the object
         * 
         * @return
         */
        public Graph getGraph() {
            return mGraph;
        }
        
        /**
         * gets the unique id to find this object
         * 
         * @return
         */
        public int getID () {
            return mGraphID;
        }
}
