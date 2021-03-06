package in.edu.ashoka.surf;

import in.edu.ashoka.surf.*;
import edu.tsinghua.dbgroup.EditDistanceClusterer;
import in.edu.ashoka.surf.Dataset;
import in.edu.ashoka.surf.Row;
import in.edu.ashoka.surf.util.Timers;
import static java.util.stream.Collectors.toList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

class Test1 {

	private static final String path = "/Users/priyamgarrg21/Documents/Aditya/Internship@Ashoka/TCPD_GE_Delhi_2020-6-18.csv";

	public static void main(String args[]) throws IOException {
		Dataset dataset = Dataset.getDataset(path);
		Filter filter = new Filter(null);
		String fieldName = "Candidate";
		int maxEditDistance = 1;
		List<Collection<Row>> classes;
		
//		Set<String> names = d.getRows().stream().map(r -> r.get("Candidate")).collect(Collectors.toSet());
//
//		EditDistanceClusterer edc = new EditDistanceClusterer(5);
//		names.forEach(edc::populate);
//		List<Set<String>> clusters = (List) edc.getClusters();
//
//		int i = 0;
//		for (Set<String> cluster : clusters) {
//			System.out.println("Cluster " + i++ + " -------");
//			for (String s : cluster)
//				System.out.println(s);
//		}

		Collection<Row> filteredRows = filter.isEmpty() ? dataset.getRows()
				: dataset.getRows().stream().filter(filter::passes).collect(toList());

		SetMultimap<String, Row> fieldValueToRows = HashMultimap.create();
		filteredRows.forEach(r -> fieldValueToRows.put(r.get(fieldName), r));

		// do the clustering based on ed (but only if ed > 0)

		List<Set<String>> clusters;

		if (maxEditDistance >= 1) {
			final EditDistanceClusterer edc = new EditDistanceClusterer(maxEditDistance);
			filteredRows.forEach(r -> edc.populate(r.get(fieldName)));
			clusters = (List) edc.getClusters();
		} else {
			// handle the case when edit distance is 0 by creating a list of single-element
			// sets with all unique fieldVal's
			clusters = new ArrayList<>();
			for (String fieldVal : fieldValueToRows.keySet()) {
				// create a set with a single val
				Set set = new LinkedHashSet<String>();
				set.add(fieldVal);
				clusters.add(set);
			}
		}

		// compute the result of this algorithm
		classes = new ArrayList<>();
		for (Set<String> cluster : clusters) {
			System.out.println("----gjkkklkjhgfds------");
			System.out.println(cluster);
			final Collection<Row> rowsForThisCluster = new ArrayList<>();
			// cluster just has strings, convert each string in the cluster to its rows, and
			// add it to rowsForThisCluster
			cluster.forEach(s -> {
				rowsForThisCluster.addAll(fieldValueToRows.get(s));
			});
			classes.add(rowsForThisCluster);
		}

	}
}

