package genepi.hadoop.tests.commands;

import genepi.hadoop.command.PipedCommand;
import genepi.hadoop.command.Pipeline;

public class PipeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("-> sleep <-");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("-> start <-");
		
		String bamFile = "NA12272.mapped.ILLUMINA.bwa.CEU.low_coverage.20101123.chrom20.20000001.20300000.bam";
		String samtools = "/home/lukas/software/mapping/bin/samtools-hybrid";

		Pipeline pipeline = new Pipeline("bam to glf");

		PipedCommand step1 = new PipedCommand(samtools);
		step1.setParams("view", "-q", "20", "-F", "0x0704", "-uh", bamFile,
				"20:20000001-20300050");
		pipeline.add(step1);

		PipedCommand step2 = new PipedCommand(samtools);
		step2.setParams("calmd", "-Abr", "-", "human.g1k.v37.chr20.fa");
		pipeline.add(step2);

		PipedCommand step3 = new PipedCommand(samtools);
		step3.setParams("pileup", "-f", "human.g1k.v37.chr20.fa", "-g", "-");
		step3.saveStdOut("result.glf");
		pipeline.add(step3);

		pipeline.execute();

		System.out.println("-> sleep <-");		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("-> end <-");		
	}

}
