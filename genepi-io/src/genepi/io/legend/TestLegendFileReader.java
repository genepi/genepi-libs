package genepi.io.legend;

import java.io.IOException;

public class TestLegendFileReader {

	public static void main(String[] args) throws IOException {

		LegendFileReader reader = new LegendFileReader(
				"/home/lukas/raw-data/reference-panels/ALL_1000G_phase1integrated_v3_annotated_legends/ALL_1000G_phase1integrated_v3_chr20_impute.legend.gz",
				"eur");

		reader.createIndex();
		reader.initSearch();

		LegendEntry line = reader.findByPosition(74545);
		System.out.println(line);

		line = reader.findByPosition(62965162);
		System.out.println(line);

	}

}
