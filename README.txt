ReadMe by Ewan Brown, 03/05/2019


IntelliJ seems to automatically select the wrong main class, 
	since there are some test-main functions in this project.
	
It also seems to be setup to build a JAR and run that. which doesn't seem necessary.


=================================================================================
How to configure launch for project.
---------------------------------------------------------------------------------

Go ro Run>Edit COnfigurations

Click the plus sign in the top left of the popup menu and select "APPLICATION".
 
On the right, give the configuration a name ("Log Viewer")

select the "Main Class: " empty box and select the LogViewer class (com.bkin.logviewer.gui.LogViewer)

Select 'OK' and proceed.

==================================================================================


==================================================================================
How to build the JAR for distribution
----------------------------------------------------------------------------------

Open project Structure (File > Project Structure)

Select the Artifacts tab on the left

Select the plus sign in the top left > JAR > From modules with dependancies

In the popup, select "Main Class" empty box and select the LogViewer class

	Make sure 'extract to the target jar' is selected	
	Select OK

	[IF THE ABOVE RESULTS IN ERROR]
		repeat steps
		select a new directory for META-INF. put it in a new folder.
	
take note of the 'Output Directory'. this is where the built JARs will end up. 
IntelliJ likes to make a directory in weird folder names like "LogViewer_jar1"

To build the jar return to main intelliJ window
Build > Build artifacts > select the jar configuration that pops up.

Now go to the output directory to get the built jar, ready to go.
-----------------------------------------------------------------------------------