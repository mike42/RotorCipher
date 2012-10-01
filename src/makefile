BINPATH = ../bin/

JCC = javac
RM = rm
RMDIR = rmdir
JAR = jar
CD = cd
CHMOD = chmod

default:
	$(JCC) RotorCipher.java -d $(BINPATH)

jar:
	$(JCC) RotorCipher.java -d $(BINPATH)
	cd $(BINPATH); \
		$(JAR) -cfe RotorCipher.jar RotorCipher *; \
		$(CHMOD) +x RotorCipher.jar;
	$(RM) $(BINPATH)*.class $(BINPATH)*/*.class
	$(RMDIR) $(BINPATH)*/

clean:
	$(RM) -f $(BINPATH)*.class $(BINPATH)*/*.class $(BINPATH)*.jar
	$(RMDIR) $(BINPATH)*
