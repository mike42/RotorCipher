BINPATH = ../bin/

JCC = javac
RM = rm
RMDIR = rmdir
JAR = jar

default:
	$(JCC) RotorCipher.java -d $(BINPATH)

jar:
	$(JCC) RotorCipher.java -d $(BINPATH)
	$(JAR) -cfe $(BINPATH)RotorCipher.jar RotorCipher *
	$(RM) $(BINPATH)*.class $(BINPATH)*/*.class
	$(RMDIR) $(BINPATH)*/

clean:
	$(RM) -f $(BINPATH)*.class $(BINPATH)*/*.class $(BINPATH)*.jar
	$(RMDIR) $(BINPATH)*
