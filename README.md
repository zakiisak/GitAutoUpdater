# GitAutoUpdater
A simple java program that auto updates an existing repository
The usage is simple:

<code>java -jar gau.jar launchCommand</code>
where <code>launchCommand</code> is replaced with a command to restart a process.
<br/><br/>
For instance, If this script was located in a maven project, a script file could be provided, that would execute steps to build the maven project again, and then launch the maven generated jar file.
<br/><br/>
Similarly, if this was a <code>node.js</code> project, the <code>launchCommand</code> could be something like <code>serve -l 5000 -s build</code>
<br/>
Example of full command then:
<br/><br/>
<code>java -jar gau.jar serve -l 5000 -s build</code>
