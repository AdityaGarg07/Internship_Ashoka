package in.edu.ashoka.surf;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import edu.tsinghua.dbgroup.EditDistanceClusterer;
import in.edu.ashoka.surf.util.Timers;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import java.io.IOException;

public class CosineSimilarityAlgo extends MergeAlgorithm {

    private final int inputval;
    private final String fieldName; // fieldname on which to compute edit distance
    private final Filter filter;

    /* set up merge algorithm parameters: d, the fieldName (col. name) of the field on which edit distance clustering is to be done, max. editDistance (inclusive) */
    CosineSimilarityAlgo(Dataset dataset, String fieldName, int inputval, Filter filter) {
        super (dataset);
        this.filter = filter;

        // set up desi versions of the given field. we'll perform edit distance computation on this version of the given field, not the original one.
        this.fieldName = fieldName;
        this.inputval = inputval;
    }

    @Override
    public List<Collection<Row>> run() {
		Collection<Row> filteredRows = filter.isEmpty() ? dataset.getRows() : dataset.getRows().stream().filter(filter::passes).collect(toList());

		SetMultimap<String, Row> fieldValueToRows = HashMultimap.create();
		filteredRows.forEach(r -> fieldValueToRows.put(r.get(fieldName), r));

		// do the clustering based on ed (but only if ed > 0)

		filteredRows.forEach(r -> System.out.println(r.get(fieldName)));
		double acc = inputval/100.0;

		System.out.println("--------------------------------------------------------------------" + acc);
		CosineFunc func = new CosineFunc();

		List<Set<String>> clusters;
		

		

		clusters = func.assign_similarity(filteredRows,fieldName,acc);
//		for (Row fil : filteredRows) {
////			System.out.println(fil.get(fieldName));
//			Set setx = new LinkedHashSet<String>();
//			setx.add(fil.get(fieldName));
//			clusters.add(setx);
//		}
		
		int key = 0;
		classes = new ArrayList<>();
		for (Set<String> cluster : clusters) {
			System.out.println(key++ + " " + cluster);
			final Collection<Row> rowsForThisCluster = new ArrayList<>();
			// cluster just has strings, convert each string in the cluster to its rows, and
			// add it to rowsForThisCluster
			cluster.forEach(s -> {
				rowsForThisCluster.addAll(fieldValueToRows.get(s));
			});
			classes.add(rowsForThisCluster);
		}

		System.out.println("--------------------------------------------------------------------");

		classes.forEach(x -> System.out.println(x.toString()));
		return classes;

    }

    /* debug method */
    void dumpClasses() {
        for (Collection<Row> rows: classes) {
            log.info (rows.iterator().next().get(fieldName));
        }
    }

    public String toString() { return "The cosine similarity algorithm works fine with inputval" + inputval; }
    
}