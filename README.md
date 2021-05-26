genepi-libs
===========

This repository is the home of all of our libraries which are used in many of our scientific projects. All artifacts are located in our [maven-repository](https://bintray.com/genepi/maven). To use this repository, add the following configuration to your `pom.xml` file or gradle script:

```
<repositories>
	<repository>
		<id>jfrog-genepi-maven</id>
		<name>jfrog-genepi-maven</name>
		<url>https://genepi.jfrog.io/artifactory/maven/</url>
	</repository>
</repositories>
```

## genepi-io

[ ![Download](https://api.bintray.com/packages/genepi/maven/genepi-io/images/download.svg) ](https://bintray.com/genepi/maven/genepi-io/_latestVersion)
![build genepi-io](https://github.com/genepi/genepi-libs/workflows/build%20genepi-io/badge.svg)

A small java library to simplify working with files.

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
![build genepi-hadoop](https://github.com/genepi/genepi-libs/workflows/build%20genepi-hadoop/badge.svg)

A small java library to simplify writing Hadoop jobs.

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
![build genepi-db](https://github.com/genepi/genepi-libs/workflows/build%20genepi-db/badge.svg)

A small java library to simplify working with databases (e.g. mysql or h2)
 
 ```
 <dependency>
  <groupId>genepi</groupId>
  <artifactId>genepi-db</artifactId>
  <version>1.2.0</version>
  <type>pom</type>
</dependency>
```
