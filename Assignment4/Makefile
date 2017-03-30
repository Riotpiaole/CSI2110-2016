BUILDPATH= .:./src/*.java

all:

build:
	javac -cp $(BUILDPATH) src/*.java

run:
	java -cp $(BUILDPATH) src/MyGraphTest1
	java -cp $(BUILDPATH) src/MyGraphTest123
	java -cp $(BUILDPATH) src/PartitionTest2
	java -cp $(BUILDPATH) src/Clusters2DTest123

testing:
	cat README.md
	make build
	make run
	make clean

clean:
	rm -f src/*.class
