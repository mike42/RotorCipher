BINPATH = ../bin/

JCC = javac
RM = rm
RMDIR = rmdir
JAR = jar
CD = cd
CHMOD = chmod
MKDIR = mkdir

default:
	# Making 'bin' folder ...
	-$(MKDIR) $(BINPATH) 2> /dev/null
	# Compiling ...
	$(JCC) RotorCipher.java -d $(BINPATH)

jar:
	# Making 'bin' folder ...
	-$(MKDIR) $(BINPATH) 2> /dev/null
	# Compiling ...
	$(JCC) RotorCipher.java -d $(BINPATH)
	# Putting in JAR file
	cd $(BINPATH); \
		$(JAR) -cfe RotorCipher.jar RotorCipher *; \
		$(CHMOD) +x RotorCipher.jar;
	# Cleaning up .class files
	$(RM) $(BINPATH)*.class $(BINPATH)*/*.class
	-$(RMDIR) $(BINPATH)*/ 2> /dev/null

clean:
	-$(RM) -f $(BINPATH)*.class $(BINPATH)*/*.class $(BINPATH)*.jar
	-$(RMDIR) $(BINPATH)* 2> /dev/null
