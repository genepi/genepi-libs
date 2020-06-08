genepi-libs
===========

This repository is the home of all of our libraries which are used in many of our scientific projects. All artifacts are located in our [maven-repository](https://bintray.com/genepi/maven). To use this repository, add the following configuration to your `pom.xml` file or gradle script:

```
<repository>
    <id>bintray-genepi-maven</id>
    <name>bintray</name>
    <url>https://dl.bintray.com/genepi/maven</url>
</repository>
```

## genepi-io

[ ![Download](https://api.bintray.com/packages/genepi/maven/genepi-io/images/download.svg) ](https://bintray.com/genepi/maven/genepi-io/_latestVersion)
![build genepi-io](https://github.com/genepi/genepi-libs/workflows/build%20genepi-io/badge.svg)

Processing genomic file formats (e.g. vcf, csv, plink, bed, etc.)

```
<dependency>
  <groupId>genepi</groupId>
  <artifactId>genepi-io</artifactId>
  <version>1.0.12</version>
  <type>pom</type>
</dependency>
```

## genepi-hadoop

[ ![Download](https://api.bintray.com/packages/genepi/maven/genepi-hadoop/images/download.svg) ](https://bintray.com/genepi/maven/genepi-hadoop/_latestVersion)
 
Communicating with a Cloudera Hadoop Cluster (mrv1 & yarn) 

```
<dependency>
  <groupId>genepi</groupId>
  <artifactId>genepi-hadoop</artifactId>
  <version>mr1-1.4.1</version>
  <type>pom</type>
</dependency>
```

## genepi-db

[ ![Download](https://api.bintray.com/packages/genepi/maven/genepi-db/images/download.svg) ](https://bintray.com/genepi/maven/genepi-db/_latestVersion)
Communicating with relational databases (mysql, h2)
 
 ```
 <dependency>
  <groupId>genepi</groupId>
  <artifactId>genepi-db</artifactId>
  <version>1.2.0</version>
  <type>pom</type>
</dependency>
```
